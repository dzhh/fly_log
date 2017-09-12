package com.osp.log.model;

import java.util.ArrayList;
import java.util.List;

public class TomcatModel {

	private String date;
	
	private int count;
	
	public List getKey() {
		return key;
	}

	public void setKey(List key) {
		this.key = key;
	}

	public List getValue() {
		return value;
	}

	public void setValue(List value) {
		this.value = value;
	}

	private List key = new ArrayList<>();
	
	private List value = new ArrayList<>();;
	
	public void addKey(Object k) {
		key.add(k);
	}
	
	public void addValue(Object v) {
		value.add(v);
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
