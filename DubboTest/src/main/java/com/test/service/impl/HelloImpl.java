package com.test.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.test.service.HelloT;

@Service
public class HelloImpl implements HelloT {
	public String sayhello(String name) {
		return "haha======"+name;
	}
}
