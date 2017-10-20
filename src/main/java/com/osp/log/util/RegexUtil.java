package com.osp.log.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import com.osp.log.config.ESConfig;

/**
 * 2017-10-20
 * 
 * @author zhangmingcheng
 */
public class RegexUtil {

	/**
	 * 判断结束时间是否在开始时间之前
	 * @param startDate
	 *            起始日期 格式20171018
	 * @param endDate
	 *            结束日期 格式20171020
	 * @return
	 */
	public static Boolean judgeDateInterval(String startDate, String endDate) {
		Boolean flag = false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		try{
		Date dBegin = sdf.parse(startDate);
		Date dEnd = sdf.parse(endDate);
		List<Date> lDate = findDates(dBegin, dEnd);
		if(lDate.size()>1){
			flag = true;
		}
		}catch(Exception e){
			System.err.println("时间格式转换异常!");
		}
		
		return flag;
	}

	public static void main(String[] args) throws Exception {
		System.out.println("结果==="+RegexUtil.judgeDateInterval("20120202", "20120203"));
		/*ESConfig.ES_CLUSTERNAME = "osp";
		ESConfig.ES_IP = "10.75.8.167";
		ESConfig.ES_PORT = 9300;
		Settings settings = Settings.builder().put("cluster.name", "osp")
				.put("client.transport.sniff", true).build();
		try {
			TransportClient client = new PreBuiltTransportClient(settings).addTransportAddress(
					new InetSocketTransportAddress(InetAddress.getByName(ESConfig.ES_IP), ESConfig.ES_PORT));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		
		        //时间范围的设定
		        RangeQueryBuilder rangequerybuilder = QueryBuilders
		                    .rangeQuery("@timestamp").format("yyyyMMdd")
		                    .from("20170921").to("20170920");
		        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		        sourceBuilder.query(rangequerybuilder);

		        //System.out.println(sourceBuilder.toString());

		        //查询建立
		        SearchRequestBuilder responsebuilder = ESUtil.getClient()
		                                .prepareSearch("logstash-lcfwzx-*")
		                                .setTypes("logs");
		        SearchResponse myresponse=responsebuilder
		                    .setQuery(rangequerybuilder)
		                    .addSort("@timestamp", SortOrder.ASC)
		                    .setFrom(0).setSize(50) //分页
		                    .setExplain(true)
		                    .execute()
		                    .actionGet();
		        SearchHits hits = myresponse.getHits();
		        for(int i = 0; i < hits.getHits().length; i++) {
		            System.out.println(hits.getHits()[i].getSourceAsString());
		        }*/
		    
	}

	/**
	 * 获取某时间段所有日期
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
