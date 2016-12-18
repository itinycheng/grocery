package com.tiny.grocery

import java.util.Properties

import org.junit.Test

/**
  * Created by tiny on 2016/12/18.
  */
@Test
class AppTest2 {

  @Test
  def test1(): Unit ={
    val Array(name,age) = Array("name",1);
    println(name + " : " + age)
    val prop = new Properties()
    List(("1","2"),("3","4")).foreach(tuple => prop.put(tuple._1,tuple._2))
    println("end")
  }

}
