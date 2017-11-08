package com.osp.log.model;

import java.io.Serializable;

/**
 * 操作日志 2017-11-07
 * 
 * @author zhangmingcheng
 */
public class LoginLog implements Serializable {

	private static final long serialVersionUID = 1L;
	private String LOG_ID;// 登录日志编号
	private String F_ZGBH;// 用户编号
	private String F_NAME;// 用户名称
	private String F_IP;// 客户端IP
	private String F_XTBH;// 系统编号
	private String F_STIME;// 开始时间
	private String F_ETIME;// 结束时间

	public String getLOG_ID() {
		return LOG_ID;
	}

	public void setLOG_ID(String lOG_ID) {
		LOG_ID = lOG_ID;
	}

	public String getF_ZGBH() {
		return F_ZGBH;
	}

	public void setF_ZGBH(String f_ZGBH) {
		F_ZGBH = f_ZGBH;
	}

	public String getF_IP() {
		return F_IP;
	}

	public void setF_IP(String f_IP) {
		F_IP = f_IP;
	}

	public String getF_XTBH() {
		return F_XTBH;
	}

	public void setF_XTBH(String f_XTBH) {
		F_XTBH = f_XTBH;
	}

	public String getF_STIME() {
		return F_STIME;
	}

	public void setF_STIME(String f_STIME) {
		F_STIME = f_STIME;
	}

	public String getF_ETIME() {
		return F_ETIME;
	}

	public void setF_ETIME(String f_ETIME) {
		F_ETIME = f_ETIME;
	}

	public String getF_NAME() {
		return F_NAME;
	}

	public void setF_NAME(String f_NAME) {
		F_NAME = f_NAME;
	}
}
