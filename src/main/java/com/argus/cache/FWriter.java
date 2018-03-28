package com.argus.cache;

import java.io.*;

/**
 * Created by xingding on 18/3/24.
 */
public class FWriter {

    public static final int CIRCLE = 10000000;
    public static void main(String[] args) {
        try {
//            Writer w = new FileWriter(new File("test.txt"));
            Writer w = new BufferedWriter(new FileWriter(new File("test.txt")));
            long start = System.currentTimeMillis();
            for(int i=0;i<CIRCLE;i++){
                w.write(i);
            }
            w.close();
            long end = System.currentTimeMillis();
            System.out.println("cost:" + (end-start));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
