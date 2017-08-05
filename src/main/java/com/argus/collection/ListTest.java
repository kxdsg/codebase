package com.argus.collection;

import java.util.*;

/**
 * @author xingding
 * @date 2017/8/5.
 *
 */
public class ListTest {

    public static final int num = 50000;

    public static List values;

    //初始化list元素
    static {
        Integer vals[] = new Integer[num];
        Random r = new Random();
        for(int i=0,currval=0;i<num;i++){
            vals[i]=new Integer(currval);
            currval+=r.nextInt(100)+1;
        }
        values = Arrays.asList(vals);
    }

    static long timeSearchList(List list){
        long start = System.currentTimeMillis();
        for(int i=0;i<num;i++){
            int idx = Collections.binarySearch(list, values.get(i));
            if(idx!=i){
                System.err.println("错误");
            }
        }
        return System.currentTimeMillis() - start;
    }

    static long timeAddList(List list){
        long start = System.currentTimeMillis();
        Object obj = new Object();
        for(int i=0;i<num;i++){
            list.add(0,obj);//从list前端加入元素
        }
        return System.currentTimeMillis() - start;
    }



    public static void main(String[] args) {
        System.out.println("ArrayList cost:" + timeAddList(new ArrayList<>(values)));
        System.out.println("LinkedList cost:" + timeAddList(new LinkedList<>(values)));
    }

    interface Testable{
        void test();
    }

}
