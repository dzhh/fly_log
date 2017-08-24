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
