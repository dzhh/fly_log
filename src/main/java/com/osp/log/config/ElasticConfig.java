package com.osp.log.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 
 * @author fly
 *
 */
@Component // @ConfigurationProperties 取消了locations
@ConfigurationProperties(prefix = "es")
@PropertySource("classpath:elastic.properties")
public class ElasticConfig {

	private String clusterName;
	private String ip;
	private String port;

	public String getClusterName() {
		return clusterName;
	}

	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}
}
