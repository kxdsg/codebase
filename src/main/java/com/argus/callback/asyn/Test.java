package com.argus.callback.asyn;

/**
 * Created by xingding on 2016/10/16.
 */
public class Test {

    public static void main(String[] args) {
        Zhansan zhansan = new Zhansan();
        Lisi lisi = new Lisi();
        zhansan.setLisi(lisi);
        zhansan.askQuestion("1+1=?");

    }
}
