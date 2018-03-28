package com.argus.thread;

/**
 * Created by xingding on 18/1/12.
 */
public class ShareObj {

    public static void main(String[] args) {

        Ticket t=new Ticket();
        //创建4个线程
        new Thread(t).start();
        new Thread(t).start();
        new Thread(t).start();
        new Thread(t).start();

    }
}

class Ticket implements Runnable{

    int tk = 30;

    @Override
    public void run() {
        while(tk>0){
            System.out.println(Thread.currentThread().getName()+"窗口正在售出"+tk--+"号票.");
        }

    }
}
