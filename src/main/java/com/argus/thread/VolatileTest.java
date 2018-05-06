package com.argus.thread;

import java.util.Hashtable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by xingding on 18/5/6.
 * volatile关键字只保证主内存可见性，不保证线程安全
 */
public class VolatileTest{

    /*
    public static volatile int num = 0;
    static CountDownLatch countDownLatch = new CountDownLatch(30);

    public static void main(String[] args) throws Exception {
        for(int i=0;i<30;i++){
            new Thread(){
                @Override
                public void run() {
                    for(int j=0;j<10000;j++){
                        num++; //多个线程共享这个变量
                    }
                    countDownLatch.countDown();
                }
            }.start();
        }
        countDownLatch.await();
        System.out.println(num); //输出245341，不是预期的300000
    }
    */
    public static AtomicInteger num = new AtomicInteger();
    static final int THREAD_COUNT = 30;
    //使用CountDownLatch来等待计算线程执行完
    static CountDownLatch countDownLatch = new CountDownLatch(THREAD_COUNT);

    public static void main(String[] args) throws Exception {
        for(int i=0;i<THREAD_COUNT;i++){
            new Thread(){
                @Override
                public void run() {
                    for(int j=0;j<10000;j++){
                        num.incrementAndGet();
                    }
                    countDownLatch.countDown(); //写在run方法里面
                }
            }.start();
        }
        countDownLatch.await();//等待所有线程执行完毕
        System.out.println(num);
    }
}
