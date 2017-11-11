package com.osp.log.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.osp.common.json.JsonUtil;
import com.osp.log.model.LoginLogModel;
import com.osp.log.model.OperationLogModel;
import com.osp.log.model.Page;
import com.osp.log.service.LogsService;
import com.osp.log.util.DateUtil;

import net.sf.json.JSONObject;

/**
 * 登录日志 2017-11-07
 * 
 * @author zhangmingcheng
 */
@Controller
public class LogsController {

	@Autowired
	private LogsService loginLogService;

	/**
	 * 登录日志
	 * 
	 * @param request
	 * @param systemId
	 * @param username
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/loginLog")
	public String loginLog(HttpServletRequest request,
			@RequestParam(value = "systemId", defaultValue = "") String systemId,
			@RequestParam(value = "username", defaultValue = "") String username,
			@RequestParam(value = "startdate", defaultValue = "") String startDate,
			@RequestParam(value = "enddate", defaultValue = "") String endDate) {
		if (startDate.isEmpty() == true) {
			startDate = "2000-01-01";
			endDate = DateUtil.getDate();
		} else {
			// 转换下时间格式 默认yyyyMMdd 转成 yyyy-MM-dd
			startDate = startDate.substring(0, 4) + "-" + startDate.substring(4, 6) + "-" + startDate.substring(6, 8);
			endDate = endDate.substring(0, 4) + "-" + endDate.substring(4, 6) + "-" + endDate.substring(6, 8);
		}
		Page page = new Page();
		page.setDraw(Integer.parseInt(request.getParameter("draw").toString()));
		page.setStart(Integer.parseInt(request.getParameter("start").toString()));
		page.setLength(Integer.parseInt(request.getParameter("length").toString()));

		System.out.println("loginLog draw=" + page.getDraw() + " start=" + page.getStart() + " length="
				+ page.getLength() + " systemId=" + systemId + " username=" + username + " startdate=" + startDate
				+ " enddate=" + endDate);

		List<LoginLogModel> list = loginLogService.selectLoginLogList(page, systemId, username, startDate, endDate);
		JSONObject jso = new JSONObject();
		if (list != null) {
			jso.put("data", JsonUtil.beanListToJson(list));
			jso.put("recordsTotal", page.getRecordsTotal());
			jso.put("recordsFiltered", page.getRecordsFiltered());
		} else {
			jso.put("data", "");
		}
		jso.put("draw", page.getDraw());

		return jso.toString();
	}

	@ResponseBody
	@RequestMapping(value = "/operationLog")
	public String operationLog(HttpServletRequest request,
			@RequestParam(value = "systemId", defaultValue = "") String systemId,
			@RequestParam(value = "startdate", defaultValue = "") String startDate,
			@RequestParam(value = "enddate", defaultValue = "") String endDate) {
		if (startDate.isEmpty() == true) {
			startDate = "2000-01-01";
			endDate = DateUtil.getDate();
		} else {
			// 转换下时间格式 默认yyyyMMdd 转成 yyyy-MM-dd
			startDate = startDate.substring(0, 4) + "-" + startDate.substring(4, 6) + "-" + startDate.substring(6, 8);
			endDate = endDate.substring(0, 4) + "-" + endDate.substring(4, 6) + "-" + endDate.substring(6, 8);
		}
		Page page = new Page();
		page.setDraw(Integer.parseInt(request.getParameter("draw").toString()));
		page.setStart(Integer.parseInt(request.getParameter("start").toString()));
		page.setLength(Integer.parseInt(request.getParameter("length").toString()));

		System.out.println("operationLog draw=" + page.getDraw() + " start=" + page.getStart() + " length="
				+ page.getLength() + " systemId=" + systemId + " startdate=" + startDate + " enddate=" + endDate);

		List<OperationLogModel> list = loginLogService.selectOperationList(page, systemId, startDate, endDate);
		JSONObject jso = new JSONObject();
		if (list != null) {
			jso.put("data", JsonUtil.beanListToJson(list));
			jso.put("recordsTotal", page.getRecordsTotal());
			jso.put("recordsFiltered", page.getRecordsFiltered());
		} else {
			jso.put("data", "");
		}
		jso.put("draw", page.getDraw());

		return jso.toString();
	}
}
