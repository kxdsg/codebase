package com.argus.gc;

import org.omg.SendingContext.RunTime;

import java.util.UUID;

/**
 * Created by xingding on 18/3/31.
 */
public class TestHeapGc {
    public static void main(String[] args) {
//        byte[] b1 = new byte[1024*1024/2];
//        byte[] b2 = new byte[1024*1024*8];
//        b2 = null;
//        b2 = new byte[1024*1024*8];
//        System.gc();

        for(int i=1;i<=10;i++){
            if(i%2==1){
                System.out.println(i);
            }
        }

    }

}
