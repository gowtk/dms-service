package com.dms.web.beans;

import javax.validation.constraints.NotNull;

public class Response<T> {

	private String status;
	private String message;
	private Toastr toast;
	private T data;

	public Response() {
	}

	// Retrive data
	public Response(T data) {
		this.status = Status.success.toString();
		this.data = data;
	}

	// Update data with toast message
	public Response(Toastr toast, T data) {
		super();
		this.status = Status.success.toString();
		this.toast = toast;
		this.data = data;
	}

	// Fail or Error with message
	public Response(@NotNull Status status, String message) {
		this.status = status.toString();
		this.message = message;
	}

	// All
	public Response(String status, String message, Toastr toast, T data) {
		super();
		this.status = status;
		this.message = message;
		this.toast = toast;
		this.data = data;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Toastr getToast() {
		return toast;
	}

	public void setToast(Toastr toast) {
		this.toast = toast;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}
