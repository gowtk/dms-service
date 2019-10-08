package com.dms.document.services;

import org.springframework.stereotype.Service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.textract.AmazonTextract;
import com.amazonaws.services.textract.AmazonTextractClientBuilder;

@Service
public class AmazonAuthService {

	private static String accessKeyId = "AKIAJTIAMU6CQDS6ZNSA";
	private static String secretAccessKey = "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";

	// S3 Storage
	private static String s3Region = "us-east-1";
	private static String bucketName = "document-mgmt-s3";
	private static String cdnDomain = "";

	// Textract
	private static String textractRegion = "us-east-1";
	private static String serviceEndpoint = "https://textract.us-east-1.amazonaws.com";

	// SQS - Simple Queue Service
	private static String sqsRegion = "us-east-1";

	// SNS - Simple Notification Service
	private static String snsRegion = "us-east-1";

	public String s3BucketName() {
		return bucketName;
	}

	public AmazonS3 initiateS3Clinet() {
		AmazonS3ClientBuilder builder = AmazonS3ClientBuilder.standard();
		builder.setRegion(s3Region);
		builder.withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKeyId, secretAccessKey)));
		return builder.build();
	}

	public AmazonTextract initiateTextractClient() {
		EndpointConfiguration endpoint = new EndpointConfiguration(serviceEndpoint, textractRegion);
		AmazonTextractClientBuilder builder = AmazonTextractClientBuilder.standard();
		builder.withEndpointConfiguration(endpoint);
		builder.withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKeyId, secretAccessKey)));
		return builder.build();
	}

	public AmazonSQS initiateSQSClient() {
		AmazonSQSClientBuilder builder = AmazonSQSClientBuilder.standard();
		builder.setRegion(sqsRegion);
		builder.withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKeyId, secretAccessKey)));
		return builder.build();
	}

	public AmazonSNS initiateSNSClient() {
		AmazonSNSClientBuilder builder = AmazonSNSClientBuilder.standard();
		builder.setRegion(snsRegion);
		builder.withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKeyId, secretAccessKey)));
		return builder.build();
	}

	public void closeS3Client(AmazonS3 s3client) {
		s3client.shutdown();
	}

	public void closeTextractClient(AmazonTextract s3client) {
		s3client.shutdown();
	}

	public void closeSQSClient(AmazonSQS s3client) {
		s3client.shutdown();
	}

	public void closeSNSClient(AmazonSNS s3client) {
		s3client.shutdown();
	}

}
