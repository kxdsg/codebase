package com.argus.pattern.singleton;

/**
 * Lazy thread-safe 
 * advantages: 
 * 		1, thread safety 
 * 		2, lazy 
 * weakness: 
 * 		1 high concurrency double check, ordinary lazy loading is to produce data cannot be
 * expected, this is the chain of memory 
 * 		2 serialized solution serialization of singleton would happen
 * 
 */
public class SingletonFour {
    
    private static class SingletonHolder{
        private static SingletonFour instance = new SingletonFour();
    }
    
    private SingletonFour() {
        
    }
    
    public static SingletonFour getInstance() {
        return SingletonHolder.instance;
    }
    
}