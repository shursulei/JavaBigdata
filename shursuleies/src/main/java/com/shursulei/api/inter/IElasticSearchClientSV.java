package com.shursulei.api.inter;

/**
 * ES接口类
 * 
 * @author zhangdq
 * @time 2017年9月30日 上午10:50:13
 * @Email qiang900714@126.com
 */
public interface IElasticSearchClientSV {
	
	/**
	 * 新增文档
	 * 
	 * @author zhangdq
	 * @time 2017年9月30日 下午2:18:15
	 * @Email qiang900714@126.com
	 * @param json
	 * @return
	 */
	boolean insert(String json);
	
	/**
	 * 查询文档总数
	 * 
	 * @author zhangdq
	 * @time 2017年9月30日 下午2:48:52
	 * @Email qiang900714@126.com
	 * @return
	 */
	long typesCount();
	
	/**
	 * 查询
	 * 
	 * @author zhangdq
	 * @time 2017年9月30日 下午3:51:34
	 * @Email qiang900714@126.com
	 */
	void search();
	
	/**
	 * 删除文档
	 * 
	 * @author zhangdq
	 * @time 2017年9月30日 下午4:07:38
	 * @Email qiang900714@126.com
	 */
	void deleteType();
}
