package com.tiny.grocery.jdk.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class DaoProxy {

	private Dao dao;

	public DaoProxy(Dao dao) {
		super();
		this.dao = dao;
	}
	
	public Dao newInstance() {
		Object instance = Proxy.newProxyInstance(this.dao.getClass().getClassLoader(), this.dao.getClass().getInterfaces(), new InvocationHandler() {
			
			@Override
			public Object invoke(Object proxy, Method method, Object[] args)
					throws Throwable {
				if(method.getName().equals("query")){
					System.out.println("check...");
				}
				return method.invoke(DaoProxy.this.dao, args);
			}
		});
		return (Dao) instance;
	}
}
