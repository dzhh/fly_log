package com.osp.log.controller;

import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHitField;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
			boolq.must(QueryBuilders.multiMatchQuery(q, "newsTitle", "newsContent"));
			// boolq.must(QueryBuilders.matchQuery("newsTitle", q));
			// boolq.must(QueryBuilders.matchQuery("newsContent", q));
		}
		// if (!newsType.equals("")) {
		// boolq.must(QueryBuilders.matchPhraseQuery("newsType", newsType));
		// }
		// if (!newsSource.equals("")) {
		// boolq.must(QueryBuilders.matchPhraseQuery("newsSource", newsSource));
		// }

		/**
		 * 聚类.统计新闻来源和新闻的类型
		 */
		// TermsAggregationBuilder AggnewsSource =
		// AggregationBuilders.terms("newsSource").field("newsSource");
		// TermsAggregationBuilder AggnewsType =
		// AggregationBuilders.terms("newsType").field("newsType");

		/**
		 * 开始搜索
		 */
		
		
		//单个匹配，搜索name为jack的文档  
		//QueryBuilder queryBuilder = QueryBuilders.matchQuery("name", "jack");
		
		//搜索name中或interest中包含有music的文档（必须与music一致）
		 //QueryBuilder queryBuilder = QueryBuilders.multiMatchQuery("music",
		 //           "name", "interest");
		
		//模糊查询，?匹配单个字符，*匹配多个字符
		//WildcardQueryBuilder queryBuilder = QueryBuilders.wildcardQuery("name",  
	    //        "*jack*");//搜索名字中含有jack文档（name中只要包含jack即可）  
		
		
		//使用BoolQueryBuilder进行复合查询  模糊查询
		//（name中必须含有jack,interest中必须含有read,只有一条文档匹配）
//		WildcardQueryBuilder queryBuilder1 = QueryBuilders.wildcardQuery(
//		            "name", "*jack*");//搜索名字中含有jack的文档
//		WildcardQueryBuilder queryBuilder2 = QueryBuilders.wildcardQuery(
//		            "interest", "*read*");//搜索interest中含有read的文档
//
//		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
//		//name中必须含有jack,interest中必须含有read,相当于and
//		boolQueryBuilder.must(queryBuilder1);
//		boolQueryBuilder.must(queryBuilder2);
		
		
//		WildcardQueryBuilder queryBuilder1 = QueryBuilders.wildcardQuery(
//	            "name", "*jack*");//搜索名字中含有jack的文档
//	WildcardQueryBuilder queryBuilder2 = QueryBuilders.wildcardQuery(
//	            "interest", "*read*");//搜索interest中含有read的文档
//
//	BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
//	//name中含有jack或者interest含有read，相当于or
//	boolQueryBuilder.should(queryBuilder1);
//	boolQueryBuilder.should(queryBuilder2);
		
		
		SearchResponse response = client.prepareSearch("logstash-apacheaccesslog*") 
//				.setQuery(QueryBuilders.termQuery("request", q))
				.setQuery(QueryBuilders.termQuery("verb", "GET"))
				.execute().actionGet();
		
		

		// SearchResponse response = client.prepareSearch("index1", "index2")
		// .setTypes("type1", "type2")
		// .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		// .setQuery(QueryBuilders.termQuery("multi", "test")) // Query
		// .setPostFilter(FilterBuilders.rangeFilter("age").from(12).to(18)) //
		// Filter
		// .setFrom(0).setSize(60).setExplain(true)
		// .execute()
		// .actionGet();
		// SearchResponse response =
		// client.prepareSearch("news").setTypes("article").setSearchType(SearchType.DEFAULT)
		// .setQuery(boolq) // Query
		// .setFrom(pagesize * (page -
		// 1)).setSize(pagesize).addAggregation(AggnewsType)
		// .addAggregation(AggnewsSource).highlighter(hiBuilder)
		// // .addSort(sortbuilder)
		// .setExplain(true) // 设置是否按查询匹配度排序
		// .get();
		SearchHits myhits = response.getHits();
		for (SearchHit searchHit : myhits) {
			System.out.println(searchHit.getSource().toString());
			Map<String, Object> map = searchHit.getSource();
			for(String key : map.keySet()) {
				System.out.println("key = " + key + " value = " +  searchHit.getSource().get(key));
			}
		}

		return "1";
	}
}
