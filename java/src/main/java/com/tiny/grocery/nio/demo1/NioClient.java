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

/**
 * @author tiny.wang
 */
public class NioClient {

    private final ByteBuffer buffer = ByteBuffer.allocate(1024);
    private Selector selector;

    public static void main(String[] args) throws Exception {
        new NioClient().init().listen();
    }

    public NioClient init() throws IOException {
        selector = Selector.open();
        SocketChannel channel = SocketChannel.open();
        channel.configureBlocking(false);
        channel.connect(new InetSocketAddress(7777));
        channel.register(selector, SelectionKey.OP_CONNECT);
        return this;
    }

    public void listen() throws IOException {
        System.out.println("listening...");
        while (true) {
            if (selector.select() <= 0) {
                continue;
            }
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
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
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String line = reader.readLine();
            SocketChannel socketChannel = (SocketChannel) key.channel();
            socketChannel.write(ByteBuffer.wrap(line.getBytes("UTF-8")));
            socketChannel.register(selector, SelectionKey.OP_READ);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleReadOp(SelectionKey key) {
        try {
            SocketChannel socketChannel = (SocketChannel) key.channel();
            buffer.clear();
            socketChannel.read(buffer);
            buffer.flip();
            System.out.println(Charset.forName("UTF-8").decode(buffer));
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
                Thread.sleep(1);
            }
            socketChannel.register(selector, SelectionKey.OP_WRITE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
