package com.argus.core;

/**
 * Created by xingding on 18/11/3.
 */
public class JstackDemo {

    public static void main(String[] args) {
        Thread thread = new Thread(new Runnable1());
        thread.start();
    }
}

class Runnable1 implements Runnable{
    @Override
    public void run() {
        while(true){
            System.out.println(1);
        }
    }
}

