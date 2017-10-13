package com.osp.log.service;

import java.util.List;

import com.osp.log.model.Page;
import com.osp.log.model.SqlModel;

/**
 * 2017-10-13
 * @author zhangmingcheng
 */
public interface SqlService {
	List<SqlModel> getSqlLogs(Page page,String index,String type);
}
