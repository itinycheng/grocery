package com.tiny.grocery.storm.lifecycle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;

public class LifecycleTopo {

	private static final Log log = LogFactory.getLog(LifecycleTopo.class);
	
	public static void main(String[] args) throws Exception {
		if(args == null || args.length != 1){
			System.err.println("Usage: outputPath offset");
			System.err.println("such as : java -jar  storm-test.jar path");
			System.exit(0);
		}
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("spout", new WordRandomSpout(), 2);
		builder.setBolt("transfer", new WordTransferBolt(), 4).shuffleGrouping("spout");
		builder.setBolt("writer", new WordWriterBolt(), 4).fieldsGrouping("transfer", new Fields("upper"));
		Config conf = new Config();
		conf.put("OUTPUT_PATH", args[0]);
		conf.setNumWorkers(2);
		conf.setDebug(true);
		log.warn(("************submitting topology lifecycle***********"));
		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("lifecycle", conf, builder.createTopology());
		log.warn(("************topology lifecycle submitted***********"));
	}

}
