package com.osp.log.service;

import java.util.List;

import com.osp.log.model.LoginLog;
import com.osp.log.model.Page;

public interface LoginLogService {
	  public List<LoginLog> selectLoginLogList(Page page,String systemId,String username,String startDate,String endDate);
}
