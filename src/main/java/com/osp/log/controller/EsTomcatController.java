package com.osp.log.controller;


import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.osp.common.json.JsonUtil;
import com.osp.log.model.TomcatModel;
import com.osp.log.service.TomcatService;

/**
 * 针对tomcat处理
 * 
 * @author fly 2017/08/24
 *
 */
@Controller
public class ESTomcatController {

	@Autowired
	TomcatService tomcatService;
	
	@ResponseBody
	@RequestMapping(value = "/tomcatRequestDate")
	public String tomcatTimeSearch(HttpServletRequest request) {
		//获取最近十天
        int day = 10;
		TomcatModel tomcat = tomcatService.tomcatTimeSearch(day);
        String json = JsonUtil.beanToJson(tomcat);
		return json;
	}
	
	@ResponseBody
	@RequestMapping(value = "/tomcatRequest")
	public String tomcatRequest(HttpServletRequest request) {
		TomcatModel tomcat = tomcatService.tomcatRequest();
		
		String json = JsonUtil.beanToJson(tomcat);
		return json;
//		String json = "{\"key\":[\"GET\",\"POST\",\"PUT\"],\"value\":[\"" + myhitsGet.getTotalHits() + 
//				 "\",\"" + myhitsPost.getTotalHits() + "\",\"" + myhitsPut.getTotalHits() + "\"]}";
//		return json;
	}
	
	@ResponseBody
	@RequestMapping(value = "/tomcatRequestType")
	public String tomcatRequestType(HttpServletRequest request) {
		String requestType = (String) request.getParameter("requestType");
		TomcatModel tomcat = tomcatService.tomcatRequestType(requestType);
		return JsonUtil.beanToJson(tomcat);
	}
}
