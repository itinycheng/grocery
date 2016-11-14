package com.tiny.grocery.nio.demo1;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by 16072453 on 2016/10/24.
 */
public class NIOProcessor {

    private static final ExecutorService threadPool = Executors.newFixedThreadPool(2 * Runtime.getRuntime().availableProcessors());

    private final Selector selector;

    public NIOProcessor() throws IOException {
        selector = SelectorProvider.provider().openSelector();
        start();
    }

    private void start() {
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        System.out.println("---------");
                        if (selector.select() <= 0) continue;
                        Set<SelectionKey> keys = selector.selectedKeys();
                        Iterator<SelectionKey> iterator = keys.iterator();
                        while (iterator.hasNext()) {
                            SelectionKey key = iterator.next();
                            iterator.remove();
                            if (key.isReadable()) {
                                handleReadOp(key);
                            } else if (key.isWritable()) {
                                handleWriteOp(key);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void handleReadOp(SelectionKey key) {
        FileChannel fileChannel = null;
        SocketChannel channel = null;
        try {
            fileChannel = new FileInputStream("F://test/nio.txt").getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            channel = (SocketChannel) key.channel();
            while (channel.read(buffer) != -1) {
                buffer.flip();
                fileChannel.write(buffer);
                System.out.println(Charset.forName("UTF-8").decode(buffer));
                buffer.clear();
            }
            addChannel(channel, SelectionKey.OP_WRITE);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fileChannel) {
                    fileChannel.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (null != channel) {
                        channel.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void handleWriteOp(SelectionKey key) {
        try {
            SocketChannel channel = (SocketChannel) key.channel();
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String msg = reader.readLine();
            if (msg != null && !"".equals(msg.trim())){
                channel.write(ByteBuffer.wrap(msg.getBytes()));
                channel.register(selector,SelectionKey.OP_READ);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addChannel(SocketChannel channel, int interestOps) throws IOException {
        channel.register(selector, interestOps);
        selector.wakeup();
    }

    public static void main(String args[]) {
        System.out.println(0x7fffffff + 30);
    }

}
