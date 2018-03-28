package com.argus.core;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

/**
 * Created by xingding on 18/3/25.
 */
public class ReferTest {

    public static void main(String[] args) {
        SoftReference<String> sr = new SoftReference<String>(new String("hello"));
        System.out.println(sr.get());
        System.gc();
        System.out.println(sr.get());

        WeakReference<String> wr = new WeakReference<String>(new String("hello"));
        System.out.println(wr.get());
        System.gc();
        System.out.println(wr.get());
    }
}
