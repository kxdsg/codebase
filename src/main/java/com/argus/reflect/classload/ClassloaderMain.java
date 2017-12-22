package com.argus.reflect.classload;

import java.lang.reflect.Field;

/**
 * Created by xingding on 2017/12/22.
 * 测试结论：
 * java中class.forName()和classLoader都可用来对类进行加载。
    class.forName()前者除了将类的.class文件加载到jvm中之外，还会对类进行解释，执行类中的static块。
    实际调用的方法Class.forName(className,true,classloader);其中参数值true就表示需要初始化类static块
    而classLoader只干一件事情，就是将.class文件加载到jvm中，不会执行static中的内容,只有在newInstance才会去执行static块。
 */
public class ClassloaderMain {
    public static void main(String[] args) {
        testClassLoader();
//        testClassForName();

    }

    public static void testClassLoader(){
        try {
            ClassLoader classLoader = ClassLoader.getSystemClassLoader();
            Class<?> line = classLoader.loadClass("com.argus.reflect.classload.Line");
            Class<?> point = classLoader.loadClass("com.argus.reflect.classload.Point");
            System.out.println("line: " + line.getName());
            System.out.println("point: " + point.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void testClassForName(){
        try {
            Class<?> line = Class.forName("com.argus.reflect.classload.Line");
            Class<?> point = Class.forName("com.argus.reflect.classload.Point");
            System.out.println("line: " + line.getName());
            System.out.println("point: " + point.getName());
//            System.out.println(point.getField("PAI").get(null)); //获取静态成员变量object传null
//            System.out.println(point.getField("globalVar").get(point.newInstance()));//获取成员变量传object实例
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
