package com.shursulei.storm;

import java.util.Map;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichSpout;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

public class TestSpout extends BaseRichBolt implements IRichSpout {
	/**
	* 
	*/
	private static final long serialVersionUID = -7907286793782167628L;
	private SpoutOutputCollector collector;
	private static final String field = "word";
	private int count = 1;
	private String[] message = { "My nickname is xuwujing", "My blog address is http://www.panchengming.com/",
			"My interest is playing games" };

	/**
	 * open()方法中是在ISpout接口中定义，在Spout组件初始化时被调用。 有三个参数:
	 * 1.Storm配置的Map;
	 * 2.topology中组件的信息;
	 * 3.发射tuple的方法;
	 */
	public void open(Map map, TopologyContext arg1, SpoutOutputCollector collector) {
		System.out.println("open:" + map.get("test"));
		this.collector = collector;
	}

	/**
	 * nextTuple()方法是Spout实现的核心。 也就是主要执行方法，用于输出信息,通过collector.emit方法发射。
	 */
	public void nextTuple() {
		if (count <= message.length) {
			System.out.println("第" + count + "次开始发送数据...");
			this.collector.emit(new Values(message[count - 1]));
		}
		count++;
	}

	/**
	 * declareOutputFields是在IComponent接口中定义，用于声明数据格式。 即输出的一个Tuple中，包含几个字段。
	 */
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		System.out.println("定义格式...");
		declarer.declare(new Fields(field));
	}

	/**
	    *   当一个Tuple处理成功时，会调用这个方法
	 * 表示Tuple处理成功 
	 */
	public void ack(Object obj) {
		System.out.println("ack:" + obj);
	}

	/**
	    *  当Topology停止时，会调用这个方法
	 */
	public void close() {
		System.out.println("关闭...");
	}

	/**
	 * 当一个Tuple处理失败时，会调用这个方法
	 * 表示Tuple处理失败
	 */
	public void fail(Object obj) {
		System.out.println("失败:" + obj);
	}

	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {

	}

	public void execute(Tuple input) {

	}

	public void activate() {
		
	}

	public void deactivate() {
		
	}
}
