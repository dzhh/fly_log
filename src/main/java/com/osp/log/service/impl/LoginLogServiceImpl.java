package com.osp.log.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.osp.log.dao.LoginLogDao;
import com.osp.log.model.LoginLog;
import com.osp.log.model.Page;
import com.osp.log.service.LoginLogService;

@Service
public class LoginLogServiceImpl implements LoginLogService {

	@Autowired
	private LoginLogDao loginLogDao;
	
	@Override
	public List<LoginLog> selectLoginLogList(Page page,String systemId,String username,String startDate,String endDate) {
		return loginLogDao.selectLoginLogList(page,systemId,username,startDate,endDate);
	}

}
