package com.argus.thread;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author xingding
 * @date 2017/8/13.
 */
public class ReenterLockTest{

    public static void main(String[] args) throws Exception{
        ReenterLock r = new ReenterLock();
        Thread t1 = new Thread(r);
        Thread t2 = new Thread(r);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(r.i);

    }

    static class ReenterLock implements Runnable{
        public ReentrantLock lock = new ReentrantLock();
        public int i=0;

        @Override
        public void run() {
            for(int j=0;j<10000000;j++){
                lock.lock();
                try{
                    i++;
                }finally {
                    //释放锁要写在finally里面
                    lock.unlock();
                }
            }
        }
    }
}
