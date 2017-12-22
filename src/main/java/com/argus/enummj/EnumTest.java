package com.argus.enummj;

/**
 * Created by xingding on 2017/4/8.
 *
 */
public class EnumTest {

    public static void main(String[] args) {
        double pay = PayrollDayEnum.MONDAY.pay(9,50);
        System.out.println(pay);
    }
}
