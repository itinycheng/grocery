package com.tiny.grocery.storm.lifecycle;

import java.util.Arrays;
import java.util.Map;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.utils.Utils;

public class WordRandomSpout extends BaseRichSpout{

	private static final long serialVersionUID = 1834774187857096900L;
	
	private static final Log log = LogFactory.getLog(WordRandomSpout.class);
	
	private Object[] words = {"hadoop","spark","storm","kafka"};
	
	private SpoutOutputCollector collector;

	@Override
	public void nextTuple() {
		Utils.sleep(1000);
		log.warn("*********WordRandomSpout.nextTuple()**********");
		Random random = new Random();
		int i = random.nextInt(words.length);
		collector.emit(Arrays.asList(words[i],words[i] + "2"));
	}

	@Override
	public void open(Map paramMap, TopologyContext paramTopologyContext,
			SpoutOutputCollector collector) {
		log.warn("*********WordRandomSpout.open()**********");
		this.collector = collector;
	}

	@Override
	public void declareOutputFields(
			OutputFieldsDeclarer declarer) {
		log.warn("*********WordRandomSpout.declareOutputFields()**********");
		declarer.declare(new Fields("word","word2"));
	}
	
	public void activate() {
		log.warn("*********WordRandomSpout.activate()**********");
	}

	public void deactivate() {
		log.warn("*********WordRandomSpout.deactivate()**********");
	}

}
