package com.dms;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.textract.AmazonTextract;
import com.amazonaws.services.textract.AmazonTextractClientBuilder;

@Component
@ConfigurationProperties(prefix = "aws-properties")
public class AwsAuth {

	private String accessKeyId;

	private String secretAccessKey;

	public String getAccessKeyId() {
		return accessKeyId;
	}

	public void setAccessKeyId(String accessKeyId) {
		this.accessKeyId = accessKeyId;
	}

	public String getSecretAccessKey() {
		return secretAccessKey;
	}

	public void setSecretAccessKey(String secretAccessKey) {
		this.secretAccessKey = secretAccessKey;
	}

	public AmazonTextract initiateTextractClient() {
		EndpointConfiguration endpoint = new EndpointConfiguration("https://textract.us-east-1.amazonaws.com", "us-east-1");
		AmazonTextractClientBuilder builder = AmazonTextractClientBuilder.standard();
		builder.withEndpointConfiguration(endpoint);
		BasicAWSCredentials basicCredentials = new BasicAWSCredentials(accessKeyId, secretAccessKey);
		builder.withCredentials(new AWSStaticCredentialsProvider(basicCredentials));
		return builder.build();
	}

	public void closeTextractClient(AmazonTextract textract) {
		textract.shutdown();
	}

}