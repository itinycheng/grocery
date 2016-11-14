package com.tiny.grocery

/**
  * Created by 16072453 on 2016/9/27.
  */
object MethodTest {

  def test(a: String = "a", b: Boolean = true): String = {
    println(a + " : " + b)
    "aa"
  }

  def test2 = test(_: String, false)

  def x = test()

  def main(args: Array[String]): Unit = {
    test(a = "tes", b = false)
    test(b = false, a = "tes")
    test("tes", false)
    test("tes")
    //test(false)
    test()
    def a = test2("d")
    println(a)
    println(test2)
    println(x)
  }

}
