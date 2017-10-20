package com.osp.log.service;

import java.util.List;

import com.osp.log.model.Page;
import com.osp.log.model.TomcatModel;

public interface TomcatService {

	TomcatModel tomcatTimeSearch(int day,String index);

	TomcatModel tomcatRequest(String index);

	TomcatModel tomcatRequestType(String requestType,String index);

	List<TomcatModel> tomcatRequestAll(Page page,String index,String startDate,String endDate);
	
	List<TomcatModel> errorTomcatRequest(Page page,String index);
	
	List<TomcatModel> clientRequestCount(Page page,String index);
}
