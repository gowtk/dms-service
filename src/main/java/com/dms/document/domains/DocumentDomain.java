package com.dms.document.domains;

import org.springframework.data.mongodb.core.mapping.Document;

import com.amazonaws.services.textract.model.GetDocumentAnalysisRequest;
import com.amazonaws.services.textract.model.GetDocumentTextDetectionResult;
import com.dms.common.constants.FileStorageType;

@Document(collection = "document_details")
public class DocumentDomain extends BaseDomain {

	private String fileName;
	private Long fileSize;
	private String contentType;
	private String s3Name;
	private String s3Bucket;
	private FileStorageType storage;
	private String md5SumInternal;
	private String md5SumExternal;
	private GetDocumentTextDetectionResult detectingTextResult;
	private GetDocumentAnalysisRequest analyzingTextResult;

	public GetDocumentTextDetectionResult getDetectingTextResult() {
		return detectingTextResult;
	}

	public void setDetectingTextResult(GetDocumentTextDetectionResult detectingTextResult) {
		this.detectingTextResult = detectingTextResult;
	}

	public GetDocumentAnalysisRequest getAnalyzingTextResult() {
		return analyzingTextResult;
	}

	public void setAnalyzingTextResult(GetDocumentAnalysisRequest analyzingTextResult) {
		this.analyzingTextResult = analyzingTextResult;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public FileStorageType getStorage() {
		return storage;
	}

	public void setStorage(FileStorageType storage) {
		this.storage = storage;
	}

	public String getMd5SumInternal() {
		return md5SumInternal;
	}

	public void setMd5SumInternal(String md5SumInternal) {
		this.md5SumInternal = md5SumInternal;
	}

	public String getMd5SumExternal() {
		return md5SumExternal;
	}

	public void setMd5SumExternal(String md5SumExternal) {
		this.md5SumExternal = md5SumExternal;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getS3Name() {
		return s3Name;
	}

	public void setS3Name(String s3Name) {
		this.s3Name = s3Name;
	}

	public String getS3Bucket() {
		return s3Bucket;
	}

	public void setS3Bucket(String s3Bucket) {
		this.s3Bucket = s3Bucket;
	}

	@Override
	public String toString() {
		return "DocumentDomain [fileName=" + fileName + ", fileSize=" + fileSize + ", contentType=" + contentType + ", s3Name=" + s3Name + ", s3Bucket="
				+ s3Bucket + ", storage=" + storage + ", md5SumInternal=" + md5SumInternal + ", md5SumExternal=" + md5SumExternal + "]";
	}

}
