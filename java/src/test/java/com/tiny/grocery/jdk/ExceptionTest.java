package com.tiny.grocery.jdk;

/**
 * Created by 16072453 on 2016/11/14.
 */
public class ExceptionTest {

    public static void main(String args[]) throws Exception {
        int i = 1;
        while (i++ < 10) {
            meth1();
            meth2();
        }
    }

    private static void meth2() throws Exception {
        throw new Exception("dfds");
    }

    private static void meth1() {
        System.out.println("start meth1.");
        try {
            throw new Exception("error..");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("catch exception..");
        }
        System.out.println("end meth1..");
    }
}
