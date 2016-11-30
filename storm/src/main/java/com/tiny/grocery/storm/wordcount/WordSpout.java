package com.tiny.grocery.storm.wordcount;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Random;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;

public class WordSpout extends BaseRichSpout{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2052652184384722078L;

	private Object[] arr = {"storm","hadoop","spark","hbase"};
	
	private SpoutOutputCollector collector;
	
	@Override
	public void nextTuple() {
		Random random = new Random();
		int i = random.nextInt(4);
		this.collector.emit(Arrays.asList(arr[i]));
	}

	@Override
	public void open(Map arg0, TopologyContext arg1, SpoutOutputCollector arg2) {
		this.collector = arg2;
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("line"));
	}

}
