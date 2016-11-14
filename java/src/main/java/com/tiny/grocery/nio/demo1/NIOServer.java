package com.tiny.grocery.nio.demo1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by 16072453 on 2016/10/24.
 */
public class NIOServer {

    private Selector selector;

    private NIOProcessor[] processors;

    public static void main(String args[]) throws IOException {
        new NIOServer().init().listen();
    }

    private NIOServer init() throws IOException {
        selector = Selector.open();
        ServerSocketChannel server = ServerSocketChannel.open();
        server.bind(new InetSocketAddress(7777));
        server.configureBlocking(false);
        server.register(selector, SelectionKey.OP_ACCEPT);
        // init NIOProcessors
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        processors = new NIOProcessor[availableProcessors];
        for (int i = 0; i < availableProcessors; i++) {
            processors[i] = new NIOProcessor();
        }
        return this;
    }

    private void listen() throws IOException {
        int index = 0;
        while (true) {
            if (selector.select() <= 0) continue;
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                if (key.isAcceptable()) {
                    ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                    SocketChannel accept = channel.accept();
                    if (accept == null) continue;
                    System.out.println("remote address : " + accept.getRemoteAddress());
                    accept.configureBlocking(false);
                    NIOProcessor processor = processors[index++ / processors.length];
                    processor.addChannel(accept,SelectionKey.OP_READ);
                }
            }
        }
    }

}
