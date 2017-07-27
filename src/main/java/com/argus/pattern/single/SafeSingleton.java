package com.argus.pattern.single;

/**
 * @author xingding
 * @date 17/7/28.
 * 静态内部类实现单例
 */
public class SafeSingleton {

    /*
    1. 构造函数私有化，其他类中无法直接新建该对象实例
     */
    private SafeSingleton(){}

    /*
    2. 公开的静态方法，供外部访问
     */
    public static SafeSingleton getInstance(){
        return SafeSingletonHolder.instance;
    }

    public static class SafeSingletonHolder{
        private static final SafeSingleton instance = new SafeSingleton();
    }

    public static void main(String[] args) {
        SafeSingleton s = SafeSingleton.getInstance();
        SafeSingleton s2 = SafeSingleton.getInstance();
        System.out.println(s == s2);
    }

}
