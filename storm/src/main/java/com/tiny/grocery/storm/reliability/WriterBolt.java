package com.tiny.grocery.storm.reliability;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;

public class WriterBolt implements IRichBolt {

	private static final long serialVersionUID = 5723947855466631031L;
	
	private static final Log log = LogFactory.getLog(WriterBolt.class);

	private OutputCollector collector;
	
	private FileWriter writer;
	
	private int loop;

	@Override
	public void prepare(Map paramMap, TopologyContext paramTopologyContext,
			OutputCollector collector) {
		log.warn("***********WriterBolt.prepare()***********");
		this.collector = collector;
		try {
			writer = new FileWriter(new File("f://a.dat"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void execute(Tuple tuple) {
		log.warn("***********WriterBolt.execute()***********");
		Object str = tuple.getString(0);
		if(loop == 5){
			collector.fail(tuple);
		}else{
			try {
				writer.write(str + "\r\n");
				writer.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			collector.emit(tuple, Arrays.asList(str));
			collector.ack(tuple);
		}
		loop++;
	}

	@Override
	public void cleanup() {
		log.warn("***********WriterBolt.cleanup()***********");
	}

	@Override
	public void declareOutputFields(
			OutputFieldsDeclarer declarer) {
		log.warn("***********WriterBolt.declareOutputFields()***********");
		
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		log.warn("***********WriterBolt.getComponentConfiguration()***********");
		return null;
	}

}
