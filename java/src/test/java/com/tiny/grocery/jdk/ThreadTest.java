package com.tiny.grocery.jdk;

import org.junit.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by 16072453 on 2016/11/14.
 */
public class ThreadTest {
    @Test
    public void test1() {
        ThreadGroup group = Thread.currentThread().getThreadGroup();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(group, runnable, "tes", 0);
        System.out.println(thread);
    }

    @Test
    public void test2() {
        ThreadPoolExecutor service = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
        service.prestartAllCoreThreads();
        for (int i = 0; i < 1; i++) {
            System.out.println("------------" + i);
            service.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.currentThread().sleep(20 * 1000);
                        System.out.println("in");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        System.out.println("out");
    }
}
