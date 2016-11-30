package com.tiny.grocery.storm.lifecycle;

import java.util.Arrays;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;

public class WordTransferBolt extends BaseRichBolt {

	private static final long serialVersionUID = 6824664706035860397L;
	
	private static final Log log = LogFactory.getLog(WordTransferBolt.class);
	
	private OutputCollector collector;

	@Override
	public void execute(Tuple tuple) {
		log.warn("*********WordTransferBolt.execute()**********");
		Object str = tuple.getStringByField("word2").toUpperCase();
		collector.emit(Arrays.asList(str));
	}

	@Override
	public void prepare(Map paramMap, TopologyContext paramTopologyContext,
			OutputCollector collector) {
		log.warn("*********WordTransferBolt.prepare()**********");
		this.collector = collector;
	}

	@Override
	public void declareOutputFields(
			OutputFieldsDeclarer declarer) {
		log.warn("*********WordTransferBolt.declareOutputFields()**********");
		declarer.declare(new Fields("upper"));
	}

}
