package com.shursulei.api;

import java.net.InetAddress;

import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.settings.Settings.Builder;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ES基本信息设置获取
 * 
 * @author zhangdq
 * @time 2017年9月29日 下午10:44:30
 * @Email qiang900714@126.com
 */
public class ElasticSearch {
	private static Logger logger = LoggerFactory.getLogger(ElasticSearch.class);
	public static TransportClient transportClient = null;
	public static BulkProcessor bulkProcessor = null;
	private static String ESHosts = "127.0.0.1:9300";

	/**
	 * ES客户端获取
	 * 
	 * @author zhangdq
	 * @time 2017年9月29日 下午10:43:42
	 * @Email qiang900714@126.com
	 * @return
	 */
	public static TransportClient getESClient() {
		try {
			if (transportClient == null) {
				Builder builder = Settings.settingsBuilder();
				// 设置集群的名称
				builder.put("cluster.name", "elasticsearch");
				// 自动嗅探整个集群的状态
				builder.put("tclient.transport.sniff", true);
				Settings settings = builder.build();
				transportClient = TransportClient.builder().settings(settings).build();
				String[] nodes = ESHosts.split(",");
				for (String node : nodes) {
					// 跳过为空的node（当开头、结尾有逗号或多个连续逗号时会出现空node）
					if (node.length() > 0) {
						String[] hostPort = node.split(":");
						transportClient.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(hostPort[0]), Integer.parseInt(hostPort[1])));
					}
				}
			}
		} catch (Exception e) {
			logger.error("ES客户端初始化异常：{}", e);
			e.printStackTrace();
		}
		return transportClient;
	}

	/**
	 * 自动提交文档设置
	 * 
	 * @author zhangdq
	 * @time 2017年9月29日 下午10:43:24
	 * @Email qiang900714@126.com
	 * @return
	 */
	public static BulkProcessor getBulkPorcessor() {
		if (bulkProcessor == null) {
			try {
				bulkProcessor = BulkProcessor.builder(getESClient(), new BulkProcessor.Listener() {
					@Override
					public void beforeBulk(long executionId, BulkRequest request) {
						logger.info("有文档在提交……");
					}
					@Override
					public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
						logger.error("文档提交失败！{}", failure);
					}
					@Override
					public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
						logger.info("提交" + response.getItems().length + "个文档，用时" + response.getTookInMillis() + "MS" + (response.hasFailures() ? " 有文档提交失败！" : ""));

					}
				}).setBulkActions(1000)										// 批量执行-文档数量达到1000时执行
				  .setBulkSize(new ByteSizeValue(5, ByteSizeUnit.MB))		// 批量执行-文档体积达到5M时执行
				  .setFlushInterval(TimeValue.timeValueSeconds(5L))			// 每5s执行一次
				  .setConcurrentRequests(1)									// 加1后为可并行的提交请求数，即设为0代表只可1个请求并行，设为1为2个并行
				  .build();
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("文档批量执行异常：" + e);
			}
		}
		return bulkProcessor;
	}
}
