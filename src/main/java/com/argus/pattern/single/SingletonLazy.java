package com.argus.pattern.single;

/**
 * Created by xingding on 2017/2/27.
 * 懒汉模式，非线程安全
 */
public class SingletonLazy {

    private SingletonLazy(){

    }

    private static SingletonLazy instance;

    public static SingletonLazy getInstance(){
        if(instance == null){
            instance = new SingletonLazy();
        }
        return instance;
    }

}
