package com.osp.log.model;

import java.util.ArrayList;
import java.util.List;

public class SqlModel {

	private String type;

	private String message;

	private String host;

	private String timestamp;

	private String path;

	private int count;

	private int rowId;

	public int getRowId() {
		return rowId;
	}

	public void setRowId(int rowId) {
		this.rowId = rowId;
	}

	private List key = new ArrayList<>();

	private List value = new ArrayList<>();

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

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

	public void addKey(Object k) {
		key.add(k);
	}

	public void addValue(Object v) {
		value.add(v);
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}
}
