package com.argus.thread;

/**
 * @author xingding
 * @date 2017/8/13.
 */
public class InterruptedTest {

    public static void main(String[] args) {
        try {
            Thread t1 = new Thread(){
                @Override
                public void run() {
                    while(true){
                        //加入相应的中断处理代码
                        if(Thread.currentThread().isInterrupted()){
                            System.out.println("interrupted");
                            break;
                        }
                        Thread.yield();
                    }
                }
            };
            t1.start();
            Thread.sleep(2000);
            t1.interrupt();//发出中断信号
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
