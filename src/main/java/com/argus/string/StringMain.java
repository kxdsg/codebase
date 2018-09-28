package com.argus.string;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * Created by xingding on 2017/12/22.
 * String类的不可变不仅仅在于final修饰，而在于本身内部实现没有操作核心的char[]
 * private final char value[]
 */
public class StringMain {

    public static void test(){
        String str1 = "abc";
        String str2 = new String("abc");
        String str3 = "a" + "bc";
        System.out.println(str1 == str3);
    }

    public static void main(String[] args) {
        test();
        final int[] values = {1,2,3};
        int[] another = {4,5,6};
//        values = another; //编译器报错，用final修饰，编译器不允许我把value指向堆区另一个地址

//        values[1] = 4; //修改成功
        Array.set(values,1,5); //修改成功
        for (int i:values
             ) {
            System.out.println(i);
        }

    }
}
