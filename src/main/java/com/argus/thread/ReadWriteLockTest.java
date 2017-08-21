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
    private static Lock lock = new ReentrantLock();
    private static ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private static Lock readLock = readWriteLock.readLock();
    private static Lock writeLock = readWriteLock.writeLock();

    private int value;

    public Object handleRead(Lock lock) throws InterruptedException{
        try {
            lock.lock();
            TimeUnit.MILLISECONDS.sleep(1000);
            return value;
        }  finally {
            lock.unlock();
        }
    }

    public void handleWrite(Lock lock, int index) throws InterruptedException{
        try {
            lock.lock();
            TimeUnit.MILLISECONDS.sleep(1000);
            value = index;
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws Exception {
        ReadWriteLockTest rw = new ReadWriteLockTest();
        Runnable readRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    rw.handleRead(readLock);
//                    rw.handleRead(lock);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Runnable writeRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    rw.handleWrite(writeLock, new Random().nextInt());
//                    rw.handleWrite(lock, new Random().nextInt());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        long start = System.currentTimeMillis();

        for (int i = 0;i<18;i++){
            Thread t = new Thread(readRunnable);
            t.start();
            t.join();
        }

        for(int i=18;i<20;i++){
            Thread t = new Thread(writeRunnable);
            t.start();
            t.join();
        }

        System.out.println("cost: " + (System.currentTimeMillis()-start));

    }


}
