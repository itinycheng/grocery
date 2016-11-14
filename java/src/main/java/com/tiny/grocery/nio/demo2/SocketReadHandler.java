package com.tiny.grocery.nio.demo2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by 16072453 on 2016/11/7.
 */
public class SocketReadHandler implements SocketHandler, Runnable {

    private static ExecutorService threadPool = Executors.newFixedThreadPool(2 * Runtime.getRuntime().availableProcessors());

    private ServerDispatcher dispatcher;

    public SocketReadHandler(ServerDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public void handle() {
        threadPool.execute(this);
    }

    @Override
    public void run() {
        //dispatcher.
    }
}
