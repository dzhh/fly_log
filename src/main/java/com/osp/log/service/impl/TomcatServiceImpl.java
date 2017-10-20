package com.osp.log.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.IndexNotFoundException;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.ExtendedBounds;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram.Bucket;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.joda.time.DateTimeZone;
import org.springframework.stereotype.Service;

import com.osp.log.model.Page;
import com.osp.log.model.TomcatModel;
import com.osp.log.service.BaseESService;
import com.osp.log.service.TomcatService;
import com.osp.log.util.DateUtil;
import com.osp.log.util.ESUtil;
import com.osp.log.util.RegexUtil;

@Service
public class TomcatServiceImpl extends BaseESService<TomcatModel> implements TomcatService {

	public static final String INDEX_NAME = "logstash-apacheaccesslog*"; // 默认索引名称
	public static final String INDEX_TYPE = "logs"; // 索引类型

	public TomcatServiceImpl() {
		super();
	}

	@Override
	public TomcatModel tomcatTimeSearch(int day, String index) {
		if (index.isEmpty() == true) {
			return null;
		}
		// 查询索引
		TransportClient client = this.getClient();
		TomcatModel tomcat = new TomcatModel();
		if (index.isEmpty() == true) {
			return null;
		}
		try {
			SearchRequestBuilder srb = client.prepareSearch(index).setTypes(getIndexType());
			// 组装分组
			DateHistogramAggregationBuilder dateAgg = AggregationBuilders.dateHistogram("dateagg");
			// 定义分组的日期字段
			dateAgg.field("@timestamp");
			ExtendedBounds extendedBounds = new ExtendedBounds(DateUtil.getPastDate(day), DateUtil.getDate());
			dateAgg.extendedBounds(extendedBounds);
			dateAgg.dateHistogramInterval(DateHistogramInterval.DAY);
			DateTimeZone timeZone = DateTimeZone.forID("Asia/Shanghai");
			dateAgg.timeZone(timeZone);
			dateAgg.format("yyyy-MM-dd");

			RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("@timestamp").from(DateUtil.getPastDate(day))
					.to(DateUtil.getDate()).includeLower(true) // 包括下界
					.includeUpper(true); // 包括上界

			// 组装请求
			srb.setQuery(rangeQueryBuilder).addAggregation(dateAgg);

			// 查询
			SearchResponse response = srb.execute().actionGet();
			// 获取一级聚合数据
			Histogram h = response.getAggregations().get("dateagg");
			// 得到一级聚合结果里面的分桶集合
			List<Bucket> buckets = (List<Bucket>) h.getBuckets();

			// 遍历分桶集
			for (Bucket b : buckets) {
				org.joda.time.DateTime key = (org.joda.time.DateTime) b.getKey();
				tomcat.addKey(key.getMonthOfYear() + "-" + key.getDayOfMonth());
				tomcat.addValue((int) b.getDocCount());
			}
		} catch (IndexNotFoundException e) {
			System.err.println("此索引不存在!");
		}
		return tomcat;
	}

	@Override
	public TomcatModel tomcatRequest(String index) {
		if (index.isEmpty() == true) {
			return null;
		}
		// 查询索引
		TransportClient client = getClient();
		TomcatModel tomcat = new TomcatModel();
		try {
			MatchQueryBuilder queryBuilderGet = QueryBuilders.matchQuery("verb", "GET");
			// GET POST ..
			SearchResponse responseGet = client.prepareSearch(index).setTypes(getIndexType()).setQuery(queryBuilderGet)
					.execute().actionGet();
			SearchHits myhitsGet = responseGet.getHits();
			tomcat.addKey("GET");
			tomcat.addValue(myhitsGet.getTotalHits());

			MatchQueryBuilder queryBuilderPost = QueryBuilders.matchQuery("verb", "POST");
			SearchResponse responsePost = client.prepareSearch(index).setTypes(getIndexType())
					.setQuery(queryBuilderPost).execute().actionGet();
			SearchHits myhitsPost = responsePost.getHits();
			tomcat.addKey("POST");
			tomcat.addValue(myhitsPost.getTotalHits());

			MatchQueryBuilder queryBuilderPut = QueryBuilders.matchQuery("verb", "PUT");
			SearchResponse responsePut = client.prepareSearch(index).setTypes(getIndexType()).setQuery(queryBuilderPut)
					.execute().actionGet();
			SearchHits myhitsPut = responsePut.getHits();
			tomcat.addKey("PUT");
			tomcat.addValue(myhitsPut.getTotalHits());
		} catch (IndexNotFoundException e) {
			System.err.println("此索引不存在!");
		}
		return tomcat;
	}

	/**
	 * 客户端访问次数统计-前10
	 */
	@Override
	public List<TomcatModel> clientRequestCount(Page page, String index) {
		if (index.isEmpty() == true) {
			return null;
		}
		List<TomcatModel> list = new ArrayList<TomcatModel>();
		TransportClient client = this.getClient();
		try {
			TermsAggregationBuilder aggregation = AggregationBuilders.terms("cilentip_count").field("clientip");
			SearchResponse response = client.prepareSearch(index).setTypes(getIndexType()).addAggregation(aggregation)
					.execute().actionGet();
			Terms terms = response.getAggregations().get("cilentip_count");
			List<Terms.Bucket> buckets = (List<Terms.Bucket>) terms.getBuckets();
			page.setRecordsFiltered((int) buckets.size());
			page.setRecordsTotal((int) buckets.size());
			int i = 1;
			for (Terms.Bucket bucket : buckets) {
				TomcatModel tomcat = new TomcatModel();
				if (RegexUtil.isboolIp((String) bucket.getKey()) == true) {// 过滤掉不规则的ipv4
					tomcat.setClientip((String) bucket.getKey());
					tomcat.setCount((int) bucket.getDocCount());
					tomcat.setRowId(i);
					list.add(tomcat);
					i++;
				}
			}
		} catch (IndexNotFoundException e) {
			System.err.println("此索引不存在!");
		} catch (Exception e) {
			System.err.println("java.lang.IllegalArgumentException:clientip字段不允许聚合!");
		}
		return list;
	}

	@Override
	public TomcatModel tomcatRequestType(String requestType, String index) {
		if (index.isEmpty() == true) {
			return null;
		}
		TransportClient client = getClient();
		TomcatModel tomcat = new TomcatModel();
		try {
			MatchQueryBuilder queryBuilder = QueryBuilders.matchQuery("verb", requestType);
			// GET POST ..
			SearchResponse response = client.prepareSearch(index).setTypes(getIndexType()).setQuery(queryBuilder)
					.execute().actionGet();
			SearchHits myhits = response.getHits();
			tomcat.addKey(requestType);
			tomcat.addValue(myhits.getTotalHits());
		} catch (IndexNotFoundException e) {
			System.err.println("此索引不存在!");
		}
		return tomcat;
	}

	/**
	 * 网络请求展示
	 */
	@Override
	public List<TomcatModel> tomcatRequestAll(Page page, String index, String startDate, String endDate) {
		if (index.isEmpty() == true) {
			return null;
		}
		// 默认返回当前索引存储的所有日志
		if (startDate.isEmpty() == true || endDate.isEmpty() == true) {
			startDate = "20000101";
			endDate = DateUtil.getCurrentDate();
		}
		if (startDate.isEmpty() == false && endDate.isEmpty() == false
				&& RegexUtil.judgeDateInterval(startDate, endDate) == false) {
			endDate = startDate;
		}
		TransportClient client = getClient();
		List<TomcatModel> list = new ArrayList<TomcatModel>();
		try {
			SearchResponse response = client.prepareSearch(index).setTypes(getIndexType()).setFrom(page.getStart())
					.setSize(page.getLength())
					.setQuery(QueryBuilders.rangeQuery("@timestamp").format("yyyyMMdd").from(startDate).to(endDate))
					.addSort("@timestamp", SortOrder.ASC).execute().actionGet();
			SearchHits myhits = response.getHits();
			page.setRecordsFiltered((int) myhits.getTotalHits());
			page.setRecordsTotal((int) myhits.getTotalHits());

			int i = 1;
			for (SearchHit hit : myhits.getHits()) {
				TomcatModel tomcat = new TomcatModel();
				Map<String, Object> map = hit.getSource();
				tomcat.setClientip((String) map.get("clientip"));
				tomcat.setResponse((String) map.get("response"));
				tomcat.setMessage((String) map.get("message"));
				tomcat.setType((String) map.get("verb"));
				tomcat.setTimestamp((String) map.get("timestamp"));
				tomcat.setRowId(i);
				list.add(tomcat);
				i++;
			}
		} catch (IndexNotFoundException e) {
			System.err.println("此索引不存在!");
		}
		return list;
	}

	/**
	 * 错误日志统计
	 */
	@Override
	public List<TomcatModel> errorTomcatRequest(Page page, String index) {
		TransportClient client = getClient();
		if (index.isEmpty() == true) {
			return null;
		}
		List<TomcatModel> list = new ArrayList<TomcatModel>();
		try {
			SearchResponse response = client.prepareSearch(index).setTypes(getIndexType()).setFrom(page.getStart())
					.setSize(page.getLength()).setQuery(QueryBuilders.regexpQuery("response", "[3-5][0-9][0-9]"))
					.execute().actionGet();
			SearchHits myhits = response.getHits();
			page.setRecordsFiltered((int) myhits.getTotalHits());
			page.setRecordsTotal((int) myhits.getTotalHits());

			int i = 1;
			for (SearchHit hit : myhits.getHits()) {
				TomcatModel tomcat = new TomcatModel();
				Map<String, Object> map = hit.getSource();
				tomcat.setClientip((String) map.get("clientip"));
				tomcat.setResponse((String) map.get("response"));
				tomcat.setMessage((String) map.get("message"));
				tomcat.setType((String) map.get("verb"));
				tomcat.setTimestamp((String) map.get("timestamp"));
				tomcat.setRowId(i);
				list.add(tomcat);
				i++;
			}
		} catch (IndexNotFoundException e) {
			System.err.println("此索引不存在!");
		}
		return list;
	}

	@Override
	public TransportClient getClient() {
		return ESUtil.getClient();
	}

	@Override
	public String getIndexName() {
		return TomcatServiceImpl.INDEX_NAME;
	}

	@Override
	public String getIndexType() {
		return TomcatServiceImpl.INDEX_TYPE;
	}
}
