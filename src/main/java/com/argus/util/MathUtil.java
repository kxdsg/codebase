package com.argus.util;

import java.util.Random;

/**
 *
 * Created by xingding on 2016/11/22.
 */
public class MathUtil {

    public static void main(String[] args) {
        for(int i=0;i<10;i++){
            String s = String.valueOf(new Random().nextInt(99999999));
            System.out.println(s);
        }
    }
}
