package com.osp.log.dao;

import java.util.List;

import com.osp.log.model.DateInterval;
import com.osp.log.model.LoginLogModel;
import com.osp.log.model.OperationLogModel;
import com.osp.log.model.Page;

public interface LogsDao {
	public List<LoginLogModel> selectLoginLogList(Page page, String systemId, String username,
			DateInterval dateInterval);

	public List<OperationLogModel> selectOperationList(Page page, String systemId, DateInterval dateInterval);
}
