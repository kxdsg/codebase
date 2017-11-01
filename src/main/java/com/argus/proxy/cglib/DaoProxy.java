package com.argus.proxy.cglib;

import com.argus.proxy.Calculator;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author xingding
 * @date 2017/10/17.
 * spring aop
 *
 */
public class DaoProxy implements MethodInterceptor {

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy proxy) throws Throwable {
        long start = System.currentTimeMillis();
        System.out.println("start time: " + start);
        proxy.invokeSuper(o, objects);
        long end = System.currentTimeMillis();
        System.out.println("end time: " + end);
        System.out.println("total cost: " + (end - start) + " ms");
        return o;
    }
}
