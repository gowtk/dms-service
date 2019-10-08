package com.dms.document.services;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.policy.Condition;
import com.amazonaws.auth.policy.Policy;
import com.amazonaws.auth.policy.Principal;
import com.amazonaws.auth.policy.Resource;
import com.amazonaws.auth.policy.Statement;
import com.amazonaws.auth.policy.Statement.Effect;
import com.amazonaws.auth.policy.actions.SQSActions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.QueueAttributeName;
import com.amazonaws.services.sqs.model.SetQueueAttributesRequest;
import com.amazonaws.services.textract.AmazonTextract;
import com.amazonaws.services.textract.model.Block;
import com.amazonaws.services.textract.model.DocumentLocation;
import com.amazonaws.services.textract.model.DocumentMetadata;
import com.amazonaws.services.textract.model.GetDocumentTextDetectionRequest;
import com.amazonaws.services.textract.model.GetDocumentTextDetectionResult;
import com.amazonaws.services.textract.model.NotificationChannel;
import com.amazonaws.services.textract.model.Relationship;
import com.amazonaws.services.textract.model.S3Object;
import com.amazonaws.services.textract.model.StartDocumentTextDetectionRequest;
import com.amazonaws.services.textract.model.StartDocumentTextDetectionResult;
import com.dms.document.domains.DocumentDomain;
import com.dms.document.repositories.DocumentRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AwsTextractService {

	@Autowired
	private AmazonAuthService authService;

	@Autowired
	private DocumentRepository docsRepo;

	private static final Logger logger = LoggerFactory.getLogger(AwsTextractService.class);

	public void parseDocument(String user, String documentId) throws IOException, InterruptedException {
		DocumentDomain document = docsRepo.findById(documentId);

		String s3Name = document.getS3Name();
		String s3Bucket = document.getS3Bucket();

		AmazonTextract textract = authService.initiateTextractClient();
		AmazonSNS sns = authService.initiateSNSClient();
		AmazonSQS sqs = authService.initiateSQSClient();

		String roleArn = null;

		// create a new SNS topic
		String snsTopicName = "AmazonTextractTopic" + Long.toString(System.currentTimeMillis());
		CreateTopicRequest createTopicRequest = new CreateTopicRequest(snsTopicName);
		CreateTopicResult createTopicResult = sns.createTopic(createTopicRequest);
		String snsTopicArn = createTopicResult.getTopicArn();

		// Create a new SQS Queue
		String sqsQueueName = "AmazonTextractQueue" + Long.toString(System.currentTimeMillis());
		final CreateQueueRequest createQueueRequest = new CreateQueueRequest(sqsQueueName);
		String sqsQueueUrl = sqs.createQueue(createQueueRequest).getQueueUrl();
		String sqsQueueArn = sqs.getQueueAttributes(sqsQueueUrl, Arrays.asList("QueueArn")).getAttributes().get("QueueArn");

		// Subscribe SQS queue to SNS topic
		String sqsSubscriptionArn = sns.subscribe(snsTopicArn, "sqs", sqsQueueArn).getSubscriptionArn();

		// Authorize queue
		Statement statements = new Statement(Effect.Allow).withPrincipals(Principal.AllUsers).withActions(SQSActions.SendMessage)
				.withResources(new Resource(sqsQueueArn))
				.withConditions(new Condition().withType("ArnEquals").withConditionKey("aws:SourceArn").withValues(snsTopicArn));
		Policy policy = new Policy().withStatements(statements);

		Map<String, String> queueAttributes = new HashMap<>();
		queueAttributes.put(QueueAttributeName.Policy.toString(), policy.toJson());
		sqs.setQueueAttributes(new SetQueueAttributesRequest(sqsQueueUrl, queueAttributes));

		System.out.println("Topic arn: " + snsTopicArn);
		System.out.println("Queue arn: " + sqsQueueArn);
		System.out.println("Queue url: " + sqsQueueUrl);
		System.out.println("Queue sub arn: " + sqsSubscriptionArn);

		// NotificationChannel channel = new NotificationChannel().withSNSTopicArn(snsTopicArn).withRoleArn(roleArn);
		NotificationChannel channel = new NotificationChannel().withSNSTopicArn(snsTopicArn);

		DocumentLocation s3Document = new DocumentLocation().withS3Object(new S3Object().withBucket(s3Bucket).withName(s3Name));

		StartDocumentTextDetectionRequest detectingText = new StartDocumentTextDetectionRequest().withDocumentLocation(s3Document).withJobTag("DetectingText")
				.withNotificationChannel(channel);
		StartDocumentTextDetectionResult detectingTextResult = textract.startDocumentTextDetection(detectingText);
		String detectingTextJobId = detectingTextResult.getJobId();

		// StartDocumentAnalysisRequest analyzingText = new StartDocumentAnalysisRequest().withFeatureTypes("TABLES", "FORMS").withDocumentLocation(s3Document)
		// .withJobTag("AnalyzingText").withNotificationChannel(channel);
		// StartDocumentAnalysisResult analyzingTextResult = textract.startDocumentAnalysis(analyzingText);
		// String analyzingTextJobId = analyzingTextResult.getJobId();

		// Poll queue for messages
		List<Message> messages = null;
		int dotLine = 0;
		boolean jobFound = false;

		// loop until the job status is published. Ignore other messages in queue.
		do {
			messages = sqs.receiveMessage(sqsQueueUrl).getMessages();
			if (dotLine++ < 40) {
				System.out.print(".");
			} else {
				System.out.println();
				dotLine = 0;
			}

			if (!messages.isEmpty()) {
				// Loop through messages received.
				for (Message message : messages) {
					String notification = message.getBody();

					// Get status and job id from notification.
					ObjectMapper mapper = new ObjectMapper();
					JsonNode jsonMessageTree = mapper.readTree(notification);
					JsonNode messageBodyText = jsonMessageTree.get("Message");
					ObjectMapper operationResultMapper = new ObjectMapper();
					JsonNode jsonResultTree = operationResultMapper.readTree(messageBodyText.textValue());
					JsonNode operationJobId = jsonResultTree.get("JobId");
					JsonNode operationStatus = jsonResultTree.get("Status");
					System.out.println("Job found was " + operationJobId);
					// Found job. Get the results and display.
					if (operationJobId.asText().equals(detectingTextJobId)) {
						jobFound = true;
						System.out.println("Job id: " + operationJobId);
						System.out.println("Status : " + operationStatus.toString());
						if (operationStatus.asText().equals("SUCCEEDED")) {
							documentTextDetectionResults(document, textract, detectingTextJobId);
						} else {
							System.out.println("Video analysis failed");
						}
						sqs.deleteMessage(sqsQueueUrl, message.getReceiptHandle());
					} else {
						System.out.println("Job received was not job " + detectingTextJobId);
						// Delete unknown message. Consider moving message to dead letter queue
						sqs.deleteMessage(sqsQueueUrl, message.getReceiptHandle());
					}
				}
			} else {
				Thread.sleep(5000);
			}
		} while (!jobFound);

		System.out.println("Finished processing document");

	}

	private void documentTextDetectionResults(DocumentDomain document, AmazonTextract textract, String detectingTextJobId) {
		GetDocumentTextDetectionRequest documentTextDetectionRequest = new GetDocumentTextDetectionRequest().withJobId(detectingTextJobId);
		GetDocumentTextDetectionResult detectingTextResult = textract.getDocumentTextDetection(documentTextDetectionRequest);
		document.setDetectingTextResult(detectingTextResult);
		docsRepo.updateTextractDetails(document);
		DocumentMetadata documentMetaData = detectingTextResult.getDocumentMetadata();
		System.out.println("Pages: " + documentMetaData.getPages().toString());
		List<Block> blocks = detectingTextResult.getBlocks();
		for (Block block : blocks) {
			displayBlockInfo(block);
		}

	}

	// Displays Block information for text detection and text analysis
	private void displayBlockInfo(Block block) {
		System.out.println("Block Id : " + block.getId());
		if (block.getText() != null)
			System.out.println("\tDetected text: " + block.getText());
		System.out.println("\tType: " + block.getBlockType());

		if (block.getBlockType().equals("PAGE") != true) {
			System.out.println("\tConfidence: " + block.getConfidence().toString());
		}
		if (block.getBlockType().equals("CELL")) {
			System.out.println("\tCell information:");
			System.out.println("\t\tColumn: " + block.getColumnIndex());
			System.out.println("\t\tRow: " + block.getRowIndex());
			System.out.println("\t\tColumn span: " + block.getColumnSpan());
			System.out.println("\t\tRow span: " + block.getRowSpan());

		}

		System.out.println("\tRelationships");
		List<Relationship> relationships = block.getRelationships();
		if (relationships != null) {
			for (Relationship relationship : relationships) {
				System.out.println("\t\tType: " + relationship.getType());
				System.out.println("\t\tIDs: " + relationship.getIds().toString());
			}
		} else {
			System.out.println("\t\tNo related Blocks");
		}

		System.out.println("\tGeometry");
		System.out.println("\t\tBounding Box: " + block.getGeometry().getBoundingBox().toString());
		System.out.println("\t\tPolygon: " + block.getGeometry().getPolygon().toString());

		List<String> entityTypes = block.getEntityTypes();

		System.out.println("\tEntity Types");
		if (entityTypes != null) {
			for (String entityType : entityTypes) {
				System.out.println("\t\tEntity Type: " + entityType);
			}
		} else {
			System.out.println("\t\tNo entity type");
		}

		if (block.getBlockType().equals("SELECTION_ELEMENT")) {
			System.out.print("    Selection element detected: ");
			if (block.getSelectionStatus().equals("SELECTED")) {
				System.out.println("Selected");
			} else {
				System.out.println(" Not selected");
			}
		}
		if (block.getPage() != null)
			System.out.println("\tPage: " + block.getPage());
		System.out.println();
	}

}
