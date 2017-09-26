package com.osp.log.controller;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.osp.log.service.SearchService;

@Controller
public class EsSearchController {
	
	@Autowired
	SearchService searchService;

	/**
	 * 搜索关键字
	 * @param q 关键字
	 * @param newsSource
	 * @param newsType
	 * @param page 页码
	 * @param pagesize 页数大小
	 * @param ip 请求客户端ip
	 * @param city 请求客户端所在城市
	 * @return
	 */
	@RequestMapping(value = "/search/get")
	@ResponseBody
	public  Response get(@DefaultValue("") @QueryParam("q") String q, 
			@DefaultValue("1") @QueryParam("page") Integer page,
			@DefaultValue("10") @QueryParam("pagesize") Integer pagesize,
			@DefaultValue("0.0.0.0") @QueryParam("ip") String ip,
			@DefaultValue("濮阳") @QueryParam("city") String city) {

		/*
		 * 获取ip
		 */
		System.out.println("ip=" + ip + " city=" + city);
		return Response.status(200).entity(searchService.getSearchesult(q, ip, city, page, pagesize)).build();

	}

}
