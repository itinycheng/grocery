package com.tiny.grocery.nio.demo2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by 16072453 on 2016/11/7.
 */
public class SocketWriteHandler implements SocketHandler, Runnable {

    private static ExecutorService threadPool = Executors.newFixedThreadPool(2 * Runtime.getRuntime().availableProcessors());;

    public SocketWriteHandler(){

    }

    @Override
    public void handle() {
        threadPool.execute(this);
    }

    @Override
    public void run() {

    }
}
