package com.osp.log.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.osp.log.feign.HelloRemote;
/**
 * 
 * @author fly
 *
 */
@RestController
public class ConsumerController {

	@Autowired
	HelloRemote HelloRemote;

	@RequestMapping("/hello/{name}")
	public String index(@PathVariable("name") String name) {
	    return HelloRemote.hello(name);
	}
}
