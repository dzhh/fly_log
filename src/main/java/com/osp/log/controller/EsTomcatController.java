package com.osp.log.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	public String index() {
		return "index.html";
	}

	/**
	 * tomcat请求展示
	 * 
	 * @param request
	 * @param index
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/tomcatRequestAll")
	public String tomcatRequestAll(HttpServletRequest request,
			@RequestParam(value = "index", defaultValue = "") String index) {
		Page page = new Page();
		page.setDraw(Integer.parseInt(request.getParameter("draw").toString()));
		page.setStart(Integer.parseInt(request.getParameter("start").toString()));
		page.setLength(Integer.parseInt(request.getParameter("length").toString()));
		System.out.println("start " + page.getStart() + " length" + page.getLength());

		List<TomcatModel> list = tomcatService.tomcatRequestAll(page, index);
		String json = JsonUtil.beanListToJson(list);
		JSONObject jso = new JSONObject();
		jso.put("draw", page.getDraw());
		jso.put("recordsTotal", page.getRecordsTotal());
		jso.put("recordsFiltered", page.getRecordsFiltered());
		jso.put("data", json);
		return jso.toString();
	}

	/**
	 * http错误请求统计
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/errorTomcatRequest")
	public String errorTomcatRequest(HttpServletRequest request,
			@RequestParam(value = "index", defaultValue = "") String index) {
		Page page = new Page();
		page.setDraw(Integer.parseInt(request.getParameter("draw").toString()));
		page.setStart(Integer.parseInt(request.getParameter("start").toString()));
		page.setLength(Integer.parseInt(request.getParameter("length").toString()));

		List<TomcatModel> list = tomcatService.errorTomcatRequest(page, index);
		String json = JsonUtil.beanListToJson(list);
		JSONObject jso = new JSONObject();
		jso.put("draw", page.getDraw());
		jso.put("recordsTotal", page.getRecordsTotal());
		jso.put("recordsFiltered", page.getRecordsFiltered());
		jso.put("data", json);
		return jso.toString();
	}

	/**
	 * 获取最近n天tomcat请求数 n默认为10
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/tomcatRequestDate")
	public String tomcatTimeSearch(HttpServletRequest request,
			@RequestParam(value = "day", defaultValue = "10") Integer day,
			@RequestParam(value = "index", defaultValue = "") String index) {
		TomcatModel tomcat = tomcatService.tomcatTimeSearch(day, index);
		return JsonUtil.beanToJson(tomcat);
	}

	/**
	 * tomcat请求方法统计
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/tomcatRequest")
	public String tomcatRequest(HttpServletRequest request,
			@RequestParam(value = "index", defaultValue = "") String index) {
		TomcatModel tomcat = tomcatService.tomcatRequest(index);
		return JsonUtil.beanToJson(tomcat);
	}

	/**
	 * tomcat客户端请求数统计
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/clientRequestCount")
	public String clientRequestCount(HttpServletRequest request,
			@RequestParam(value = "index", defaultValue = "") String index) {
		Page page = new Page();
		page.setDraw(Integer.parseInt(request.getParameter("draw").toString()));
		page.setStart(Integer.parseInt(request.getParameter("start").toString()));
		page.setLength(Integer.parseInt(request.getParameter("length").toString()));

		List<TomcatModel> list = tomcatService.clientRequestCount(index);
		String json = JsonUtil.beanListToJson(list);
		JSONObject jso = new JSONObject();
		jso.put("draw", page.getDraw());
		jso.put("recordsTotal", page.getRecordsTotal());
		jso.put("recordsFiltered", page.getRecordsFiltered());
		jso.put("data", json);
		return jso.toString();
	}

	@ResponseBody
	@RequestMapping(value = "/tomcatRequestType")
	public String tomcatRequestType(HttpServletRequest request,
			@RequestParam(value = "index", defaultValue = "") String index) {
		String requestType = (String) request.getParameter("requestType");
		TomcatModel tomcat = tomcatService.tomcatRequestType(requestType, index);
		return JsonUtil.beanToJson(tomcat);
	}
}
