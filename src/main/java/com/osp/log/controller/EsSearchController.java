package com.osp.log.controller;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.osp.log.service.SearchService;
/**
 * 
 * @author zhangmingcheng
 */
@Controller
public class EsSearchController {

	@Autowired
	SearchService searchService;

	/**
	 * 搜索关键字
	 * 
	 * @param q
	 *            关键字
	 * @param newsSource
	 * @param newsType
	 * @param page
	 *            页码
	 * @param pagesize
	 *            页数大小
	 * @param ip
	 *            请求客户端ip
	 * @param city
	 *            请求客户端所在城市
	 * @return
	 */
	@RequestMapping(value = "/search/get")
	@ResponseBody
	public Response get(@RequestParam(value = "q", defaultValue = "") String q,
			@RequestParam(value = "page", defaultValue = "1") Integer page,
			@RequestParam(value = "pagesize", defaultValue = "10") Integer pagesize,
			@RequestParam(value = "ip", defaultValue = "0.0.0.0") String ip,
			@RequestParam(value = "city", defaultValue = "北京市") String city) {
		/*
		 * 获取ip
		 */
		System.out.println("q=" + q + " page=" + page + " pagesize=" + pagesize + " ip=" + ip + " city=" + city);
		return Response.status(200).entity(searchService.getSearchesult(q, ip, city, page, pagesize)).build();
	}

	/**
	 * 获取搜索统计数据
	 * 
	 * @return
	 */
	@RequestMapping(value = "/search/getcount")
	@ResponseBody
	public Response getcount() {
		return searchService.getcount();
	}

	/**
	 * 获取搜索历史
	 * 
	 * @param page
	 * @param pagesize
	 * @return
	 */
	@RequestMapping(value = "/search/getHistoryList")
	@ResponseBody
	public String getHistoryList(@RequestParam(value = "pageNumber", defaultValue = "1") Integer page,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pagesize) {
	    System.out.println("page="+page+" pagesize="+pagesize);
		return searchService.getHistoryList(page, pagesize);
	}

}
