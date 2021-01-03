package com.shursulei.api.impl;

import java.net.InetAddress;

import com.shursulei.api.inter.IElasticSearchClientSV;
import com.shursulei.common.ElasticSearchConstants;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.settings.Settings.Builder;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;


/**
 * ES接口实现类
 * 
 * @author zhangdq
 * @time 2017年9月30日 上午10:51:41
 * @Email qiang900714@126.com
 */
public class ElasticSearchClientImpl implements IElasticSearchClientSV {
	private Logger logger = LoggerFactory.getLogger(ElasticSearchClientImpl.class);

	// TCP地址
	private String hosts;

	// 索引名称
	private String indexName;
	
	// 文档
	private String type;

	// 文档ID
	private String _id;

	// 创建ES对象
	private TransportClient transportClient;

	// 默认构建函数
	public ElasticSearchClientImpl() {
	}

	/**
	 * 设置ES基本参数
	 * 
	 * @param hosts
	 * @param indexName
	 * @param _id
	 */
	public ElasticSearchClientImpl(String hosts, String indexName, String type, String _id) {
		this.hosts = hosts;
		this.indexName = indexName;
		this.type = type;
		this._id = _id;
		initESClient();
	}

	/**
	 * 初始化ES客户端
	 * 
	 * @author zhangdq
	 * @time 2017年9月30日 上午11:16:16
	 * @Email qiang900714@126.com
	 */
	public void initESClient() {
		try {
			if (transportClient == null) {
				Builder builder = Settings.settingsBuilder();

				// 设置集群的名称
				builder.put("cluster.name", ElasticSearchConstants.CLUSTERNAME);

				// 自动嗅探整个集群的状态
				builder.put("tclient.transport.sniff", true);

				Settings settings = builder.build();
				transportClient = TransportClient.builder().settings(settings).build();

				String[] nodes = hosts.split(",");
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
			throw new RuntimeException("client init error : {}", e);
		}
	}

	/**
	 * 取得ES客户端实例
	 * 
	 * @author zhangdq
	 * @time 2017年9月30日 上午11:16:47
	 * @Email qiang900714@126.com
	 * @return
	 */
	public synchronized TransportClient getTransportClient() {
		return transportClient;
	}

//	public boolean insert(String json) {
//		if (StringUtils.isEmpty(json)) {
//			return false;
//		}
//		IndexResponse response = null;
//		try {
//			response = transportClient.prepareIndex(indexName, indexName, _id).setOpType(IndexRequest.OpType.CREATE).setSource(json).setRefresh(true).get();
//		} catch (Exception e) {
//			logger.error("文档插入异常：" + e);
//			throw new RuntimeException("client init error : {}", e);
//		}
//		if (null != response && response.isCreated()) {
//			return true;
//		} else {
//			throw new RuntimeException("文档{" + json + "}插入异常  : " + response.toString());
//		}
//	}
	
	@Override
	public boolean insert(String json) {
		if(StringUtils.isEmpty(json)){
			return false;
		}
		IndexResponse indexResponse = null;
		try {
			transportClient.prepareBulk();
			indexResponse = transportClient.prepareIndex().setIndex(indexName).setType(type).setId(_id).setSource(json).get();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("文档插入异常：" + e);
		}
		if (null != indexResponse && indexResponse.isCreated()) {
			return true;
		} else {
			throw new RuntimeException("文档{" + json + "}插入异常  : " + indexResponse.toString());
		}
	}

	@Override
	public long typesCount() {
		return transportClient.prepareSearch(indexName).get().getHits().getTotalHits();
	}
	
	public void search() {
		SearchResponse searchResponse = transportClient.prepareSearch(indexName).setTypes(type).get(); // 查询所有
				// .setQuery(QueryBuilders.matchQuery("name",
				// "tom").operator(Operator.AND)) //根据tom分词查询name,默认or
				// .setQuery(QueryBuilders.multiMatchQuery("tom", "name",
				// "age")) //指定查询的字段
				// .setQuery(QueryBuilders.queryString("name:to* AND age:[0 TO
				// 19]")) //根据条件查询,支持通配符大于等于0小于等于19
				// .setQuery(QueryBuilders.termQuery("name", "tom"))//查询时不分词
//				.setSearchType(SearchType.QUERY_THEN_FETCH).setFrom(0).setSize(10)// 分页
//				.addSort("age", SortOrder.DESC)// 排序
//				.get();

		SearchHits hits = searchResponse.getHits();
		long total = hits.getTotalHits();
		System.out.println(total);
		SearchHit[] searchHits = hits.hits();
		for (SearchHit s : searchHits) {
			System.out.println(s.getSourceAsString());
		}
	}
	
	public void deleteType(){
		transportClient.prepareDelete().setIndex(indexName).setType(type).setId(_id).get();
	}
}
