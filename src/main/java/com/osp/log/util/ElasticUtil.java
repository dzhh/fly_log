package com.osp.log.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.stereotype.Component;

import com.osp.log.config.ElasticConfig;


/**
 * 
 * @author fly
 *
 */
@Component
public class ElasticUtil {

	@Autowired
	private ElasticConfig elasticConfig;
	
	public TransportClient getClient() {
		TransportClient client = null;
		Settings settings = Settings.builder().put("cluster.name", elasticConfig.getClusterName())
				.put("client.transport.sniff", true).build();
		try {
			client = new PreBuiltTransportClient(settings).addTransportAddress(
					new InetSocketTransportAddress(InetAddress.getByName(elasticConfig.getIp()), 
							Integer.parseInt(elasticConfig.getPort())));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return client;
	}
}
