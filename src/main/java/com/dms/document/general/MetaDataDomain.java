package com.dms.document.general;

import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "general_metadata")
public class MetaDataDomain {

	@Id
	private String id;
	private String constant;
	private Map<String, Object> objectMap;

	@Override
	public String toString() {
		return "MetaData [id=" + id + "]";
	}

	public String getId() {
		return id;
	}

	public String getConstant() {
		return constant;
	}

	public Map<String, Object> getObjectMap() {
		return objectMap;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setConstant(String constant) {
		this.constant = constant;
	}

	public void setObjectMap(Map<String, Object> objectMap) {
		this.objectMap = objectMap;
	}

}
