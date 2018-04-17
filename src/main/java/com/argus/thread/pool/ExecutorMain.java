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
        ExecutorService es = Executors.newFixedThreadPool(1);
        /*
        return new ThreadPoolExecutor(nThreads, nThreads,
                                      0L, TimeUnit.MILLISECONDS,
                                      new LinkedBlockingQueue<Runnable>());
         */

    }
}
