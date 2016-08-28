package com.tiny.grocery.jdk;

import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

import org.junit.Test;

public class NIOTest {

    @Test
    public void  testByteBuf() {
        byte[] a = new byte[]{0,1,2,3,4,5,6,7,8,9};
        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put(a, 0, 10);
        buffer.flip();
        System.out.println(buffer.toString());
    }

    @Test
    public void testSelector() throws Exception {
        Selector selector = Selector.open();
        ServerSocketChannel channel = ServerSocketChannel.open();
        channel.socket().bind(new InetSocketAddress(9199));
        channel.configureBlocking(false);
        SelectionKey key = channel.register(selector, SelectionKey.OP_ACCEPT);
        key.attach(new Object());
        testWakeUp(selector);
        Thread.sleep(3000);
        selector.select();
        System.out.println("ww");
    }

    private void testWakeUp(final Selector selector) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(10000);
                    selector.wakeup();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @Test
    public void testFileByte() throws Exception {
        RandomAccessFile aFile = new RandomAccessFile(
                "src/test/resources/nio.txt", "rw");
        FileChannel inChannel = aFile.getChannel();
        ByteBuffer buf = ByteBuffer.allocate(48);
        int bytesRead = inChannel.read(buf);
        while (bytesRead != -1) {
            System.out.println("Read " + bytesRead);
            buf.flip();
            while (buf.hasRemaining()) {
                System.out.print((char) buf.get());
            }
            buf.clear();
            bytesRead = inChannel.read(buf);
        }
        aFile.close();
    }
}
