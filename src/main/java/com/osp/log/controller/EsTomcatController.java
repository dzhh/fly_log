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
import com.osp.log.util.DateUtil;

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
			@RequestParam(value = "index", defaultValue = "") String index,
			@RequestParam(value = "startdate", defaultValue = "") String startDate,
			@RequestParam(value = "enddate", defaultValue = "") String endDate) {
		System.out.println("tomcatRequestAll index=" + index+" startDate="+startDate+" endDate="+endDate);
		Page page = new Page();
		page.setDraw(Integer.parseInt(request.getParameter("draw").toString()));
		page.setStart(Integer.parseInt(request.getParameter("start").toString()));
		page.setLength(Integer.parseInt(request.getParameter("length").toString()));

		List<TomcatModel> list = tomcatService.tomcatRequestAll(page, index,startDate,endDate);
		JSONObject jso = new JSONObject();
		if(list!=null){
			jso.put("data", JsonUtil.beanListToJson(list));
		}else{
			jso.put("data", "");
		}
		jso.put("draw", page.getDraw());
		jso.put("recordsTotal", page.getRecordsTotal());
		jso.put("recordsFiltered", page.getRecordsFiltered());
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
			@RequestParam(value = "index", defaultValue = "") String index,
			@RequestParam(value = "startdate", defaultValue = "") String startDate,
			@RequestParam(value = "enddate", defaultValue = "") String endDate) {
		System.out.println("errorTomcatRequest index=" + index+" startDate="+startDate+" endDate="+endDate);
		Page page = new Page();
		page.setDraw(Integer.parseInt(request.getParameter("draw").toString()));
		page.setStart(Integer.parseInt(request.getParameter("start").toString()));
		page.setLength(Integer.parseInt(request.getParameter("length").toString()));

		List<TomcatModel> list = tomcatService.errorTomcatRequest(page, index,startDate,endDate);
		JSONObject jso = new JSONObject();
		if(list!=null){
			jso.put("data", JsonUtil.beanListToJson(list));
		}else{
			jso.put("data", "");
		}
		jso.put("draw", page.getDraw());
		jso.put("recordsTotal", page.getRecordsTotal());
		jso.put("recordsFiltered", page.getRecordsFiltered());
		return jso.toString();
	}

	/**
	 * 获取最近n天tomcat请求数 n默认为12
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/tomcatRequestDate")
	public String tomcatTimeSearch(HttpServletRequest request,
			@RequestParam(value = "day", defaultValue = "12") Integer day,
			@RequestParam(value = "index", defaultValue = "") String index,
			@RequestParam(value = "startdate", defaultValue = "") String startDate,
			@RequestParam(value = "enddate", defaultValue = "") String endDate) {
		//默认为20天
		if(startDate.isEmpty()==true){
			startDate = DateUtil.getPastDate(day);
			endDate =  DateUtil.getDate();
		}else{
			//转换下时间格式 默认yyyyMMdd 转成 yyyy-MM-dd
			startDate = startDate.substring(0, 4)+"-"+startDate.substring(4,6)+"-"+startDate.substring(6,8);
			endDate = endDate.substring(0, 4)+"-"+endDate.substring(4,6)+"-"+endDate.substring(6,8);
		}
		System.out.println("tomcatRequestDate index=" + index+" startDate="+startDate+" endDate="+endDate);
		TomcatModel tomcat = tomcatService.tomcatTimeSearch(day, index,startDate,endDate);
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
		System.out.println("tomcatRequest index=" + index);
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
		System.out.println("clientRequestCount index=" + index);
		Page page = new Page();
		page.setDraw(Integer.parseInt(request.getParameter("draw").toString()));
		page.setStart(Integer.parseInt(request.getParameter("start").toString()));
		page.setLength(Integer.parseInt(request.getParameter("length").toString()));

		List<TomcatModel> list = tomcatService.clientRequestCount(page,index);
		JSONObject jso = new JSONObject();
		if(list!=null){
			jso.put("data", JsonUtil.beanListToJson(list));
		}else{
			jso.put("data", "");
		}
		jso.put("draw", page.getDraw());
		jso.put("recordsTotal", page.getRecordsTotal());
		jso.put("recordsFiltered", page.getRecordsFiltered());
		return jso.toString();
	}	

	@ResponseBody
	@RequestMapping(value = "/tomcatRequestType")
	public String tomcatRequestType(HttpServletRequest request,
			@RequestParam(value = "index", defaultValue = "") String index) {
		System.out.println("tomcatRequestType index=" + index);
		String requestType = (String) request.getParameter("requestType");
		TomcatModel tomcat = tomcatService.tomcatRequestType(requestType, index);
		return JsonUtil.beanToJson(tomcat);
	}
}
