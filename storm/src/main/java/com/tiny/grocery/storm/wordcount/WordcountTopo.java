package com.tiny.grocery.storm.wordcount;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;

public class WordcountTopo {

	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("Usage: inputPath timeOffset");
			System.err.println("such as : java -jar  WordCount.jar 2");
			System.exit(2);
		}
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("reader", new WordSpout());
		builder.setBolt("spliter", new WordSpliterBolt()).shuffleGrouping("reader");
		builder.setBolt("counter", new WordCounterBolt()).shuffleGrouping("spliter");
		Config conf = new Config();
		conf.put("TIME_OFFSET", args[0]);
		conf.setDebug(false);
		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("wordcount", conf, builder.createTopology());
	}
}
