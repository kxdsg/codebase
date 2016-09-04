package com.argus.reflect;

import java.lang.reflect.Method;

public class ReflectClass {
	
	public static void main(String[] args) throws Exception {
		//invoke native method, use invoker's classloader to load class object
		Class actionClass = Class.forName("com.argus.reflect.StraightDrawer");
//		Class actionClass = Thread.currentThread().getClass().forName("com.argus.reflect.StraightDrawer");
		//get public method draw
		Method method = actionClass.getMethod("draw", null);
		//new class instance
		Object action = actionClass.newInstance();
		//execute method reflect
		method.invoke(action, null);
	}

}
