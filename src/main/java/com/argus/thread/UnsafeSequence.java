package com.argus.thread;

/**
 * @author xingding
 * @date 2017/9/26.
 */
public class UnsafeSequence {

    private int value;

    /**
     * 非线程安全
     * 1.1 读取值
     * 1.2 加值操作
     * 1.3 写入新值
     * @return
     */
    public int getNext(){
        return value++;
    }

    /*
    public synchronized int getNext(){
        return value++;
    }
    */

    public static void main(String[] args) {
        final UnsafeSequence seq = new UnsafeSequence();
        for(int i=0;i<100;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("Thread " + Thread.currentThread() + " get next value is " + seq.getNext());
                }
            }).start();
        }
    }
}
