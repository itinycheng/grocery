package com.tiny.grocery.nio.demo2;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by 16072453 on 2016/11/7.
 */
public class SocketAcceptHandler implements SocketHandler, Runnable {

    private ExecutorService threadpool = Executors.newSingleThreadExecutor();

    private ServerDispatcher dispatcher;

    private Selector selector;

    public SocketAcceptHandler(ServerDispatcher dispatcher) {
        this.dispatcher = dispatcher;
        this.selector = dispatcher.getAcceptSelector();
        threadpool.execute(this);
    }

    @Override
    public void handle() throws IOException {
        while (selector.select() > 0){
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = keys.iterator();
            while (iterator.hasNext()){
                SelectionKey key = iterator.next();
                iterator.remove();
                if(key.isAcceptable()){
                    ServerSocketChannel channel = (ServerSocketChannel)key.channel();
                    SocketChannel accept = channel.accept();
                    accept.configureBlocking(false);
                    dispatcher.registerRead(accept);
                }
            }
        }
    }

    @Override
    public void run() {

    }
}
