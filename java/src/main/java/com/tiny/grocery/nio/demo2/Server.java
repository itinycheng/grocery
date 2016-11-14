package com.tiny.grocery.nio.demo2;

import java.io.IOException;

public class Server {
	public static void main(String[] args) throws IOException {
		 int port = 8008;          
		 new Thread(new ServerReactor(port)).start();
	}
}