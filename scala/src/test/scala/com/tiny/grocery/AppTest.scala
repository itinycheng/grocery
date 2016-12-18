package com.tiny.grocery

import org.junit._
import Assert._

@Test
class AppTest {

    @Test
    def test(): Unit ={
        val list = List("1","3");
        val head = fun(list.head)
        println(head)
    }

    @Test
    def test1(): Unit ={
        val f1 = fun1 _
        val f2 = fun1
        println(f1 + " : " + f2)
    }

    @Test
    def test2(): Unit ={
        val f1 = fun2 _
        val f2 = fun2(_)
        val f3 = fun2("r")
        println(f1("f1") + " : " + f2("f2") + " : " + f3)
    }


    def fun[A](x: => A): A ={
        println("a")
        x
    }

    def fun1 ={
        "ds"
    }

    def  fun2(str: String) ={
        str + " - "
    }

}


