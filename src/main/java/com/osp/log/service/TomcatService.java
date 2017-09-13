package com.osp.log.service;

import java.util.List;

import com.osp.log.model.Page;
import com.osp.log.model.TomcatModel;


public interface TomcatService {

	public TomcatModel tomcatTimeSearch(int day);
	
	public TomcatModel tomcatRequest();
	
	public TomcatModel tomcatRequestType(String type);

	public List<TomcatModel> tomcatRequestAll(Page page);
}
