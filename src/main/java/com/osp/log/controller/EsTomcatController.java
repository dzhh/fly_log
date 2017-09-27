package com.osp.log.controller;


import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.osp.common.json.JsonUtil;
import com.osp.log.model.Page;
import com.osp.log.model.TomcatModel;
import com.osp.log.service.TomcatService;

import net.sf.json.JSONObject;

/**
 * 针对tomcat处理
 * 
 * @author fly 2017/08/24
 *
 */
@Controller
public class EsTomcatController {

	@Autowired
	TomcatService tomcatService;
	
	@RequestMapping("/")  
	public String index(){  
		return "index.html"; 
	}
	
	
	
	@ResponseBody
	@RequestMapping(value = "/tomcatRequestAll")
	public String tomcatRequestAll(HttpServletRequest request) {
		Page page = new Page();
		page.setDraw(Integer.parseInt(request.getParameter("draw").toString()));
		page.setStart(Integer.parseInt(request.getParameter("start").toString()));
		page.setLength(Integer.parseInt(request.getParameter("length").toString()));
		System.out.println("start " + page.getStart() + " length" + page.getLength());
		
		List<TomcatModel> list = tomcatService.tomcatRequestAll(page);
        String json = JsonUtil.beanListToJson(list);
        JSONObject jso = new JSONObject();
        jso.put("draw", page.getDraw());
        jso.put("recordsTotal", page.getRecordsTotal());
        jso.put("recordsFiltered", page.getRecordsFiltered());
        jso.put("data", json);
        System.out.println(jso.toString());
		return jso.toString();
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/errorTomcatRequest")
	public String errorTomcatRequest(HttpServletRequest request) {
		Page page = new Page();
		page.setDraw(Integer.parseInt(request.getParameter("draw").toString()));
		page.setStart(Integer.parseInt(request.getParameter("start").toString()));
		page.setLength(Integer.parseInt(request.getParameter("length").toString()));
		
		List<TomcatModel> list = tomcatService.errorTomcatRequest(page);
        String json = JsonUtil.beanListToJson(list);
        JSONObject jso = new JSONObject();
        jso.put("draw", page.getDraw());
        jso.put("recordsTotal", page.getRecordsTotal());
        jso.put("recordsFiltered", page.getRecordsFiltered());
        jso.put("data", json);
		return jso.toString();
	}
	
	/**
	 * 获取最近10天tomcat请求数
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/tomcatRequestDate")
	public String tomcatTimeSearch(HttpServletRequest request) {
		TomcatModel tomcat = tomcatService.tomcatTimeSearch(10);
		return JsonUtil.beanToJson(tomcat);
	}
	
	/**
	 * tomcat请求方法统计
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/tomcatRequest")
	public String tomcatRequest(HttpServletRequest request) {
		TomcatModel tomcat = tomcatService.tomcatRequest();
		return JsonUtil.beanToJson(tomcat);
	}
	
	/**
	 * tomcat客户端请求数统计
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/clientRequestCount")
	public String clientRequestCount(HttpServletRequest request) {
		Page page = new Page();
		page.setDraw(Integer.parseInt(request.getParameter("draw").toString()));
		page.setStart(Integer.parseInt(request.getParameter("start").toString()));
		page.setLength(Integer.parseInt(request.getParameter("length").toString()));
		System.out.println("start " + page.getStart() + " length" + page.getLength());
		
		List<TomcatModel> list = tomcatService.clientRequestCount();
        String json = JsonUtil.beanListToJson(list);
        JSONObject jso = new JSONObject();
        jso.put("draw", page.getDraw());
        jso.put("recordsTotal", page.getRecordsTotal());
        jso.put("recordsFiltered", page.getRecordsFiltered());
        jso.put("data", json);
        System.out.println(jso.toString());
		return jso.toString();
	}
	
	@ResponseBody
	@RequestMapping(value = "/tomcatRequestType")
	public String tomcatRequestType(HttpServletRequest request) {
		String requestType = (String) request.getParameter("requestType");
		TomcatModel tomcat = tomcatService.tomcatRequestType(requestType);
		return JsonUtil.beanToJson(tomcat);
	}
	
    private String message = "hi,hello world......";
	@RequestMapping("/web")  
	public String web(Map<String,Object> model){  
		model.put("time",new Date());  
		model.put("message",this.message);  
		return "web";//返回的内容就是templetes下面文件的名称  
	}
	
//	@RequestMapping("/table")  
//	public String table(Map<String,Object> model){  
//		return "table";//返回的内容就是templetes下面文件的名称  
//	}
}
