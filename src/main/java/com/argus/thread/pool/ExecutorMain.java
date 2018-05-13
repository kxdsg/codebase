package com.argus.thread.pool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by xingding on 18/3/29.
 */
public class ExecutorMain {

    public static void main(String[] args) {
        //ExecutorService是接口,继承了Executor接口,ThreadPoolExecutor是其实现类
        ExecutorService es = Executors.newFixedThreadPool(3);
        /*
        return new ThreadPoolExecutor(nThreads, nThreads,
                                      0L, TimeUnit.MILLISECONDS,
                                      new LinkedBlockingQueue<Runnable>());
         */

        for(int i=0;i<10;i++){ //放10个任务交给3个线程处理
            final int task = i;
            es.execute(new Runnable() {
                @Override
                public void run() {
                   for(int j=0;j<10;j++){
                       try {
                           Thread.sleep(20);
                       } catch (InterruptedException e) {
                           e.printStackTrace();
                       }
                       System.out.println(Thread.currentThread().getName() + " is loop of " + j + " for task " + task);
                   }
                }
            });
        }

    }
}
