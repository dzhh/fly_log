package com.osp.log.service.impl;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.AvgAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.InternalAvg;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;

import com.osp.common.json.JsonUtil;
import com.osp.log.config.ESConfig;
import com.osp.log.model.SearchModel;
import com.osp.log.model.SearchResultBean;
import com.osp.log.model.TomcatModel;
import com.osp.log.service.SearchService;
import com.osp.log.util.DateUtil;
import com.osp.log.util.ESUtil;

/**
 * 关键字搜索
 * 
 * @author zhangmingcheng 2017-09-26
 */
@Service
public class SearchServiceImpl implements SearchService {

	public static final String INDEX_NAME = "logstash-apacheaccesslog*"; // 索引名称
	public static final String INDEX_TYPE = "logs"; // 索引类型

	/**
	 * 关键字搜索
	 * 
	 * @param q
	 *            关键词
	 * @param page
	 *            页码
	 * @param pageSize
	 *            页数大小
	 * @param ip
	 *            请求客户端ip
	 * @param city
	 *            请求客户端所在城市
	 * @return
	 */
	@Override
	public SearchResultBean getSearchesult(String keyword, String ip, String city, Integer page, Integer pagesize) {
		TransportClient client = ESUtil.getClient();
		BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
		if (keyword.isEmpty()) {
			boolQueryBuilder.must(QueryBuilders.matchAllQuery());
		} else {
			boolQueryBuilder.must(QueryBuilders.multiMatchQuery(keyword, "request", "verb", "clientip", "message",
					"path", "response", "host", "timestamp"));
		}
		/**
		 * 高亮设置
		 */
		HighlightBuilder hiBuilder = new HighlightBuilder();
		hiBuilder.preTags("<span class=\'pointKey\'>");
		hiBuilder.postTags("</span>");
		hiBuilder.field("request", 50);
		hiBuilder.field("message", 30);
		/**
		 * 开始搜索
		 */
		SearchResponse response = client.prepareSearch(SearchServiceImpl.INDEX_NAME)
				.setTypes(SearchServiceImpl.INDEX_TYPE).setSearchType(SearchType.DEFAULT).setQuery(boolQueryBuilder)
				.setFrom(pagesize * (page - 1)).setSize(pagesize).highlighter(hiBuilder).setExplain(true) // 设置是否按查询匹配度排序
				.get();
		SearchHits myhits = response.getHits();
		LinkedList<TomcatModel> newsList = new LinkedList<>();
		for (SearchHit hit : myhits) {
			Map<String, Object> map = hit.getSource();
			TomcatModel tomcatModel = new TomcatModel();
			String request = this.getHighlightFieldString(hit, "request");
			String message = this.getHighlightFieldString(hit, "message");
			if (request.isEmpty()) {
				tomcatModel.setRequest((String) map.get("request"));
			} else {
				tomcatModel.setRequest(request);
			}
			if (message.isEmpty()) {
				tomcatModel.setMessage((String) map.get("message"));
			} else {
				tomcatModel.setMessage(message);
			}
			tomcatModel.setPath((String) map.get("path"));
			tomcatModel.setClientip((String) map.get("clientip"));
			tomcatModel.setResponse((String) map.get("response"));
			tomcatModel.setType((String) map.get("verb"));
			tomcatModel.setTimestamp((String) map.get("timestamp"));
			newsList.add(tomcatModel);
		}
		/**
		 * 记录本次客户端查询：翻页不存储
		 */
		String usetime = response.getTook().toString();
		if (page == 1) {
			String json = JsonUtil.beanToJson(new SearchModel(keyword, DateUtil.getCurrentTime(), ip, city,
					Integer.parseInt(usetime.substring(0, usetime.length() - 2)), System.currentTimeMillis()));
			client.prepareIndex(ESConfig.SEARCHINDEX, ESConfig.SEARCHTYPE).setSource(json, XContentType.JSON).get();
		}
		/**
		 * 开始存储结果
		 */
		SearchResultBean searchResult = new SearchResultBean();
		searchResult.setPage(page);
		searchResult.setPagesize(pagesize);
		searchResult.setTotal(myhits.getTotalHits());
		searchResult.setUsetime(usetime);
		searchResult.setNewsList(newsList);
		return searchResult;
	}

	/**
	 * 获取搜索统计数据
	 */
	@Override
	public Response getcount() {
		HashMap<String, Object> RealReult = new HashMap<>();
		TransportClient client = getClient();
		TermsAggregationBuilder agg_clientip = AggregationBuilders.terms("cilentip_count").field("clientip");
		AvgAggregationBuilder avg_usetime = AggregationBuilders.avg("avg_usetime").field("usetime");
		TermsAggregationBuilder agg_message = AggregationBuilders.terms("agg_message").field("message");
		SearchResponse response = client.prepareSearch(ESConfig.SEARCHINDEX).setTypes(ESConfig.SEARCHTYPE)
				.addAggregation(agg_clientip).addAggregation(avg_usetime).addAggregation(agg_message)
				.setQuery(QueryBuilders.matchAllQuery()).execute().actionGet();
		/**
		 * 搜索次数
		 */
		RealReult.put("total", response.getHits().getTotalHits());
		/**
		 * 统计访问人数
		 */
		Terms terms = response.getAggregations().get("cilentip_count");
		List<Bucket> buckets = terms.getBuckets();
		RealReult.put("people", buckets.size());
		/**
		 * 统计平均搜索用时
		 */
		InternalAvg avg = response.getAggregations().get("avg_usetime");
		DecimalFormat df = new DecimalFormat("#.00");
		RealReult.put("avgUsetime", df.format(avg.getValue()).trim() + "ms");
		/**
		 * 统计最多搜索的10个词(聚合默认返回10个值)
		 */
		Terms messageTerms = response.getAggregations().get("agg_message");
		List<Bucket> messageBuckets = messageTerms.getBuckets();
		LinkedList<TopWord> result = new LinkedList<TopWord>();
		for (Bucket bucket : messageBuckets) {
			TopWord topWord = new TopWord();
			topWord.set_id((String) bucket.getKey());
			topWord.setCount((int) bucket.getDocCount());
			result.add(topWord);
		}
		RealReult.put("Top", result);
		return Response.status(200).entity(RealReult).build();
	}

	/**
	 * 获取历史搜索记录
	 */
	@Override
	public String getHistoryList(Integer page, Integer pagesize) {
		LinkedList<Map<String, Object>> result = new LinkedList<>();
		TransportClient client = getClient();
		SearchResponse response = client.prepareSearch(ESConfig.SEARCHINDEX).setTypes(ESConfig.SEARCHTYPE)
				.setQuery(QueryBuilders.matchAllQuery()).addSort("createDate", SortOrder.DESC)
				.setFrom(pagesize * (page - 1)).setSize(pagesize).get();
		SearchHits myhits = response.getHits();
		for (SearchHit hit : myhits) {
			Map<String, Object> hitmap = hit.getSource();
			HashMap<String, Object> map = new HashMap<>();
			map.put("q", hitmap.get("message"));
			map.put("usetime", hitmap.get("usetime"));
			map.put("city", hitmap.get("city"));
			map.put("ip", hitmap.get("clientip"));
			map.put("total", myhits.getTotalHits());
			map.put("time", hitmap.get("timestamp"));
			result.add(map);
		}
		HashMap<String, Object> RealReult = new HashMap<>();
		RealReult.put("rows", result);
		RealReult.put("total", myhits.getTotalHits());
		return JsonUtil.beanToJson(RealReult);
	}

	public String getHighlightFieldString(SearchHit hit, String field) {
		String content = "";
		if (hit.getHighlightFields().containsKey(field)) {
			Text[] text = hit.getHighlightFields().get(field).getFragments();
			for (Text str : text) {
				content = content + str;
			}
		}
		return content;
	}

	public TransportClient getClient() {
		return ESUtil.getClient();
	}

	class TopWord {
		private String _id;
		private int count;

		public String get_id() {
			return _id;
		}

		public void set_id(String _id) {
			this._id = _id;
		}

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}
	}
}
