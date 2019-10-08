package com.dms.document.views;

import com.dms.web.beans.ListView;

public class UserDetailsView {

	private String username;
	private ListView<FileListView> filesList;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public ListView<FileListView> getFilesList() {
		return filesList;
	}

	public void setFilesList(ListView<FileListView> filesList) {
		this.filesList = filesList;
	}

}
