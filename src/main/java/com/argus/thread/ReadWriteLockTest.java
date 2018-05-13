package com.argus.thread;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author xingding
 * @date 2017/8/19.
 */
public class ReadWriteLockTest {

    public static void main(String[] args) {
        new ReadWriteLockTest().test();
    }


    private void test(){
        Queue queue = new Queue();
        for(int i=0;i<3;i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while(true) {
                        queue.get();
                    }
                }
            }).start();
        }
        for(int i=0;i<3;i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while(true) {
                        queue.put(new Random().nextInt(1000));
                    }

                }
            }).start();
        }
    }

    class Queue {
        private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        private Lock readLock = readWriteLock.readLock();
        private Lock writeLock = readWriteLock.writeLock();

        private Object data = null; //共享数据，只能有一个线程能写该数据，但可以有多个线程同时读取数据

        public void get(){
            readLock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + " be ready to read data!");
                Thread.sleep((long)Math.random()*1000);
                System.out.println(Thread.currentThread().getName() + " read data : " + data);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally{
                readLock.unlock();
            }

        }

        public void put(Object data){
            writeLock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + " be ready to write data!");
                Thread.sleep((long)Math.random()*1000);
                this.data = data;
                System.out.println(Thread.currentThread().getName() + " write data : " + data);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                writeLock.unlock();
            }

        }
    }



}
