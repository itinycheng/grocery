package com.tiny.grocery.jdk;

import org.junit.Test;

/**
 * Created by tiny on 2016/11/17.
 */
public class GramTest {

    @Test
    public void test0() {
        System.out.println(_test0_0());
        System.out.println("------------------");
        System.out.println(_test0_1());
    }

    private String _test0_0() {
        try {
            System.out.println("try block");
            return "try";
        } finally {
            System.out.println("finally block");
            return "finally";
        }
    }

    private String _test0_1() {
        try {
            System.out.println("try block");
            throw new RuntimeException("");
        } catch (Exception e) {
            System.out.println("catch block");
            return "catch";
        } finally {
            System.out.println("finally block");
            return "finally";
        }
    }
}
