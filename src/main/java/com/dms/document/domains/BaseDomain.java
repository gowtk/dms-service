package com.dms.document.domains;

import java.util.Date;
import java.util.Set;

import org.springframework.data.annotation.Id;

public class BaseDomain {

	@Id
	private String id;
	private String createdBy;
	private String createdRole;
	private Date createdTime;
	private String updatedBy;
	private String updatedRole;
	private Date updatedTime;
	protected Set<String> keywords;

	public String getCreatedRole() {
		return createdRole;
	}

	public void setCreatedRole(String createdRole) {
		this.createdRole = createdRole;
	}

	public String getUpdatedRole() {
		return updatedRole;
	}

	public void setUpdatedRole(String updatedRole) {
		this.updatedRole = updatedRole;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public Set<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(Set<String> keywords) {
		this.keywords = keywords;
	}

}
