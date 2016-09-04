package com.argus.proxy;

/**
 * 静态代理类，一般不采用
 */
public class StaticProxy {
	
	//for class Calculator proxy only
	private Calculator calculator;

	public StaticProxy(Calculator calculator) {
		this.calculator = calculator;
	}
	
	public StaticProxy(){}
	
	public int add(int a, int b){
		System.out.println("Do something before add");
		int result = calculator.add(a, b);
		System.out.println("Do something after add");
		return result;
	}
	
	
	public static void main(String[] args) throws Exception {
		new StaticProxy().testStaticProxy();
	}
	
	public void testStaticProxy(){
		Calculator calculator = new CalculatorImpl();
		int result = new StaticProxy(calculator).add(1, 2);
		System.out.println(result);
	}

}
