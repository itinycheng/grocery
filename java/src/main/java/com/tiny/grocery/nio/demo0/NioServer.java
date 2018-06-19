package com.tiny.grocery.nio.demo0;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author tiny.wang
 */
public class NioServer {

    private static final AtomicInteger SERVER_ID_ADDER = new AtomicInteger(0);
    private static final Charset UTF8 = Charset.forName("utf-8");
    private Map<String, ByteBuffer> bufferMap = new HashMap<>();
    private Selector selector;

    private String serverId;

    private NioServer(int port) throws IOException {
        serverId = "server-" + SERVER_ID_ADDER.incrementAndGet();
        selector = Selector.open();
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress(port));
        serverChannel.configureBlocking(false);
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    public static void main(String[] args) throws IOException {
        if (Objects.isNull(args) || args.length <= 0) {
            throw new IllegalArgumentException("port is null!");
        }
        NioServer nioServer = new NioServer(Integer.parseInt(args[0]));
        nioServer.run();
    }

    private void run() throws IOException {
        for (; ; ) {
            if (selector.select() <= 0) {
                continue;
            }
            Iterator<SelectionKey> selectionKeys = selector.selectedKeys().iterator();
            while (selectionKeys.hasNext()) {
                SelectionKey selectionKey = selectionKeys.next();
                selectionKeys.remove();
                if (selectionKey.isAcceptable()) {
                    ServerSocketChannel channel = (ServerSocketChannel) selectionKey.channel();
                    SocketChannel accept = channel.accept();
                    if (Objects.nonNull(accept)) {
                        doAccept(accept);
                    }
                }
                if (selectionKey.isConnectable()) {
                    System.out.println("connect");
                }
                if (selectionKey.isReadable()) {
                    doRead((SocketChannel) selectionKey.channel());
                }
                if (selectionKey.isWritable()) {
                    doWrite((SocketChannel) selectionKey.channel());
                }
            }
        }
    }

    private void doWrite(SocketChannel channel) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line = reader.readLine();
        channel.register(selector, SelectionKey.OP_READ);
        channel.write(ByteBuffer.wrap((serverId + " : " + line).getBytes()));
    }

    private void doRead(SocketChannel channel) throws IOException {
        ByteBuffer allocate = ByteBuffer.allocate(1024);
        channel.read(allocate);
        allocate.flip();
        System.out.println(UTF8.decode(allocate).toString());
        channel.register(selector, SelectionKey.OP_WRITE);
    }

    private void doAccept(SocketChannel channel) throws IOException {
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_READ);
    }

}
