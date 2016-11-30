package com.tiny.grocery.storm.wordcount;

import java.util.Arrays;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;

public class WordSpliterBolt extends BaseRichBolt {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9178390770697712497L;
	
	private OutputCollector collector;

	@Override
	public void execute(Tuple tuple) {
		String str = tuple.getString(0);
		String[] words = str.split(",");
		for (String word : words) {
			if(StringUtils.isNotEmpty(word)){
				word = word.toUpperCase();
				collector.emit(Arrays.asList((Object)word));
			}
		}
		
	}

	@Override
	public void prepare(Map arg0, TopologyContext arg1, OutputCollector arg2) {
		this.collector = arg2;
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("word"));
	}

}
