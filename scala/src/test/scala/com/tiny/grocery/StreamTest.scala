package com.tiny.grocery

import java.util.concurrent.LinkedBlockingQueue

import org.junit.Test

import scala.collection.immutable.Stream

/**
  * Created by 16072453 on 2016/10/31.
  */
class StreamTest {

  @Test
  def testContinually() = {

    val queue = new LinkedBlockingQueue[String](10000000);
    for (i <- 1 to 10000000) {
      queue.put(i + "fs" + i)
    }
    Stream.continually(queue.poll()).takeWhile(item => item != null && !item.isEmpty).foreach {
      curItem =>
        Thread.sleep(2);
        queue.offer(curItem + "d")
        if(curItem.contains("10000000"))
          println("end")
    }
  }

}
