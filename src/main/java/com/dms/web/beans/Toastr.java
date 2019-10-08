package com.dms.web.beans;

public class Toastr {

	private Toast type;
	private String title;
	private String message;

	public Toastr() {
		super();
	}

	public Toastr(Toast type, String title, String message) {
		super();
		this.type = type;
		this.title = title;
		this.message = message;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Toast getType() {
		return type;
	}

	public void setType(Toast type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
