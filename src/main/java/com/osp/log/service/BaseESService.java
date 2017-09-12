package com.osp.log.service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;

import com.osp.common.json.JsonUtil;


/**
 * ElasticSearch服务基类
 * 
 */
public abstract class BaseESService<T> {
	
	private String indexName; // 索引名称
	private String indexType; // 索引类型

	private Class<T> cls; // 泛型类
	private long start; // 初始化时间
	private boolean batchEnabled = false; // 是否启用批量操作
	private int batchSize = 50; // 批量大小
	private List<T> lists; // 缓存入ES数据
	private boolean isBatchSaveThreadToRunning = false; // 批量入库线程是否运行
	private Thread batchSaveThread; // 批量入库线程实例
	public TransportClient client;
	
	public BaseESService() {
	}

	/**
	 * 获取客户端连接
	 *
	 * @return
	 */
	public abstract TransportClient getClient();

	/**
	 * 获取索引名称
	 *
	 * @return
	 */
	public abstract String getIndexName();

	/**
	 * 获取索引类型
	 *
	 * @return
	 */
	public abstract String getIndexType();
	
	/**
	 * 条件查询
	 *
	 * @param query
	 * @param from
	 * @param size
	 * @param sortField
	 * @param sortOrder
	 * @return
	 * @throws Exception
	 */
	public List<T> query(BoolQueryBuilder query, int from, int size, String... indexTypes) throws Exception {
		return query(query, from, size, null, null, indexTypes);
	}
	
	/**
	 * 条件查询
	 *
	 * @param query
	 * @param from
	 * @param size
	 * @param sortField
	 * @param sortOrder
	 * @return
	 * @throws Exception
	 */
	public List<T> query(BoolQueryBuilder query, int from, int size) throws Exception {
		return query(query, from, size, null, null);
	}
	
	/**
	 * 条件查询
	 *
	 * @param query
	 * @param from
	 * @param size
	 * @param sortField
	 * @param sortOrder
	 * @return
	 * @throws Exception
	 */
	public List<T> query(BoolQueryBuilder query, int from, int size, String sortField, SortOrder sortOrder, String... indexTypes) throws Exception {
		
		List<T> list = new ArrayList<T>();
		
		SearchRequestBuilder builder = client.prepareSearch(indexName)
			.setTypes(indexTypes)
			.setQuery(query)
			.setFrom(from)
			.setSize(size);
		if (StringUtils.isNotEmpty(sortField)) {
			builder.addSort(sortField, sortOrder);
		}
		
		SearchResponse response = builder.execute().actionGet();
		SearchHits hits = response.getHits();
		
		if (hits.getTotalHits() > 0) {
			for (SearchHit hit : hits) {
				list.add(JsonUtil.jsonToBean(hit.getSourceAsString(), cls));
			}
		}
		return list;
	}
	
	/**
	 * 条件查询
	 *
	 * @param query
	 * @param from
	 * @param size
	 * @param sortField
	 * @param sortOrder
	 * @return
	 * @throws Exception
	 */
	public List<T> query(BoolQueryBuilder query, int from, int size, String sortField, SortOrder sortOrder) throws Exception {
		return query(query, from, size, sortField, sortOrder, indexType);
	}
	
	/**
	 * 统计总数
	 *
	 * @param query
	 * @return
	 * @throws Exception
	 */
	public long count(BoolQueryBuilder query) throws Exception {
		SearchResponse response = client.prepareSearch(indexName)
			.setTypes(indexType)
			.setSearchType(SearchType.QUERY_THEN_FETCH)
			.setQuery(query)
			.setFrom(0)
			.setSize(0)
			.execute()
			.actionGet();
		SearchHits hits = response.getHits();
		return hits.totalHits();
	}
}
