package com.tiny.grocery.storm.wordcount;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

public class WordCounterBolt extends BaseRichBolt {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4120398913077702804L;

	private ConcurrentHashMap<Object, AtomicLong> temp = new ConcurrentHashMap<Object, AtomicLong>();
	@Override
	public void execute(Tuple tuple) {
		String str = tuple.getString(0);
		if(temp.containsKey(str)) {
			long addAndGet = temp.get(str).addAndGet(1);
		} else
			temp.put(str, new AtomicLong(1));
	}

	@Override
	public void prepare(Map arg0, TopologyContext arg1, OutputCollector arg2) {
		final int i = Integer.parseInt(arg0.get("TIME_OFFSET").toString());
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					for (Entry<Object, AtomicLong> entry : WordCounterBolt.this.temp.entrySet()) {
						System.out.println(entry.getKey() + " : " + entry.getValue().toString());
					}
					System.out.println("-----------------------------------");
					try {
						Thread.sleep(i * 1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer arg0) {
		
	}

}
