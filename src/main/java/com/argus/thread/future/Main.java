package com.argus.thread.future;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * Created by xingding on 18/3/25.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        FutureTask<String> future = new FutureTask<String>(new RealData("a"));
        ExecutorService es = Executors.newFixedThreadPool(1);
        //执行FutureTask
        es.submit(future);
        System.out.println("请求完毕");

        try {
            //模拟做一些别的事情
            System.out.println("do something else");
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String realResult = future.get();
        System.out.println("数据： " + realResult);

    }
}
