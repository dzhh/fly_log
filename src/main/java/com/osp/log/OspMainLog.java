package com.osp.log;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 2017/08/23
 * OSP 日志中心
 * @author fly
 *
 */
@SpringBootApplication
@EnableConfigurationProperties
@RestController
public class OspMainLog {

	public static void main(String[] args) {
		SpringApplication.run(OspMainLog.class, args);
	}
	
	
	@RequestMapping("/t")
	public String home() {
		return "Hello Docker World";
	}
}