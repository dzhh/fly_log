package com.osp.log.util;

import java.text.SimpleDateFormat;

/**
 * 时间工具类
 * @author zhangmingcheng
 * @time 2017-09-27
 */
public class TimeUtils {
	/**
	 * 获取当前时间:格式yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String getCurrentTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.util.Date date = new java.util.Date();
		return sdf.format(date);
	}
}
