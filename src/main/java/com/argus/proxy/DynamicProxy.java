package com.argus.proxy;

import java.lang.reflect.Proxy;

/**
 * 动态代理类，使用jdk实现
 * 代理类在程序运行时创建的代理方式被称为动态代理。
 * 也就是说，代理类并不需要在Java代码中定义，而是在运行时动态生成的。
 * 相比于静态代理， 动态代理的优势在于可以很方便的对代理类的函数进行统一的处理，而不用修改每个代理类的函数。
 *
 * 1. 一个接口 Calculator
 * 2. 一个真实实现类 CalculatorImpl
 * 3. 通过实现InvocationHandler的中介类 LogHandler
 * 4. 动态生成代理类
 *
 */
public class DynamicProxy {

	public static void main(String[] args) {
		new DynamicProxy().testDynamicProxy();
	}

	public void testDynamicProxy() {
		Calculator impl = new CalculatorImpl();
		LogHandler handler = new LogHandler(impl);
		Calculator proxy = (Calculator) Proxy.newProxyInstance(impl.getClass().getClassLoader(),
				impl.getClass().getInterfaces(), handler);

		int result = proxy.add(2, 2);
		int minusResult = proxy.minus(4,1);
		System.out.println(String.format("add result: %s, minus result: %s",result,minusResult));
	}

}
