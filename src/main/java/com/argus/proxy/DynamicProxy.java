package com.argus.proxy;

import java.lang.reflect.Proxy;

/**
 * 动态代理类，使用jdk实现
 */
public class DynamicProxy {

	public static void main(String[] args) {
		new DynamicProxy().testDynamicProxy();
	}

	public void testDynamicProxy() {
		Calculator impl = new CalculatorImpl();
		LogHandler handler = new LogHandler(impl);
		Calculator calculator = (Calculator) Proxy.newProxyInstance(impl.getClass().getClassLoader(),
				impl.getClass().getInterfaces(), handler);

		int result = calculator.add(2, 2);
		System.out.println(result);
	}

}
