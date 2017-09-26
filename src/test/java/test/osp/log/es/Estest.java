package test.osp.log.es;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.DocValueFormat.DateTime;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.ExtendedBounds;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram.Bucket;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.joda.time.DateTimeZone;

import com.osp.log.config.ESConfig;
import com.osp.log.util.DateUtil;
import com.osp.log.util.ESUtil;

public class Estest {

	
	public static void main(String[] args) {
		TransportClient client = getTransportClient();

//		for(int i=0; i<100; i++) {
//			testTomcat(client);
//		}
//		testTomcat(client);
//		testTomcatLog11(client);
//		testTomcatLog22(client);
		testTomcatLogAll(client);
	}
	public static void testTomcatLogAll(TransportClient client) {
		//设置一个叫	types 聚合，聚合字段是types（这里可以多个），然后 size = 0 标示全局
		AggregationBuilder aggregation = AggregationBuilders.terms("response").field("response");
        //查询索引
    	SearchRequestBuilder srb = client.prepareSearch("logstash-apacheaccesslog*")
    			.setTypes("logs");
    	SearchResponse response = srb.execute().actionGet();
		System.out.println("execute time = " + response.getTook());
		SearchHits myhitsGet = response.getHits();
		System.out.println("execute Hits = " + myhitsGet.getTotalHits());
		System.out.println(response.toString());
		
		for(SearchHit hit : myhitsGet.getHits()) {
			Map<String, Object> map = hit.getSource();
			String response1 = (String) map.get("response");
			System.out.println(response1);
		}
	}
	
	
	public static void testTomcatLog22(TransportClient client) {
		
		//设置一个叫	types 聚合，聚合字段是types（这里可以多个），然后 size = 0 标示全局
		AggregationBuilder aggregation = AggregationBuilders.terms("response").field("response");
        //查询索引
    	SearchRequestBuilder srb = client.prepareSearch("logstash-apacheaccesslog*")
    			.setTypes("logs");
    	srb.addAggregation(aggregation);
		SearchResponse response = srb.execute().actionGet();
		System.out.println("execute time = " + response.getTook());
		SearchHits myhitsGet = response.getHits();
		System.out.println("execute Hits = " + myhitsGet.getTotalHits());
		System.out.println(response.toString());
		Aggregations agg = response.getAggregations();		
		//取到聚合数据
		Terms terms = agg.get("response");
//		String type = terms.getType();
	}
		
	
	public static void testTomcatLog11(TransportClient client) {
		
		
		int day = 1;
		BoolQueryBuilder boolq = new BoolQueryBuilder();
		//组装分组  
		DateHistogramAggregationBuilder dateAgg = AggregationBuilders.dateHistogram("dateagg");  
		 //定义分组的日期字段  
        dateAgg.field("@timestamp");
        dateAgg.minDocCount(0);
        ExtendedBounds extendedBounds = new ExtendedBounds(DateUtil.getPastDate(day), DateUtil.getDate());
        dateAgg.extendedBounds(extendedBounds);
        dateAgg.dateHistogramInterval(DateHistogramInterval.DAY);  
        DateTimeZone timeZone = DateTimeZone.forID("Asia/Shanghai");
        dateAgg.timeZone(timeZone);
        dateAgg.format("yyyy-MM-dd"); 
        
       
        System.out.println(DateUtil.getPastDate(day));
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("@timestamp")
        		.from(DateUtil.getPastDate(day))
        		.to(DateUtil.getDate())
				.includeLower(true) // 包括下界
				.includeUpper(true); // 包括上界
        
		MatchQueryBuilder queryBuilderGet = QueryBuilders.matchQuery("response", "404");

        
        //查询索引
    	SearchRequestBuilder srb = client.prepareSearch("logstash-apacheaccesslog*")
    			.setTypes("logs");
    	 //组装请求  
    	srb.setQuery(rangeQueryBuilder)
    		.setQuery(queryBuilderGet)
    		.addAggregation(dateAgg);

	    /*******************************|日期查询代码 end|***********************************/
	    
    	SearchResponse response = srb.execute().actionGet();
		System.out.println("execute time = " + response.getTook());
		SearchHits myhitsGet = response.getHits();
		System.out.println("execute Hits = " + myhitsGet.getTotalHits());
		System.out.println(response.toString());
		
		//获取一级聚合数据  
        Histogram h = response.getAggregations().get("dateagg");
        //得到一级聚合结果里面的分桶集合 
        //org.elasticsearch.search.aggregations.bucket.histogram
        List<Bucket> buckets = (List<Bucket>) h.getBuckets();
        System.out.println("buckets = " + buckets.size());
        for(int i=day-buckets.size(); i>0 ; i--) {
        	System.out.println(DateUtil.getPastDate(i));
        	System.out.println("数量 0" );
        }
        DateUtil.getPastDate(day);
        //遍历分桶集  
        for(Bucket b : buckets){
        	//读取二级聚合数据集引用  
            Aggregations sub = b.getAggregations();
            List<Aggregation> list = sub.asList();
            
            org.joda.time.DateTime key = (org.joda.time.DateTime)b.getKey();
            System.out.println(key.getYear() + "-" + key.getMonthOfYear() 
            					+ "-" + key.getDayOfMonth());
            System.out.println("时间：" + b.getKey() + "  数量" + b.getDocCount());
            
        }
	}
	
	
	public static void testTomcatLog(TransportClient client) {
		BoolQueryBuilder boolq = new BoolQueryBuilder();
		
		MatchQueryBuilder queryBuilderGet = QueryBuilders.matchQuery("verb", "GET");
		
//		TermsAggregationBuilder teamAgg= AggregationBuilders.terms("verb").field("GET");
//		TermsAggregationBuilder teamAgg= AggregationBuilders.terms("agg1").field("verb");
		//GET  POST  ..
		  /*******************************|日期查询代码 start|***********************************/
	    //是否只查询当天
	    	//取到当天凌晨时间
    	Calendar cal = Calendar.getInstance();
    	cal.set(Calendar.HOUR_OF_DAY,0);
    	cal.set(Calendar.MINUTE, 0);
    	cal.set(Calendar.SECOND, 0);
    	cal.add(Calendar.DATE,0);
    	
    	SearchRequestBuilder srb = client.prepareSearch("logstash-apacheaccesslog*")
    			.setTypes("logs");
    	//查询今天的的数据,查询createDate 字段，大于当天0点的时间
    	System.out.println(cal.getTime());
//    	srb.setQuery(QueryBuilders.rangeQuery("timestamp").gt(cal.getTime()));
    	String time = "2017-10-01";
    	srb.setQuery(QueryBuilders.rangeQuery("@timestamp").gt(time));
    	srb.setQuery(QueryBuilders.rangeQuery("@timestamp").from("2017-08-11").
    			to("2017-09-12")
				.includeLower(true) // 包括下界
				.includeUpper(true)); // 包括上界

	    /*******************************|日期查询代码 end|***********************************/
	    
    	SearchResponse responseGet = srb.execute().actionGet();
//		SearchResponse responseGet = client.prepareSearch("logstash-apacheaccesslog*") 
//				.setQuery(queryBuilderGet)
////				.addAggregation(teamAgg)
//				.execute().actionGet();
		System.out.println("execute time = " + responseGet.getTook());
		SearchHits myhitsGet = responseGet.getHits();
		System.out.println("execute Hits = " + myhitsGet.getTotalHits());
		System.out.println(responseGet.toString());
		
	}

	
	
	public static void testTomcat(TransportClient client) {
		
		BoolQueryBuilder boolq = new BoolQueryBuilder();
		
		MatchQueryBuilder queryBuilderGet = QueryBuilders.matchQuery("message", "main");
		//GET  POST  ..
		SearchResponse responseGet = client.prepareSearch("logstash*") 
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
		System.out.println(json);
	}
	
	
	public static TransportClient getTransportClient() {
//		ESConfig elasticConfig = new ESConfig();
//		elasticConfig.setClusterName("osp");
//		elasticConfig.setIp("192.168.206.129");
//		elasticConfig.setPort("9300");
		
		ESUtil elasticUtil = new ESUtil();
//		elasticUtil.setElasticConfig(elasticConfig);
		TransportClient client = elasticUtil.getClient();
		return client;
	}
	
	
}

