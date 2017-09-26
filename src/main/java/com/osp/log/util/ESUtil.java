package com.osp.log.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import com.osp.log.config.ESConfig;

/**
 * 
 * @author fly
 *
 */
public class ESUtil {

	private static TransportClient client; // 默认的客户端

	/**
	 * 获取默认客户端连接
	 * 
	 * @return
	 */
	@SuppressWarnings("resource")
	public static TransportClient getClient() {
		if (client == null) {
			Settings settings = Settings.builder().put("cluster.name", ESConfig.ES_CLUSTERNAME)
					.put("client.transport.sniff", true).build();
			try {
				client = new PreBuiltTransportClient(settings).addTransportAddress(
						new InetSocketTransportAddress(InetAddress.getByName(ESConfig.ES_IP), ESConfig.ES_PORT));
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}
		return client;
	}

	/**
	 * 关闭连接
	 */
	public void close() {
		if (client != null) {
			client.close();
		}
	}
}
