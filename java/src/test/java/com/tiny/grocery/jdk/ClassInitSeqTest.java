package com.tiny.grocery.jdk;

public class ClassInitSeqTest {
	
	public static void main(String[] args) {
		new ChildInitClass();
	}

	public static class ParentInitClass {

		public ParentInitClass() {
			System.out.println("ParentInitClass");
		}

		{
			System.out.println("parent 块");
		}
		static {
			System.out.println("parent 静态块");
			try {
				System.out.println(ParentInitClass.class.getDeclaredField("a"));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}

	}

	public static class ChildInitClass extends ParentInitClass {

		public ChildInitClass() {
			System.out.println("ChildInitClass");
		}
		
		{
			System.out.println("Child 块");
		}
		static {
			System.out.println("Child 静态块");
		}

	}
}
