package com.tiny.grocery.jdk.proxy;

public class DaoProxyTest {

	public static void main(String[] args) {
		DaoProxy daoProxy = new DaoProxy(new DaoImpl());
		Dao instance = daoProxy.newInstance();
		instance.query();
	}
}
