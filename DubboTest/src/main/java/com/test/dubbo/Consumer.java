package com.test.dubbo;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.test.service.HelloT;

public class Consumer {
	private static ClassPathXmlApplicationContext context;

	public static void main(String[] args) {
		context = new ClassPathXmlApplicationContext(new String[] { "application-context-consumer.xml" });
		HelloT demoService = (HelloT) context.getBean("helloT");
		String hello = demoService.sayhello("dubbo consumer!");
		System.out.println(hello);
	}
}
