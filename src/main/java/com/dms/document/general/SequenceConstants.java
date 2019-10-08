package com.dms.document.general;

public enum SequenceConstants {

	DOCUMENT_ID("DOC", 10);

	private String value;
	private Integer digitLimit;

	private SequenceConstants(String value, Integer digitLimit) {
		this.value = value;
		this.digitLimit = digitLimit;
	}

	public String getValue() {
		return value;
	}

	public Integer getDigitLimit() {
		return digitLimit;
	}

}
