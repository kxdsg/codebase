package com.argus.pattern.singleton;

/**
 * not Lazy thread-safe 
 * advantages: 
 * 		1, thread safety 
 * weakness: 
 * 		1, not lazy loading, if the structure of singleton is very big, structure and reluctant to use,
 * can lead to waste of resources.
 * 
 */
public class SingletonThree {
    
    private static SingletonThree instance = new SingletonThree();
    
    private SingletonThree() {
        
    }
    
    public static SingletonThree getInstance() {
        return instance;
    }
    
}
