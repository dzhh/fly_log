package com.osp.log.model;
/**
 * 日期区间 2017-10-20
 * 
 * @author zhangmingcheng
 */
public class DateInterval {
	private String startDate;
	private String endDate;

	public DateInterval() {
		super();
	}

	public DateInterval(String startDate, String endDate) {
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
}
