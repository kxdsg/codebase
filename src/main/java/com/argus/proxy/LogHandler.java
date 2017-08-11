package com.argus.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 功能增强处理，位于代理类和真实类之间的中介类
 */
public class LogHandler implements InvocationHandler {
	//被代理的实例
	Calculator calculator;
	LogHandler(Calculator calculator){
		this.calculator = calculator;
	}

	/**
	 *
	 * @param proxy
	 * @param method 标识了我们具体调用的是代理类的哪个方法
	 * @param args  args为这个方法的参数
	 * @return
     * @throws Throwable
     */
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		long start = System.currentTimeMillis();
		System.out.println("start time: " + start);
		Object o = method.invoke(calculator, args);
		long end = System.currentTimeMillis();
		System.out.println("end time: " + end);
		System.out.println("total cost: " + (end - start) + " ms");
		return o;
	}
	
	
}
