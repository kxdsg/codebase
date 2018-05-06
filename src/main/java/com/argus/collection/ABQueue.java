package com.argus.collection;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by xingding on 18/5/6.
 */
public class ABQueue {

    public static void main(String[] args) {
        //基于数组、先进先出、线程安全
//        ArrayBlockingQueue queue = new ArrayBlockingQueue(10);
//        for(int i=0;i<10;i++){
//            queue.add("test"+i);
//        }
//        System.out.println(queue.size());

        ArrayBlockingQueue queue1 = new ArrayBlockingQueue(10);
        queue1.add("a");
        System.out.println(queue1.peek()); //a
        System.out.println(queue1.size());  //1
        System.out.println(queue1.peek()); //a


    }
}
