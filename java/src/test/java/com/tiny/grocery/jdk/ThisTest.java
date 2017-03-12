package com.tiny.grocery.jdk;

import org.junit.Test;

/**
 * Created by tiny on 2017/3/11.
 */
public class ThisTest {

    @Test
    public void test(){
        new Child().print();
    }

    public static class Child extends Parent{
        public void print(){
            super.print();
            System.out.println("child,  " + this);
        }
    }
    public static class Parent{
        public void print(){
            System.out.println("parent, " + this);
        }
    }
}
