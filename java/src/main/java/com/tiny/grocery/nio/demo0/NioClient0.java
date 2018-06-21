package com.tiny.grocery.nio.demo0;

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
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author tiny.wang
 */
public class NioClient0 {

    private static final AtomicInteger CLIENT_ID_ADDER = new AtomicInteger(0);
    private static final Charset UTF8 = Charset.forName("utf-8");
    private ByteBuffer buffer = ByteBuffer.allocate(1024);
    private String clientId;
    private Selector selector;

    private NioClient0(int port) throws IOException {
        clientId = "client-" + CLIENT_ID_ADDER.incrementAndGet();
        selector = Selector.open();
        SocketChannel open = SocketChannel.open();
        open.configureBlocking(false);
        open.connect(new InetSocketAddress(port));
        open.register(selector, SelectionKey.OP_CONNECT);
    }

    public static void main(String[] args) throws Exception {
        if (Objects.isNull(args) || args.length <= 0) {
            throw new IllegalArgumentException("port is null.");
        }
        NioClient0 nioClient = new NioClient0(Integer.parseInt(args[0]));
        nioClient.run();
    }

    private void run() throws Exception {
        for (; ; ) {
            if (selector.select() <= 0) {
                continue;
            }
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey next = iterator.next();
                iterator.remove();
                SocketChannel channel = (SocketChannel) next.channel();
                if (Objects.isNull(next)) {
                    continue;
                }
                if (next.isConnectable()) {
                    doConnect(channel);
                }
                if (next.isAcceptable()) {
                    System.out.println("accept");
                }
                if (next.isReadable()) {
                    doRead(channel);
                }
                if (next.isWritable()) {
                    doWrite(channel);
                }
            }
        }
    }

    private void doWrite(SocketChannel channel) throws IOException {
        // key board input
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line = reader.readLine();
        // register & write out
        channel.register(selector, SelectionKey.OP_READ);
        channel.write(ByteBuffer.wrap((clientId + " : " + line).getBytes()));
    }

    private void doRead(SocketChannel channel) throws IOException {
        buffer.clear();
        channel.read(buffer);
        buffer.flip();
        System.out.println(UTF8.decode(buffer).toString());
        channel.register(selector, SelectionKey.OP_WRITE);
    }

    private void doConnect(SocketChannel channel) throws Exception {
        while (!channel.finishConnect()) {
            Thread.sleep(1);
        }
        channel.register(selector, SelectionKey.OP_WRITE);
    }

}
