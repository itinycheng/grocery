package com.tiny.grocery.nio.demo1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by 16072453 on 2016/10/28.
 */
public class NIOClient {

    private Selector selector;

    public NIOClient init() throws IOException {
        selector = Selector.open();
        SocketChannel channel = SocketChannel.open();
        channel.configureBlocking(false);
        channel.connect(new InetSocketAddress("127.0.0.1", 7777));
        channel.register(selector, SelectionKey.OP_CONNECT);
        return this;
    }

    public void listen() throws IOException, InterruptedException {
        System.out.println("listening...");
        while (true) {
            if (selector.select() <= 0) continue;
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = keys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                if (key.isConnectable()) {
                    handleConnectOp(key);
                } else if (key.isReadable()) {
                    handleReadOp(key);
                } else if (key.isWritable()) {
                    handleWriteOp(key);
                }
            }
        }
    }

    private void handleWriteOp(SelectionKey key) {
        try {
            // listen keyboard
            SocketChannel socketChannel = (SocketChannel) key.channel();
            boolean flag = true;
            while (flag) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                String line = reader.readLine();
                if (line == null || line.trim().length() <= 0) {
                    continue;
                } else if ("quit".equalsIgnoreCase(line)) {
                    System.out.println("client quitting.");
                    reader.close();
                    socketChannel.close();
                    selector.close();
                    System.exit(0);
                } else {
                    socketChannel.write(ByteBuffer.wrap(line.getBytes("UTF-8")));
                    socketChannel.register(selector, SelectionKey.OP_READ);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleReadOp(SelectionKey key) {
        try {
            SocketChannel socketChannel = (SocketChannel) key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(2 * 1024);
            while (socketChannel.read(buffer) > 0) {
                buffer.flip();
                System.out.println(Charset.forName("UTF-8").decode(buffer));
                buffer.clear();
            }
            socketChannel.register(selector, SelectionKey.OP_WRITE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleConnectOp(SelectionKey key) {
        try {
            SocketChannel socketChannel = (SocketChannel) key.channel();
            socketChannel.configureBlocking(false);
            while (!socketChannel.finishConnect()) {
                // Thread.sleep(10);
            }
            System.out.println("finished connect.");
            socketChannel.write(ByteBuffer.wrap("connected message".getBytes("UTF-8")));
            socketChannel.register(selector, SelectionKey.OP_READ);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) throws Exception {
        new NIOClient().init().listen();
       /* while (true) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String line = reader.readLine();
            if ("quit".equalsIgnoreCase(line)) {
                return;
            }
            System.out.println(line);
        }*/

    }
}
