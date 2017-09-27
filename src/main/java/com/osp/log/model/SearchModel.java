package com.osp.log.model;

/**
 * 客户端搜索状态
 * 
 * @author zhangmingcheng
 * @time 2017-09-27
 */
public class SearchModel {
	private String message;
	private String timestamp;
	private String clientip;
	private String city;
	private Integer usetime;

	public SearchModel() {
		super();
	}

	public SearchModel(String message, String timestamp, String clientip, String city, Integer usetime) {
		this.message = message;
		this.timestamp = timestamp;
		this.clientip = clientip;
		this.city = city;
		this.usetime = usetime;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getClientip() {
		return clientip;
	}

	public void setClientip(String clientip) {
		this.clientip = clientip;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Integer getUsetime() {
		return usetime;
	}

	public void setUsetime(Integer usetime) {
		this.usetime = usetime;
	}
}
