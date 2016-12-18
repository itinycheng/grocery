package com.tiny.grocery

/**
 * Created by wangchenglong on 2016/3/2.
 */
class Person {
  println("class start")
  def test() ={
    println("person")
  }
}

object Person{
  println("obj start~~")
  def test() = {
    println("object")
  }
}

object Te{
  def main(args: Array[String]) {
    val p = new Person
    p.test()
    Person.test()
    Person.test()
  }
}