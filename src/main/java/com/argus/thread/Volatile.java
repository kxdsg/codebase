package com.argus.thread;

import java.util.concurrent.TimeUnit;

/**
 * Created by xingding on 18/10/31.
 */
public class Volatile implements Runnable {

    private static volatile boolean flag = true;
    @Override
    public void run() {
        while (flag){
            System.out.println(Thread.currentThread().getName() + "正在运行...");
        }
        System.out.println(Thread.currentThread().getName() + "执行完毕!");
    }

    public static void main(String[] args) {
        Volatile v = new Volatile();
        new Thread(v,"Thread A").start();

        System.out.println("Main线程正在运行");
        try {
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        v.stop();


    }

    private void stop(){
        flag = false;
    }
}
