package com.tiny.grocery

import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Seconds, State, StateSpec, StreamingContext}

/**
  * Created by 16072453 on 2016/9/25.
  */
object MapWithState {

  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setAppName("MapWithStateTest").setMaster("local")
    val ssc = new StreamingContext(sparkConf, Seconds(2))
    ssc.checkpoint(".")

    val initRDD: RDD[(String, Int)] = ssc.sparkContext.parallelize(List(("A",1),("B",1)))

    val lines = ssc.socketTextStream("127.0.0.1",9999)
    val wordMap: DStream[(String, Int)] = lines.flatMap(_.split(" ")).map(x => (x,1))

    val mappingFun = (word : String,  one : Option[Int],state : State[Int]) => {
      val sum = one.getOrElse(0) + state.getOption().getOrElse(0)
      state.update(sum)
      (word,sum)
    }

    val stateDStream = wordMap.mapWithState(StateSpec.function(mappingFun).initialState(initRDD))
    stateDStream.print()
    ssc.start()
    ssc.awaitTermination()
  }
}
