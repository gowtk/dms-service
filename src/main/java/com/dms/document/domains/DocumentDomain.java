package com.dms.document.domains;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.dms.common.constants.FileStorageType;

@Document(collection = "document_details")
public class DocumentDomain extends BaseDomain {

	private String fileId;
	private String fileName;
	private Long fileSize;
	private String contentType;
	private FileStorageType storage;
	private String md5;
	private Integer numberOfPages;
	private List<TextractResults> textractResults;

	public Integer getNumberOfPages() {
		return numberOfPages;
	}

	public void setNumberOfPages(Integer numberOfPages) {
		this.numberOfPages = numberOfPages;
	}

	public List<TextractResults> getTextractResults() {
		return textractResults;
	}

	public void setTextractResults(List<TextractResults> textractResults) {
		this.textractResults = textractResults;
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

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public FileStorageType getStorage() {
		return storage;
	}

	public void setStorage(FileStorageType storage) {
		this.storage = storage;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

}
