package com.argus.pattern.singleton;

/**
 * Lazy patterns, not thread-safe
 * Advantages: 
 * 		1,lazy, used for the first time to instantiate the singleton, avoid the waste of resources
 * Disadvantages: 
 * 		1, the lazy, time consuming if the instance is initialized, the initial use, may cause performance problems
 * 		2, not thread-safe.Multiple threads may have multiple instances are initialized.
 * 
 */
public class SingletonOne {
    
    private static SingletonOne instance = null;
    
    private SingletonOne() {
        
    }
    //非线程安全
    public static SingletonOne getInstance() {
        if (instance == null) {
            instance = new SingletonOne();
        }
        return instance;
    }
}
