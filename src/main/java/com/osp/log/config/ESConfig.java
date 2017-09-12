package com.osp.log.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 
 * @author fly
 *
 */
//@Component // @ConfigurationProperties 取消了locations
//@ConfigurationProperties(prefix = "es")
//@PropertySource("classpath:elastic.properties")
public class ESConfig {

	public static String ES_CLUSTERNAME = "osp";
	public static String ES_IP = "192.168.206.129";
	public static int ES_PORT = 9300;

}
