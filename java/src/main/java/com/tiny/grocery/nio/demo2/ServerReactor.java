package com.tiny.grocery.nio.demo2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;

public class ServerReactor implements Runnable {

    private ServerSocketChannel server;

    public ServerReactor(int port) throws IOException {
        server = ServerSocketChannel.open();
        server.bind(new InetSocketAddress(port));
        server.configureBlocking(false);
    }

    public void run() {
        try {
            new ServerDispatcher(server);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}