package com.argus.pattern.single;

/**
 * Created by xingding on 2017/2/27.
 * 饿汉模式，线程安全
 */
public class Singleton {
    /*
    1. 构造函数私有化，其他类中无法直接新建该对象实例
     */
    private Singleton(){

    }

    /*
    2. 初始化一个对象，private static 保证唯一
     */
    private static Singleton instance = new Singleton();

    /*
    提供方法供外部类调用获取该唯一对象
     */
    public static Singleton getInstance(){
        return instance;
    }

    public static void main(String[] args) {

        Singleton s1 = Singleton.getInstance();
        Singleton s2 = Singleton.getInstance();
        if(s1==s2){
            System.out.println("指向同一个对象实例");
        } else {
            System.out.println("指向不同对象实例");
        }

    }

}
