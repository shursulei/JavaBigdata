package com.shursulei.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.clover.es.api.impl.ElasticSearchClientImpl;
import com.clover.es.api.inter.IElasticSearchClientSV;
import com.clover.es.common.ElasticSearchConstants;

public class ElasticSearchFactory {
	private static Logger logger = LoggerFactory.getLogger(ElasticSearchFactory.class);

	private ElasticSearchFactory()
	{
		// 禁止实例化
	}
	/**
	 * 获取ES客户端
	 * 
	 * @author zhangdq
	 * @time 2017年9月30日 下午2:25:43
	 * @Email qiang900714@126.com
	 * @return
	 */
	public static IElasticSearchClientSV getElasticSearchClient(String type, String _id){
		logger.info("获取ES客户端");
		return new ElasticSearchClientImpl(ElasticSearchConstants.TCPIPPORT, ElasticSearchConstants.INDEXNAME, type, _id);
	}
}
