package com.osp.log.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.osp.log.dao.LogsDao;
import com.osp.log.model.DateInterval;
import com.osp.log.model.LoginLogModel;
import com.osp.log.model.OperationLogModel;
import com.osp.log.model.Page;
import com.osp.log.service.LogsService;
import com.osp.log.util.RegexUtil;

@Service
public class LogsServiceImpl implements LogsService {

	@Autowired
	private LogsDao loginLogDao;

	@Override
	public List<LoginLogModel> selectLoginLogList(Page page, String systemId, String username, String startDate,
			String endDate) {
		DateInterval dateInterval = RegexUtil.getDateInterval(new DateInterval(startDate, endDate), "yyyy-MM-dd");
		dateInterval.setStartDate(dateInterval.getStartDate() + " 00:00:00");
		dateInterval.setEndDate(dateInterval.getEndDate() + " 23:59:59");
		return loginLogDao.selectLoginLogList(page, systemId, username, dateInterval);
	}

	@Override
	public List<OperationLogModel> selectOperationList(Page page, String systemId, String startDate, String endDate) {
		DateInterval dateInterval = RegexUtil.getDateInterval(new DateInterval(startDate, endDate), "yyyy-MM-dd");
		dateInterval.setStartDate(dateInterval.getStartDate() + " 00:00:00");
		dateInterval.setEndDate(dateInterval.getEndDate() + " 23:59:59");
		return loginLogDao.selectOperationList(page, systemId, dateInterval);
	}

}
