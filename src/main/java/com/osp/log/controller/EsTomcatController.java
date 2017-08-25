package com.osp.log.controller;

import javax.servlet.http.HttpServletRequest;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.WildcardQueryBuilder;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.osp.log.util.ElasticUtil;

/**
 * 针对tomcat处理
 * 
 * @author fly 2017/08/24
 *
 */
@Controller
public class EsTomcatController {

	@Autowired
	private ElasticUtil elasticUtil;
	
	@ResponseBody
	@RequestMapping(value = "/tomcatRequest")
	public String tomcatRequest(HttpServletRequest request) {
		TransportClient client = elasticUtil.getClient();
		BoolQueryBuilder boolq = new BoolQueryBuilder();
		
		MatchQueryBuilder queryBuilderGet = QueryBuilders.matchQuery("verb", "GET");
		//GET  POST  ..
		SearchResponse responseGet = client.prepareSearch("logstash-apacheaccesslog*") 
				.setQuery(queryBuilderGet)
				.execute().actionGet();
		System.out.println("execute time = " + responseGet.getTook());
		SearchHits myhitsGet = responseGet.getHits();
		System.out.println("execute Hits = " + myhitsGet.getTotalHits());
		
		
		MatchQueryBuilder queryBuilderPost = QueryBuilders.matchQuery("verb", "POST");
		SearchResponse responsePost = client.prepareSearch("logstash-apacheaccesslog*") 
				.setQuery(queryBuilderPost)
				.execute().actionGet();
		SearchHits myhitsPost = responsePost.getHits();

		
		MatchQueryBuilder queryBuilderPut = QueryBuilders.matchQuery("verb", "PUT");
		SearchResponse responsePut = client.prepareSearch("logstash-apacheaccesslog*") 
				.setQuery(queryBuilderPut)
				.execute().actionGet();
		SearchHits myhitsPut = responsePut.getHits();

		
		String json = "{\"key\":[\"GET\",\"POST\",\"PUT\"],\"value\":[\"" + myhitsGet.getTotalHits() + 
				 "\",\"" + myhitsPost.getTotalHits() + "\",\"" + myhitsPut.getTotalHits() + "\"]}";
		return json;
	}
	
	@ResponseBody
	@RequestMapping(value = "/tomcatRequestType")
	public String tomcatRequestType(HttpServletRequest request) {
		String requestType = (String) request.getParameter("requestType");
		TransportClient client = elasticUtil.getClient();
		BoolQueryBuilder boolq = new BoolQueryBuilder();
		
//		WildcardQueryBuilder queryBuilder = QueryBuilders.wildcardQuery("message", "*GET*");
//		TermQueryBuilder queryBuilder = QueryBuilders.termQuery("verb", "GET");
		MatchQueryBuilder queryBuilder = QueryBuilders.matchQuery("verb", requestType);
		//GET  POST  ..
		SearchResponse response = client.prepareSearch("logstash-apacheaccesslog*") 
//				.setQuery(QueryBuilders.termQuery("verb", requestType))
//				.setQuery(QueryBuilders.termQuery("verb", "GET"))
//				.setQuery(QueryBuilders.termQuery("clientip", "192.168.206.1"))
				.setQuery(queryBuilder)
				.execute().actionGet();
		
		System.out.println("execute time = " + response.getTook());

		SearchHits myhits = response.getHits();
		System.out.println("execute Hits = " + myhits.getTotalHits());
		
		return "";
	}
}
