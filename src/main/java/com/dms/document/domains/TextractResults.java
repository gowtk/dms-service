package com.dms.document.domains;

import java.util.List;

import com.amazonaws.services.textract.model.Block;

public class TextractResults {

	private List<Block> blocks;
	private Integer pageNumber;

	public TextractResults() {
		super();
	}

	public TextractResults(List<Block> blocks, Integer pageNumber) {
		super();
		this.blocks = blocks;
		this.pageNumber = pageNumber;
	}

	public List<Block> getBlocks() {
		return blocks;
	}

	public void setBlocks(List<Block> blocks) {
		this.blocks = blocks;
	}

	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

}
