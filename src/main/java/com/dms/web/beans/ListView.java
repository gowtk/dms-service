package com.dms.web.beans;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.data.domain.Pageable;

public class ListView<T> {

	private List<T> contents;
	private LinkedHashMap<String, String> columns;
	private List<String> sort;
	private long totalElements;
	private int page;
	private int size;
	private int totalPages;
	private String search;

	public ListView() {
		super();
	}

	public ListView(List<T> contents, long totalElements, Pageable pageable, LinkedHashMap<String, String> columns, List<String> sort) {
		super();
		this.contents = contents;
		this.totalElements = totalElements;
		if (null != pageable) {
			this.page = pageable.getPageNumber() + 1;
			this.size = pageable.getPageSize();
			this.totalPages = pageable.getPageSize() == 0 ? 1 : (int) Math.ceil(totalElements / (double) pageable.getPageSize());
		} else {
			this.page = -1;
			this.size = -1;
			this.totalPages = -1;
		}
		this.columns = columns;
		this.sort = sort;
	}

	public List<T> getContents() {
		return contents;
	}

	public void setContents(List<T> contents) {
		this.contents = contents;
	}

	public LinkedHashMap<String, String> getColumns() {
		return columns;
	}

	public void setColumns(LinkedHashMap<String, String> columns) {
		this.columns = columns;
	}

	public List<String> getSort() {
		return sort;
	}

	public void setSort(List<String> sort) {
		this.sort = sort;
	}

	public long getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(long totalElements) {
		this.totalElements = totalElements;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

}
