package com.dms.document.views;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.dms.common.constants.ViewConstants;
import com.dms.document.domains.DocumentDomain;

public class FileListView {

	private String id;
	private String fileName;
	private String md5Sum;

	// statics
	public static LinkedHashMap<String, String> uiColumns;
	public static List<String> uiSort;
	static {
		uiColumns = new LinkedHashMap<>(2);
		uiColumns.put(ViewConstants.DOCUMENT_FILENAME, "File Name");
		uiColumns.put(ViewConstants.DOCUMENT_MD5SUM, "MD5SUM");

		uiSort = new ArrayList<>(2);
		uiSort.add(ViewConstants.DOCUMENT_FILENAME);
		uiSort.add(ViewConstants.DOCUMENT_MD5SUM);
	}

	public FileListView() {
		super();
	}

	public FileListView(DocumentDomain document) {
		this.id = document.getId();
		this.fileName = document.getFileName();
		this.md5Sum = document.getMd5SumInternal();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getMd5Sum() {
		return md5Sum;
	}

	public void setMd5Sum(String md5Sum) {
		this.md5Sum = md5Sum;
	}

}
