package com.shursulei.api;

import java.util.Random;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.clover.es.api.inter.IElasticSearchClientSV;

public class ElasticSearchOpt {
	public static Logger logger = LoggerFactory.getLogger(ElasticSearchOpt.class);

	public static void insert(String json) {
		try {
			TransportClient client = ElasticSearch.getESClient();
			client.prepareBulk();
			IndexResponse indexResponse = client.prepareIndex().setIndex("chm_online").setType("article1").setId(String.valueOf(System.currentTimeMillis())).setSource(json).get();
			System.out.println(indexResponse.getVersion());
			client.close();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("文档插入异常：" + e);
		}
	}
	
	public static void main(String[] args) {
//		Article article = new Article();
//		article.setId(new Random().nextInt(1000));
//		article.setTitle("chm标题测试");
//		article.setContent("curl http://10.0.2.15:9200/chm_online/_analyze?pretty=true -d '{\"text\":\"北京大学\"}'");
//		insert(JSON.toJSONString(article));
//		IElasticSearchClientSV client = ElasticSearchFactory.getElasticSearchClient("article", "CHM$ARTICLE$" + article.getId());
//		client.insert(JSON.toJSONString(article));
		IElasticSearchClientSV client = ElasticSearchFactory.getElasticSearchClient("article1", "100");
		client.deleteType();
	}
}
