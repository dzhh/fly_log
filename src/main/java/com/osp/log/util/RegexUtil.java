package com.osp.log.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.osp.log.model.DateInterval;

/**
 * 2017-10-20
 * 
 * @author zhangmingcheng
 */
public class RegexUtil {

	/**
	 * 取得日期区间
	 * 
	 * @param startDate
	 *            起始日期 格式20171018(默认：20000101)
	 * @param endDate
	 *            结束日期 格式20171020
	 * @return
	 */
	public static DateInterval getDateInterval(DateInterval dateInterval,String timeFormat) {
		String startDate = dateInterval.getStartDate();
		String endDate = dateInterval.getEndDate();
		// 默认时间区间：20000101到今天
		if (startDate.isEmpty() == true || endDate.isEmpty() == true) {
			dateInterval.setStartDate("20000101");
			dateInterval.setEndDate(DateUtil.getCurrentDate());
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
			try {
				Date dBegin = sdf.parse(dateInterval.getStartDate());
				Date dEnd = sdf.parse(dateInterval.getEndDate());
				List<Date> lDate = findDates(dBegin, dEnd);
				// 结束时间在开始时间之前
				if (lDate.size() <= 1) {
					dateInterval.setEndDate(startDate);
				}
			} catch (Exception e) {
				System.err.println("时间格式转换异常!");
			}
		}
		return dateInterval;
	}

	public static void main(String[] args) throws Exception {
		DateInterval aa = RegexUtil.getDateInterval(new DateInterval("", "20120202"),"ss");
		System.out.println("结果===" + aa.getStartDate() + "  " + aa.getEndDate());
		/*
		 * ESConfig.ES_CLUSTERNAME = "osp"; ESConfig.ES_IP = "10.75.8.167";
		 * ESConfig.ES_PORT = 9300; Settings settings =
		 * Settings.builder().put("cluster.name", "osp")
		 * .put("client.transport.sniff", true).build(); try { TransportClient
		 * client = new PreBuiltTransportClient(settings).addTransportAddress(
		 * new InetSocketTransportAddress(InetAddress.getByName(ESConfig.ES_IP),
		 * ESConfig.ES_PORT)); } catch (UnknownHostException e) {
		 * e.printStackTrace(); }
		 * 
		 * 
		 * //时间范围的设定 RangeQueryBuilder rangequerybuilder = QueryBuilders
		 * .rangeQuery("@timestamp").format("yyyyMMdd")
		 * .from("20170921").to("20170920"); SearchSourceBuilder sourceBuilder =
		 * new SearchSourceBuilder(); sourceBuilder.query(rangequerybuilder);
		 * 
		 * //System.out.println(sourceBuilder.toString());
		 * 
		 * //查询建立 SearchRequestBuilder responsebuilder = ESUtil.getClient()
		 * .prepareSearch("logstash-lcfwzx-*") .setTypes("logs"); SearchResponse
		 * myresponse=responsebuilder .setQuery(rangequerybuilder)
		 * .addSort("@timestamp", SortOrder.ASC) .setFrom(0).setSize(50) //分页
		 * .setExplain(true) .execute() .actionGet(); SearchHits hits =
		 * myresponse.getHits(); for(int i = 0; i < hits.getHits().length; i++)
		 * { System.out.println(hits.getHits()[i].getSourceAsString()); }
		 */

	}

	/**
	 * 获取某时间段所有日期
	 * 
	 * @param dBegin
	 * @param dEnd
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Date> findDates(Date dBegin, Date dEnd) {
		@SuppressWarnings("rawtypes")
		List lDate = new ArrayList();
		lDate.add(dBegin);
		Calendar calBegin = Calendar.getInstance();
		// 使用给定的 Date 设置此 Calendar 的时间
		calBegin.setTime(dBegin);
		Calendar calEnd = Calendar.getInstance();
		// 使用给定的 Date 设置此 Calendar 的时间
		calEnd.setTime(dEnd);
		// 测试此日期是否在指定日期之后
		while (dEnd.after(calBegin.getTime())) {
			// 根据日历的规则，为给定的日历字段添加或减去指定的时间量
			calBegin.add(Calendar.DAY_OF_MONTH, 1);
			lDate.add(calBegin.getTime());
		}
		return lDate;
	}

	/**
	 * * 判断是否为合法IP * @return the ip
	 */
	public static boolean isboolIp(String ipAddress) {
		String ip = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
		Pattern pattern = Pattern.compile(ip);
		Matcher matcher = pattern.matcher(ipAddress);
		return matcher.matches();
	}

}
