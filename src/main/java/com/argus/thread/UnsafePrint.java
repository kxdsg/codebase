package com.argus.thread;

/**
 * Created by xingding on 18/5/12.
 */
public class UnsafePrint {
    public static void main(String[] args) {
        new UnsafePrint().test();
    }

    private void test(){
        Outputer outputer = new Outputer();
        //启动第1个线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    outputer.output("kangxingding");
                }
            }
        }).start();

        //启动第2个线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    outputer.output("wangyuwen");
                }
            }
        }).start();
    }



    class Outputer{
        public void output(String name){
            int len = name.length();
            synchronized (this) { //fix
//            synchronized (name) { //错误写法，必须是同一个对象
                for (int i = 0; i < len; i++) {
                    System.out.print(name.charAt(i));
                }
//            }
            }
            System.out.println();
        }
    }
}
