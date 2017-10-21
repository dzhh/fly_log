package com.osp.log.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.exists.types.TypesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.types.TypesExistsResponse;
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
	 * 判断指定的索引名是否存在
	 * 
	 * @param indexName
	 *            索引名
	 * @return 存在：true; 不存在：false;
	 */
	public static boolean isExistsIndex(String indexName) {
		IndicesExistsResponse response = ESUtil.getClient().admin().indices()
				.exists(new IndicesExistsRequest().indices(new String[] { indexName })).actionGet();
		if(response.isExists()==false){
			System.out.println("索引"+indexName+"不存在!");
		}
		return response.isExists();
	}

	/**
	 * 判断指定的索引的类型是否存在
	 * 
	 * @param indexName
	 *            索引名
	 * @param indexType
	 *            索引类型
	 * @return 存在：true; 不存在：false;
	 */
	public static boolean isExistsType(String indexName, String indexType) {
		TypesExistsResponse response = ESUtil.getClient().admin().indices()
				.typesExists(new TypesExistsRequest(new String[] { indexName }, indexType)).actionGet();
		if(response.isExists()==false){
			System.out.println("索引"+indexName+"的类型"+indexType+"不存在!");
		}
		return response.isExists();
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
