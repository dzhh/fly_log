package com.osp.log.service;

import java.util.List;

import com.osp.log.model.Page;
import com.osp.log.model.TomcatModel;

public interface TomcatService {

	TomcatModel tomcatTimeSearch(int day,String index,String startDate,String endDate);

	TomcatModel tomcatRequest(String index,String startDate,String endDate);

	TomcatModel tomcatRequestType(String requestType,String index);

	List<TomcatModel> tomcatRequestAll(Page page,String index,String startDate,String endDate);
	
	List<TomcatModel> errorTomcatRequest(Page page,String index,String startDate, String endDate);
	
	List<TomcatModel> clientRequestCount(Page page,String index,String startDate,String endDate);
}
