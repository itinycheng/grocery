package com.tiny.grocery.storm.reliability;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;


public class TopoMain {

	public static void main(String[] args) throws Exception {
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("spout", new MsgSpout(),1);
		builder.setBolt("bolt-1", new SpliterBolt(),1).shuffleGrouping("spout");
		builder.setBolt("bolt-2", new WriterBolt(),1).shuffleGrouping("bolt-1");
		Config conf = new Config();
		conf.setDebug(false);
		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("reliability", conf, builder.createTopology());
	}
}
