package com.osp.log.test;

import java.text.DecimalFormat;
import java.util.List;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.aggregations.metrics.avg.AvgAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.InternalAvg;

import com.osp.log.config.ESConfig;
import com.osp.log.util.ESUtil;

/**
 * 2017-09-28
 * 
 * @author zhangmingcheng
 */
public class ESSearchTest {

	public static void main(String[] args) {
		ESConfig.ES_CLUSTERNAME = "osp";
		ESConfig.ES_IP = "10.75.8.167";
		ESConfig.ES_PORT = 9300;
		/**
		 * 获取客户端连接
		 */
		TransportClient client = ESSearchTest.getClient();
		/**
		 * 聚合clientip字段
		 */
		TermsAggregationBuilder agg_clientip = AggregationBuilders.terms("cilentip_count").field("clientip");
		/**
		 * 聚合usetime字段
		 */
		AvgAggregationBuilder avg_usetime = AggregationBuilders.avg("avg_usetime").field("usetime");
		TermsAggregationBuilder agg_message = AggregationBuilders.terms("agg_message").field("message");
		SearchResponse response = client.prepareSearch("searchlogs").setTypes("logs")
				.addAggregation(agg_clientip).addAggregation(avg_usetime).addAggregation(agg_message)
				.setQuery(QueryBuilders.matchAllQuery()).execute().actionGet();
		/**
		 * QueryBuilders.matchAllQuery()
		 */
		System.out.println("搜索命中数="+response.getHits().getTotalHits());
	
		/**
		 * 统计访问人数  key:cilentip_count
		 */
		Terms terms = response.getAggregations().get("cilentip_count");
		List<Bucket> buckets = terms.getBuckets();
		System.out.println("访问人数="+buckets.size());
		
		/**
		 * 统计平均搜索用时 key:avg_usetime
		 */
		InternalAvg avg = response.getAggregations().get("avg_usetime");
		DecimalFormat df = new DecimalFormat("#.00");
		System.out.println("平均用时="+df.format(avg.getValue()).trim() + "ms");
		
	}

	public static TransportClient getClient() {
		return ESUtil.getClient();
	}

}
