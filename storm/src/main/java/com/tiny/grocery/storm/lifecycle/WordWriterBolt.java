package com.tiny.grocery.storm.lifecycle;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

public class WordWriterBolt extends BaseRichBolt{

	private static final long serialVersionUID = 4692290849932803527L;
	
	private static final Log log = LogFactory.getLog(WordWriterBolt.class);

	private Writer writer;
	@Override
	public void prepare(Map params, TopologyContext paramTopologyContext,
			OutputCollector collector) {
		log.warn("*********WordWriterBolt.prepare()**********");
		try {
			writer = new FileWriter(new File(params.get("OUTPUT_PATH").toString() + this.toString()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void execute(Tuple tuple) {
		log.warn("*********WordWriterBolt.execute()**********");
		try {
			writer.write(tuple.getStringByField("upper"));
			writer.write("\n");
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void declareOutputFields(
			OutputFieldsDeclarer paramOutputFieldsDeclarer) {
		log.warn("*********WordWriterBolt.declareOutputFields()**********");
		
	}

}
