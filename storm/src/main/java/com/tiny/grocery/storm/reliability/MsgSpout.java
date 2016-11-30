package com.tiny.grocery.storm.reliability;

import java.util.Arrays;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;

public class MsgSpout implements IRichSpout{

	private static final long serialVersionUID = -1555290483934077898L;
	
	private static final Log log = LogFactory.getLog(MsgSpout.class);
	
	private Object[] nums = {"0,zero",
	                         "1,one",
	                         "2,two",
	                         "3,three",
	                         "4,four",
	                         "5,five",
	                         "6,six",
	                         "7,seven",
	                         "8,eight",
	                         "9,nine"};
	
	private SpoutOutputCollector collector;
	
	private int offset;
	
	@Override
	public void open(Map paramMap, TopologyContext paramTopologyContext,
			SpoutOutputCollector collector) {
		log.warn("***********MsgSpout.open()***********");
		this.collector = collector;
	}

	@Override
	public void close() {
		log.warn("***********MsgSpout.close()***********");
	}

	@Override
	public void activate() {
		log.warn("***********MsgSpout.activate()***********");
	}

	@Override
	public void deactivate() {
		log.warn("***********MsgSpout.deactivate()***********");
	}

	@Override
	public void nextTuple() {
		log.warn("***********MsgSpout.nextTuple()***********");
		while (this.offset < nums.length - 1) {
			collector.emit(Arrays.asList(nums[offset]), (Object) offset);
			offset++;
		}
	}

	@Override
	public void ack(Object msgId) {
		log.warn("***********MsgSpout.ack()***********");
	}

	@Override
	public void fail(Object msgId) {
		log.warn("***********MsgSpout.fail()***********");
		collector.emit(Arrays.asList(nums[(Integer)msgId]), msgId);
	}

	@Override
	public void declareOutputFields(
			OutputFieldsDeclarer declarer) {
		log.warn("***********MsgSpout.declareOutputFields()***********");
		declarer.declare(new Fields("line"));
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		log.warn("***********MsgSpout.getComponentConfiguration()***********");
		return null;
	}

}
