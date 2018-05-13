package com.argus.thread;

import java.util.Random;

/**
 * Created by xingding on 18/5/12.
 * 线程内共享变量，线程外独立，这个变量通常需要跨越多个模块，例如一个线程一个connection，connection
 * 涉及转入的功能，也涉及转出的功能。
 */
public class ThreadLocalTest {


    private ThreadLocal dataMap = new ThreadLocal();

    public static void main(String[] args) {
        new ThreadLocalTest().test();
    }

    private void test(){
        for(int i=0;i<3;i++) { //启动3个线程
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //这个数据给当前线程
                    int data = new Random().nextInt();
                    dataMap.set(data);
                    System.out.println("Thread " + Thread.currentThread().getName()
                            + " has set data of " + data);
                    new A().get();
                    new B().get();
                }
            }).start();
        }
    }



    //模拟A模块
    class A{
        public void get(){
            System.out.println("A from "+Thread.currentThread().getName()+"get data of " + dataMap.get());
        }
    }

    //模拟B模块
    class B{
        public void get(){
            System.out.println("B from "+Thread.currentThread().getName()+"get data of " + dataMap.get());
        }
    }


}
