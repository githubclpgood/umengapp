package com.test.dubbo;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Provide {
	private static ClassPathXmlApplicationContext context;

	public static void main(String[] args) throws Exception {
		context = new ClassPathXmlApplicationContext(new String[] { "application-context-provide.xml" });
		context.start();
		System.in.read();
	}
}
