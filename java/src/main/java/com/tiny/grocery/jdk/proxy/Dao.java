package com.tiny.grocery.jdk.proxy;

public interface Dao {

	void persist();

	void remove();

	void update();

	String query();
}
