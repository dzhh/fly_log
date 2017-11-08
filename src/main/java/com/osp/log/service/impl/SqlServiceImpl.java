package com.osp.log.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;

import com.osp.log.model.DateInterval;
import com.osp.log.model.Page;
import com.osp.log.model.SqlModel;
import com.osp.log.service.SqlService;
import com.osp.log.util.ESUtil;
import com.osp.log.util.RegexUtil;

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
	public List<SqlModel> getSqlLogs(Page page, String index, String type, String startDate, String endDate) {
		if (ESUtil.isExistsIndex(index) == false || ESUtil.isExistsType(index, type) == false) {
			return null;
		}
		DateInterval dateInterval = RegexUtil.getDateInterval(new DateInterval(startDate, endDate), "yyyyMMdd");
		TransportClient client = getClient();
		List<SqlModel> list = new ArrayList<SqlModel>();
		try {
			SearchResponse response = client.prepareSearch(index).setTypes(type).setFrom(page.getStart())
					.setSize(page.getLength()).setQuery(QueryBuilders.rangeQuery("timestamp").format("yyyyMMdd")
							.from(dateInterval.getStartDate()).to(dateInterval.getEndDate()))
					.addSort("timestamp", SortOrder.DESC).execute().actionGet();
			SearchHits myhits = response.getHits();
			page.setRecordsFiltered(myhits.getTotalHits());
			page.setRecordsTotal(myhits.getTotalHits());

			int i = 1;
			for (SearchHit hit : myhits.getHits()) {
				SqlModel sqlModel = new SqlModel();
				Map<String, Object> map = hit.getSource();
				sqlModel.setMessage((String) map.get("message"));
				sqlModel.setType((String) map.get("type"));
				sqlModel.setTimestamp((String) map.get("timestamp"));
				sqlModel.setRowId(i);
				list.add(sqlModel);
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public TransportClient getClient() {
		return ESUtil.getClient();
	}
}
