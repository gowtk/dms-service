package com.dms.document.services;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.dms.common.utils.StringUtils;
import com.dms.document.domains.DocumentDomain;
import com.dms.document.repositories.DocumentRepository;

@Service
public class AwsS3Service {

	@Autowired
	private AmazonAuthService authService;

	@Autowired
	private DocumentRepository docsRepo;

	private static final Logger logger = LoggerFactory.getLogger(AwsS3Service.class);

	public void uploadFile(DocumentDomain document, MultipartFile file) throws IOException {

		AmazonS3 s3client = authService.initiateS3Clinet();
		String s3Bucket = authService.s3BucketName();
		String s3Name = document.getId() + StringUtils.DOT + StringUtils.fileExtension(document.getFileName());

		byte[] contents = file.getBytes();
		InputStream stream = new ByteArrayInputStream(contents);
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType(file.getContentType());
		PutObjectRequest request = new PutObjectRequest(s3Bucket, s3Name, stream, metadata).withCannedAcl(CannedAccessControlList.Private);
		PutObjectResult result = s3client.putObject(request);
		authService.closeS3Client(s3client);

		document.setS3Name(s3Name);
		document.setS3Bucket(s3Bucket);
		document.setMd5SumExternal(result.getContentMd5());
		docsRepo.updateS3Details(document);
		logger.info("S3 details saved: {}", document);
	}

	public BufferedImage downloadFile(String documentId) throws IOException {
		AmazonS3 s3client = authService.initiateS3Clinet();
		S3Object s3object = s3client.getObject(authService.s3BucketName(), documentId);
		S3ObjectInputStream inputStream = s3object.getObjectContent();
		return ImageIO.read(inputStream);
	}

	private void validateBucketName(AmazonS3 s3client) {
		if (s3client.doesBucketExistV2(authService.s3BucketName())) {
			List<String> buckets = s3client.listBuckets().stream().map(Bucket::getName).collect(Collectors.toList());
			logger.info("{}: Bucket name is not available. " + "Available :{}", authService.s3BucketName(), buckets);
			return;
		}
	}

}
