package com.osp.log.controller;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.osp.log.util.ElasticUtil;

@Controller
public class EsController {

	@Autowired
	private ElasticUtil elasticUtil;
	
	@ResponseBody
	@RequestMapping(value = "/")
	public String test() {
		return "test";
	}
	
	@ResponseBody
	@RequestMapping(value = "/test")
	public String userInfo() {
		TransportClient client = elasticUtil.getClient();
		BoolQueryBuilder boolq = new BoolQueryBuilder();
		String q = "examples";
		if (q.equals("")) {
			boolq.must(QueryBuilders.matchAllQuery());
			// multiMatchQuery(q, "newsTitle", "newsContent")
		} else {
			boolq.must(QueryBuilders.multiMatchQuery(q, "newsTitle","newsContent"));
//			boolq.must(QueryBuilders.matchQuery("newsTitle", q));
//			boolq.must(QueryBuilders.matchQuery("newsContent", q));
		}
//		if (!newsType.equals("")) {
//			boolq.must(QueryBuilders.matchPhraseQuery("newsType", newsType));
//		}
//		if (!newsSource.equals("")) {
//			boolq.must(QueryBuilders.matchPhraseQuery("newsSource", newsSource));
//		}
		/**
		 * 高亮设置
		 */
		HighlightBuilder hiBuilder = new HighlightBuilder();
		hiBuilder.preTags("<span class=\'pointKey\'>");
		hiBuilder.postTags("</span>");
		hiBuilder.field("newsTitle", 10);
		hiBuilder.field("newsContent", 50);

		/**
		 * 聚类.统计新闻来源和新闻的类型
		 */
//		TermsAggregationBuilder AggnewsSource = AggregationBuilders.terms("newsSource").field("newsSource");
//		TermsAggregationBuilder AggnewsType = AggregationBuilders.terms("newsType").field("newsType");

		/**
		 * 开始搜索
		 */
//		client.prepareGet()
		SearchResponse response = client.prepareSearch(q)  
		        .setFrom(0).setSize(10)  
		        .execute().actionGet();  
//		SearchResponse response = client.prepareSearch("news").setTypes("article").setSearchType(SearchType.DEFAULT)
//				.setQuery(boolq) // Query
//				.setFrom(pagesize * (page - 1)).setSize(pagesize).addAggregation(AggnewsType)
//				.addAggregation(AggnewsSource).highlighter(hiBuilder)  
//				// .addSort(sortbuilder)
//				.setExplain(true) // 设置是否按查询匹配度排序
//				.get();
		SearchHits myhits = response.getHits();
		
		return "1";
	}
}
