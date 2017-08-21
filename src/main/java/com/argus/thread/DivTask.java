package com.argus.thread;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author xingding
 * @date 2017/8/19.
 */
public class DivTask implements Runnable {
    int a,b;

    public DivTask(int a, int b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public void run() {
        double r = a/b;
        System.out.println(r);
    }

    public static void main(String[] args) {
        ThreadPoolExecutor pool = new ThreadPoolExecutor(0,Integer.MAX_VALUE, 0, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
        for (int i=0;i<5;i++){
            pool.execute(new DivTask(10,i));
        }
    }
}
