package com.argus.core;

/**
 * Created by xingding on 18/3/25.
 */
public class ArrayCopy {

    public static void main(String[] args) {
        int size = 100000;
        int[] array = new int[size];
        int[] arraydest = new int[size];
        for(int i=0;i<size;i++){
            array[i]=i;
        }
        long starttime = System.currentTimeMillis();
        for(int k=0;k<1000;k++){
            System.arraycopy(array,0,arraydest,0,size);
        }
        long endtime = System.currentTimeMillis();

        System.out.println("cost: " + (endtime-starttime));

    }
}
