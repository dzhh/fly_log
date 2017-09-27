package com.osp.log.service;

import java.util.List;

import com.osp.log.model.Page;
import com.osp.log.model.TomcatModel;

public interface TomcatService {

	TomcatModel tomcatTimeSearch(int day);

	TomcatModel tomcatRequest();

	TomcatModel tomcatRequestType(String type);

	List<TomcatModel> tomcatRequestAll(Page page);
	
	List<TomcatModel> errorTomcatRequest(Page page);
	
	List<TomcatModel> clientRequestCount();
}
