package com.osp.log.model;

public class Page {
	
	//这个值作者会直接返回给前台
	private int draw;
	
	//从多少开始
	private int start;
	
	//数据长度
	private int length;
	
	//表的总记录数 必要
	private int recordsTotal;
	
	//条件过滤后记录数 必要
	private int recordsFiltered;

	public int getDraw() {
		return draw;
	}

	public void setDraw(int draw) {
		this.draw = draw;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getRecordsTotal() {
		return recordsTotal;
	}

	public void setRecordsTotal(int recordsTotal) {
		this.recordsTotal = recordsTotal;
	}

	public int getRecordsFiltered() {
		return recordsFiltered;
	}

	public void setRecordsFiltered(int recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}
}
