package com.tiny.grocery.nio.demo2;

import java.io.IOException;
import java.nio.channels.*;
import java.nio.channels.spi.SelectorProvider;

public class ServerDispatcher {

    private Selector[] selectors;

    private SelectorProvider provider;

    private ServerSocketChannel server;

    public ServerDispatcher(ServerSocketChannel server) throws IOException {
        provider = SelectorProvider.provider();
        selectors = new Selector[3];
        for (int i = 0; i < selectors.length; i++) {
            selectors[i] = provider.openSelector();
        }
        registerAccept(server);
        this.server = server;
    }

    public void registerAccept(ServerSocketChannel channel) throws ClosedChannelException {
        channel.register(getAcceptSelector(), SelectionKey.OP_ACCEPT)
                .attach(new SocketAcceptHandler(this));
    }

    public void registerRead(SocketChannel channel) throws ClosedChannelException {
        channel.register(getReadSelector(), SelectionKey.OP_READ)
                .attach(new SocketReadHandler(this));
    }

    public void registerWrite(SocketChannel channel) throws ClosedChannelException {
        channel.register(getWriteSelector(), SelectionKey.OP_WRITE)
                .attach(new SocketWriteHandler());
    }

    public Selector getAcceptSelector(){
        return selectors[0];
    }

    public Selector getReadSelector(){
        return selectors[1];
    }

    public Selector getWriteSelector(){
        return selectors[2];
    }

}