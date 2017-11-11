package com.osp.log.service;

import java.util.List;

import com.osp.log.model.LoginLogModel;
import com.osp.log.model.OperationLogModel;
import com.osp.log.model.Page;

public interface LogsService {
	  public List<LoginLogModel> selectLoginLogList(Page page,String systemId,String username,String startDate,String endDate);
	  public List<OperationLogModel> selectOperationList(Page page,String systemId,String startDate,String endDate);
}
