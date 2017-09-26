package com.osp.log.model;

import java.util.LinkedList;

/**
 * @author zhangmingcheng 2017-09-26
 */
public class SearchResultBean {
	private String usetime;
	private long total;
	private int page;
	private int pagesize;
	private LinkedList<TomcatModel> newsList;
	
	public SearchResultBean() {
		super();
	}

	public SearchResultBean(String usetime, long total, int page, int pagesize, LinkedList<TomcatModel> newsList) {
		super();
		this.usetime = usetime;
		this.total = total;
		this.page = page;
		this.pagesize = pagesize;
		this.newsList = newsList;
	}

	public String getUsetime() {
		return usetime;
	}

	public void setUsetime(String usetime) {
		this.usetime = usetime;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPagesize() {
		return pagesize;
	}

	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}

	public LinkedList<TomcatModel> getNewsList() {
		return newsList;
	}

	public void setNewsList(LinkedList<TomcatModel> newsList) {
		this.newsList = newsList;
	}

}
