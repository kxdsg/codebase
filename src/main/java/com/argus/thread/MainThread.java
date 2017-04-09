package com.argus.thread;

/**
 * Created by xingding on 2017/4/9.
 */
public class MainThread {
    public static void main(String[] args) {
//        System.out.println(Thread.currentThread().getName());
//        MyThread myThread = new MyThread();
//        myThread.start();
//        System.out.println("Test finish");

        /*
         * Test finish
           Mythread is running
           两个线程:
           main 线程
           mythread线程
           多线程的执行顺序和代码调用顺序无关
         */

        MyThread myThread = new MyThread();
        myThread.setName("MyThread");
        myThread.start(); //启动让CPU来调度线程的run方法 //异步
//        myThread.run(); //错误写法，同步执行，实际是main线程在调用mythread中的run方法，本质只有一个main线程在运行。run=main
        //main线程睡眠
        try {
            for(int i=0;i<5;i++){
                int time = (int)(Math.random()*1000);
                Thread.sleep(time);
                System.out.println("main = " + Thread.currentThread().getName());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /*
         * 线程执行顺序随机
         * 输出:
         *  main = main
            run = MyThread
            run = MyThread
            main = main
            main = main
            main = main
            run = MyThread
            main = main
            run = MyThread
            run = MyThread
         */


    }
}
