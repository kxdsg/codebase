package com.argus.thread;


import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by xingding on 18/5/13.
 * 多路通知
 * subthread 执行完 通知 sub2thread执行，执行完后再通知 mainthread执行，执行后通知subthread执行
 */
public class CommunicateMultiTest {


    public static void main(String[] args) {
        new CommunicateMultiTest().test();
    }


    private void test() {
        Business business = new Business();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i <= 5; i++) {
                    business.sub(i);
                }
            }
        },"subthread").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i <= 5; i++) {
                    business.sub2(i);
                }
            }
        },"sub2thread").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i <= 5; i++) {
                    business.main(i);
                }
            }
        },"mainthread").start();
    }


    class Business {
        volatile int subrun = 1;
        Lock lock = new ReentrantLock();
        Condition condition1 = lock.newCondition();
        Condition condition2 = lock.newCondition();
        Condition condition3 = lock.newCondition();

        public void sub(int i) {
            lock.lock();
            try {
                while (subrun != 1) {
                    try {
                        condition1.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                for (int j = 1; j <= 10; j++) {
                    System.out.println(Thread.currentThread().getName() + " loop of " + j + " for loop " + i);
                }
                subrun = 2;
                condition2.signal();
            }finally{
                lock.unlock();
            }
        }

        public void sub2(int i) {
            lock.lock();
            try {
                while (subrun != 2) {
                    try {
                        condition2.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                for (int j = 1; j <= 10; j++) {
                    System.out.println(Thread.currentThread().getName() + " loop of " + j + " for loop " + i);
                }
                subrun = 3;
                condition3.signal();
            }finally{
                lock.unlock();
            }
        }


        public void main(int i) {
            lock.lock();
            try {
                while (subrun != 3) {
                    try {
                        condition3.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                for (int j = 1; j <= 10; j++) {
                    System.out.println(Thread.currentThread().getName() + " loop of " + j + " for loop " + i);
                }
                subrun = 1;
                condition1.signal();
            } finally{
                lock.unlock();
            }

        }

    }


}
