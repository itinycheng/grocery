package com.tiny.grocery

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by 16072453 on 2016/9/25.
  */
object WordCount {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    conf.setAppName("wordCount")
    conf.setMaster("local")

    val sc = new SparkContext(conf)
    val lines = sc.textFile("F://test//text.txt", 1)
    val words = lines.flatMap { line => line.split(" ") }
    val pairs = words.map { word => (word, 1) }
    val wordCounts = pairs.reduceByKey(_+_)
    wordCounts.foreach(map => println(map._1 +":"+ map._2))
    sc.stop()
  }
}
