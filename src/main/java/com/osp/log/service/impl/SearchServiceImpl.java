package com.osp.log.service.impl;

import java.util.LinkedList;
import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.stereotype.Service;

import com.osp.log.model.SearchResultBean;
import com.osp.log.model.TomcatModel;
import com.osp.log.service.SearchService;
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
	private Integer page;
	private Integer pagesize;

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

		initPageParam(page, pagesize);
		System.out.println("page===" + this.page + "   pageSize=" + this.pagesize);

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
				.setFrom(this.pagesize * (this.page - 1)).setSize(this.pagesize).highlighter(hiBuilder).setExplain(true) // 设置是否按查询匹配度排序
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
		 * 开始存储结果
		 */
		SearchResultBean searchResult = new SearchResultBean();
		searchResult.setPage(this.page);
		searchResult.setPagesize(this.pagesize);
		searchResult.setTotal(myhits.getTotalHits());
		searchResult.setUsetime(response.getTook().toString());
		searchResult.setNewsList(newsList);
		return searchResult;
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

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getPagesize() {
		return pagesize;
	}

	public void setPagesize(Integer pagesize) {
		this.pagesize = pagesize;
	}

	public void initPageParam(Integer page, Integer pagesize) {
		this.page = (page == null || page < 1) ? 1 : page;
		this.pagesize = (pagesize == null || pagesize < 1) ? 10 : pagesize;
	}

}
