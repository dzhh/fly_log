package com.osp.log.service;

import com.osp.log.model.TomcatModel;


public interface TomcatService {

	public TomcatModel tomcatTimeSearch(int day);
	
	public TomcatModel tomcatRequest();
	
	public TomcatModel tomcatRequestType(String type);
}
