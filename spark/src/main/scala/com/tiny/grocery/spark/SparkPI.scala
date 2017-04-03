package com.tiny.grocery.spark

import org.apache.spark.sql.SparkSession

import scala.math._

/**
  * Created by 16072453 on 2016/9/25.
  */
object SparkPI {
  def main(args: Array[String]) {
    val args = Array("2")
    val spark = SparkSession
      .builder
      .master("local")
      .appName("Spark Pi")
      .getOrCreate()
    val slices = if (args.length > 0) args(0).toInt else 2
    val n = math.min(100000L * slices, Int.MaxValue).toInt // avoid overflow
    val count = spark.sparkContext.parallelize(1 until n, slices).map { i =>
      val x = random * 2 - 1
      val y = random * 2 - 1
      if (x*x + y*y < 1) 1 else 0
    }.reduce(_ + _)
    println("Pi is roughly " + 4.0 * count / (n - 1))
    spark.stop()
  }

}
