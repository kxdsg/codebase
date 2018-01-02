package com.argus.enummj;

/**
 * Created by xingding on 2017/4/8.
 *
 */
public class EnumTest {

    public static void main(String[] args) {
//        double pay = PayrollDayEnum.MONDAY.pay(9,50);
//        System.out.println(pay);
        WeekDay weekDay = WeekDay.FRI;
        System.out.println(weekDay);       //FRI枚举
        System.out.println(weekDay.name()); //FRI字符串
        System.out.println(weekDay.ordinal()); //5,从0开始
        System.out.println(WeekDay.valueOf("SUN")); //SUN枚举
        System.out.println(WeekDay.SUN.equals(WeekDay.valueOf("SUN"))); //true
        System.out.println("FRI".equals(WeekDay.FRI)); //false
        System.out.println("FRI".equals(WeekDay.FRI.name())); //true 字符串比较
        System.out.println("FRI".equals(WeekDay.FRI.toString())); //true 字符串比较
        System.out.println(WeekDay.values());
    }

    public enum WeekDay{
        SUN,MON,TUE,WED,THI,FRI,SAT;
    }



}
