package com.tiny.grocery

import org.junit.Test

/**
  * Created by tiny on 2016/12/18.
  */
@Test
class ListTest {

  @Test
  def test1(): Unit ={
    val list = List(1,2,3)
    val res = list.foldLeft(1)((_+_))
    val pairs = list.map((_,"e"))
    println(pairs)
    println(res)
  }
}
