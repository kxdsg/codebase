package com.argus.thread;


import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by xingding on 18/5/13.
 */
public class CommunicateNewTest {


    public static void main(String[] args) {
        new CommunicateNewTest().test();
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
                    business.main(i);
                }
            }
        },"mainthread").start();
    }


    class Business {
        volatile boolean subrun = true;
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        public void sub(int i) {
            lock.lock();
            try {
                while (!subrun) {
                    try {
                        condition.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                for (int j = 1; j <= 10; j++) {
                    System.out.println(Thread.currentThread().getName() + " loop of " + j + " for loop " + i);
                }
                subrun = false;
                condition.signal();
            }finally{
                lock.unlock();
            }
        }


        public void main(int i) {
            lock.lock();
            try {
                while (subrun) {
                    try {
                        condition.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                for (int j = 1; j <= 10; j++) {
                    System.out.println(Thread.currentThread().getName() + " loop of " + j + " for loop " + i);
                }
                subrun = true;
                condition.signal();
            } finally{
                lock.unlock();
            }

        }

    }


}
