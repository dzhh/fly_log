package com.osp.log.service;

import javax.ws.rs.core.Response;

import com.osp.log.model.SearchResultBean;
/**
 * 2017-9-26
 * @author zhangmingcheng
 */
public interface SearchService {
	
	/**
	 * 关键字搜索
	 * @param q  关键词
	 * @param page  页码
	 * @param pageSize 页数大小
	 * @param ip  请求客户端ip
	 * @param city  请求客户端所在城市
	 * @return
	 */
	SearchResultBean getSearchesult(String keyword,String ip,String city,Integer page,Integer pagesize);
	/**
	 * 获取搜索统计数据
	 * @return
	 */
	Response getcount();
	/**
	 * 获取历史搜索记录
	 * @return
	 */
	String getHistoryList(Integer page,Integer pagesize);
}
