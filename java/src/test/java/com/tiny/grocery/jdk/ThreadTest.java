package com.tiny.grocery.jdk;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
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

    @Test
    public void test3() {
        ExecutorService service = Executors.newSingleThreadExecutor();
        List<Runnable> runnables = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            final int index = i;
            runnables.add(new Runnable() {
                @Override
                public void run() {
                    if (index % 5 == 0) {
                        Thread.currentThread().interrupt();
                    }
                    System.out.println(index + " : " + Thread.currentThread().isInterrupted());
                }
            });
        }
        for (Runnable runnable : runnables) {
            service.execute(runnable);
        }
    }

    @Test
    public void test4() throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                    System.out.println("end");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        Thread.sleep(2000);
        thread.interrupt();
    }

    @Test
    public void test5() throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int i = 0;
                    while (true) {
                        i++;
                        Thread.yield();
                    }
                } finally {
                    System.out.print("end");
                }
            }
        });
        thread.start();
        Thread.sleep(1000);
        thread.interrupt();
    }

    @Test
    public void test6(){
        System.out.println(Thread.currentThread());
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread());
            }
        }).start();
    }
}
