package com.tiny.grocery.jdk.proxy;

public class DaoImpl implements Dao{


	public void persist() {
		System.out.println("persist");
	}


	public void remove() {
		System.out.println("remove");
	}


	public void update() {
		System.out.println("update");
	}

	@Override
	public String query() {
		System.out.println("query");
		return null;
	}

	
}
