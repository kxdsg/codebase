package com.argus.reflect.classload;

/**
 * Created by xingding on 2017/12/22.
 */
public class Point {
    public static final String PAI = "3.14";
    public String globalVar = "haha";
    static {
        System.out.println("静态代码块执行：loading point");
    }
}
