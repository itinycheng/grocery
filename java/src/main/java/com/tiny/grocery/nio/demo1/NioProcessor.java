package com.tiny.grocery.nio.demo1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author tiny.wang
 */
public class NioProcessor implements Runnable {

    private static final int WAIT = 500;
    private final Selector selector;
    private final BlockingQueue<ChannelEntry> channelQueue;
    private final String ident;
    private final Charset UTF8 = Charset.forName("UTF-8");

    public NioProcessor(int id) throws IOException {
        ident = "processor-" + id;
        channelQueue = new ArrayBlockingQueue<>(1000);
        selector = Selector.open();
        Executors.newSingleThreadExecutor().execute(this);
    }

    public static void main(String[] args) {
        System.out.println(0x7fffffff + 30);
    }

    @Override
    public void run() {
        while (true) {
            try {
                process();
                register();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void process() throws Exception {
        if (selector.select(WAIT) > 0) {
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
        }
    }

    private void register() throws Exception {
        for (; ; ) {
            ChannelEntry entry = channelQueue.poll();
            if (Objects.isNull(entry)) {
                break;
            }
            entry.channel.register(selector, entry.interestOps);
        }
    }

    private void handleReadOp(SelectionKey key) {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            SocketChannel channel = (SocketChannel) key.channel();
            channel.read(buffer);
            buffer.flip();
            System.out.println(ident + " says: " + UTF8.decode(buffer).toString());
            addChannel(channel, SelectionKey.OP_WRITE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleWriteOp(SelectionKey key) {
        try {
            SocketChannel channel = (SocketChannel) key.channel();
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String msg = reader.readLine();
            channel.write(ByteBuffer.wrap(msg.getBytes()));
            channel.register(selector, SelectionKey.OP_READ);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean addChannel(SocketChannel channel, int interestOps) throws Exception {
        return channelQueue.offer(new ChannelEntry(channel, interestOps),
                WAIT, TimeUnit.MILLISECONDS);
    }

    class ChannelEntry {

        SocketChannel channel;

        int interestOps;

        ChannelEntry(SocketChannel channel, int interestOps) {
            Objects.requireNonNull(channel);
            this.channel = channel;
            this.interestOps = interestOps;
        }
    }

}
