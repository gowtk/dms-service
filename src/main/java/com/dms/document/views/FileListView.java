package com.dms.document.views;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.dms.common.constants.ViewConstants;
import com.dms.document.domains.DocumentDomain;

public class FileListView {

	private String id;
	private String fileName;
	private String md5;

	// statics
	public static LinkedHashMap<String, String> uiColumns;
	public static List<String> uiSort;
	static {
		uiColumns = new LinkedHashMap<>(2);
		uiColumns.put(ViewConstants.DOCUMENT_FILENAME, "File Name");
		uiColumns.put(ViewConstants.DOCUMENT_MD5, "MD5");

		uiSort = new ArrayList<>(2);
		uiSort.add(ViewConstants.DOCUMENT_FILENAME);
		uiSort.add(ViewConstants.DOCUMENT_MD5);
	}

	public FileListView() {
		super();
	}

	public FileListView(DocumentDomain document) {
		this.id = document.getId();
		this.fileName = document.getFileName();
		this.md5 = document.getMd5();
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

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

}
