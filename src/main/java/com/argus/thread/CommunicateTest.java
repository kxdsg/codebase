package com.argus.thread;


/**
 * Created by xingding on 18/5/13.
 */
public class CommunicateTest {


    public static void main(String[] args) {
        new CommunicateTest().test();
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

        public synchronized void sub(int i) {
            while(!subrun){
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            for (int j = 1; j <= 10; j++) {
                System.out.println(Thread.currentThread().getName() + " loop of " + j + " for loop " + i);
            }
            subrun = false;
            this.notify();
        }


        public synchronized void main(int i) {
            while(subrun){
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            for (int j = 1; j <= 10; j++) {
                System.out.println(Thread.currentThread().getName() + " loop of " + j + " for loop " + i);
            }
            subrun = true;
            this.notify();

        }

    }


}
