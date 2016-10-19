package com.argus.callback.anony;

/**
 *
 * Created by xingding on 2016/10/16.
 */
public class Callee {

    public void execute(ICallback callback){
        //共用代码1
        long start = System.currentTimeMillis();
        System.out.println("open session...");

        String s = "hello";
        callback.transform(s);

        //共用代码2
        System.out.println("close session...");
        long end = System.currentTimeMillis();
        System.out.println("cost: " + (end-start)/1000 + " seconds");

    }

    public static void main(String[] args) {

        new Callee().execute(new ICallback() {
            @Override
            public void transform(String arg) {
                //不同的实现
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("UPPERCASE: " + arg.toUpperCase());
            }
        });

        new Callee().execute(new ICallback() {
            @Override
            public void transform(String arg) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("LOWERCASE: " + arg.toLowerCase());
            }
        });
    }


}
