package com.tiny.grocery.storm.reliability;

import java.util.Arrays;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;

public class SpliterBolt implements IRichBolt {

	private static final long serialVersionUID = 6705179229253966150L;
	
	private static final Log log = LogFactory.getLog(SpliterBolt.class);

	private OutputCollector collector;
	@Override
	public void prepare(Map paramMap, TopologyContext paramTopologyContext,
			OutputCollector collector) {
		log.warn("***********SpliterBolt.prepare()***********");
		this.collector = collector;
	}

	@Override
	public void execute(Tuple tuple) {
		log.warn("***********SpliterBolt.execute()***********");
		String[] words = tuple.getString(0).split(",");
		for (Object word : words) {
			if(null != word)
				collector.emit(tuple, Arrays.asList(word));
		}
		collector.ack(tuple);
	}

	@Override
	public void cleanup() {
		log.warn("***********SpliterBolt.cleanup()***********");
		
	}

	@Override
	public void declareOutputFields(
			OutputFieldsDeclarer declarer) {
		log.warn("***********SpliterBolt.declareOutputFields()***********");
		declarer.declare(new Fields("word"));
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		log.warn("***********SpliterBolt.getComponentConfiguration()***********");
		return null;
	}

}
