package com.argus.pattern.singleton;

/**
 * Lazy patterns, thread safety 
 * advantages: 
 * 		1, lazy, used for the first time to instantiate the singleton, avoid the waste of resources 
 * 		2, thread safety
 * weakness: 
 * 		1, lazy, very time consuming if the instance is initialized, the initial use, may cause performance problems 
 * 		2, each call getInstance () to obtain synchronization locks, performance cost.
 * 
 */
public class SingletonTwo {
    
    private static SingletonTwo instance = null;
    
    private SingletonTwo() {
    }
    //加上synchronized，线程安全
    public static synchronized SingletonTwo getInstance() {
        if (instance == null) { 
            instance = new SingletonTwo(); 
        }
        return instance;
    }
    
}