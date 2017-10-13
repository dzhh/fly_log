package com.osp.log.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.IndexNotFoundException;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.stereotype.Service;

import com.osp.log.model.Page;
import com.osp.log.model.SqlModel;
import com.osp.log.service.SqlService;
import com.osp.log.util.ESUtil;

/**
 * sql日志分析业务类
 * 
 * @author zhangmingcheng 2017-10-13
 */
@Service
public class SqlServiceImpl implements SqlService {

	/**
	 * 展示收集的sql日志，获取sql错误日志
	 */
	@Override
	public List<SqlModel> getSqlLogs(Page page, String index, String type) {
		if (index.isEmpty() == true||type.isEmpty()==true) {
			return null;
		}
		TransportClient client = getClient();
		List<SqlModel> list = new ArrayList<SqlModel>();
		try {
			SearchResponse response = client.prepareSearch(index).setTypes(type).setFrom(page.getStart())
					.setSize(page.getLength()).execute().actionGet();
			SearchHits myhits = response.getHits();
			page.setRecordsFiltered((int) myhits.getTotalHits());
			page.setRecordsTotal((int) myhits.getTotalHits());

			int i = 1;
			for (SearchHit hit : myhits.getHits()) {
				SqlModel sqlModel = new SqlModel();
				Map<String, Object> map = hit.getSource();
				sqlModel.setMessage((String) map.get("message"));
				sqlModel.setType((String) map.get("type"));
				sqlModel.setHost((String) map.get("host"));
				//获取sql日志时间
				sqlModel.setTimestamp((String) map.get("@timestamp"));
				//获取sql错误日志时间
				sqlModel.setMysql_time((String) map.get("mysql_time"));
				sqlModel.setRowId(i);
				list.add(sqlModel);
				i++;
			}
		} catch (IndexNotFoundException e) {
			System.err.println("此索引不存在!");
		}
		return list;
	}
	
	public TransportClient getClient() {
		return ESUtil.getClient();
	}
}
