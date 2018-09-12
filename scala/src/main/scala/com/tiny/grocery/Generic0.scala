package com.tiny.grocery

import scala.reflect.ClassTag
import scala.reflect.runtime.universe._

object Generic0 {
  def main(args: Array[String]): Unit = {
    println(args)
    println(test[User]())
    println(test1(classOf[User]))
    println(this.getClass.getSimpleName)
    println(fun)

    val bool = classOf[List[Int]] == classOf[List[String]]
    val bo = typeOf[List[Int]] == typeOf[List[String]]
  }

  def test1(x: Class[_]): String = {
    x.getSimpleName
  }

  def test2[T: ClassTag](str: String): String = {
    implicitly[ClassTag[T]].runtimeClass.getSimpleName
  }

  def test[T: ClassTag](): String = {
    implicitly[ClassTag[T]].runtimeClass.getSimpleName
  }

  def udfImplicitly[T](implicit e: T): T = e

  implicit val str1: String = "str1"

  def fun(implicit x: String): String = x + "~"

  def print[A](a: A): List[A] = List[A](a)

  class User(name: String, age: Int)

}
