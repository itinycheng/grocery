package com.tiny.grocery.jdk;

public class CodeBlockInitTest {

	{
		System.out.println("code block. ");
	}

	static{
		System.out.println("static code block. ");
	}
	public static void main(String[] args) {
		System.out.println(CodeBlockInitTest.class);
		System.out.println(CodeBlockInitTest.class);
		System.out.println("\n--------------------\n");
		System.out.println(new CodeBlockInitTest());
		System.out.println(new CodeBlockInitTest());
	}
}
