package com.osp.log;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.web.bind.annotation.RestController;

/**
 * 2017/08/23 OSP 日志中心
 * 
 * @author fly
 *
 */
@SpringBootApplication
@EnableConfigurationProperties
@RestController
// @EnableDiscoveryClient
@EnableFeignClients
public class OspMainLog {

	public static void main(String[] args) {
		SpringApplication.run(OspMainLog.class, args);
	}
}
