package com.argus.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 功能增强处理
 */
public class LogHandler implements InvocationHandler {
	Object obj;
	LogHandler(Object obj){
		this.obj = obj;
	}
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		this.doBefore();
		Object o = method.invoke(obj, args);
		this.doAfter();
		return o;
	}
	
	public void doBefore(){
		System.out.println("Do something before");
	}
	
	public void doAfter(){
		System.out.println("Do something after");
	}
	
	
}
