package com.tiny.grocery

import java.util.concurrent.LinkedBlockingQueue

import org.junit.Test

/**
  * Created by 16072453 on 2016/10/31.
  */
class StreamTest {

  @Test
  def testContinually() = {

    val queue = new LinkedBlockingQueue[String](10000);
    for (i <- 1 to 1000) {
      queue.put(i + "fs" + i)
    }
    Stream.continually(queue.poll()).takeWhile(item => item != null && !item.isEmpty).foreach {
      curItem =>
        println(curItem)
    }
  }

}
