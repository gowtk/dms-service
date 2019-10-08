package com.dms.common.constants;

public enum FileStorageType {

	GRIDFS("grid_fs"), AWSS3("aws_s3");

	private String dbName;

	private FileStorageType(String dbName) {
		this.dbName = dbName;
	}

	public String getDbName() {
		return dbName;
	}

}
