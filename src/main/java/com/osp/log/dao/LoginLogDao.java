package com.osp.log.dao;

import java.util.List;

import com.osp.log.model.LoginLog;
import com.osp.log.model.Page;

public interface LoginLogDao {
	public List<LoginLog> selectLoginLogList(Page page, String systemId, String username, String startDate,
			String endDate);
}
