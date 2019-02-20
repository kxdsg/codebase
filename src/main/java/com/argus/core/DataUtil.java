package com.argus.core;


import com.argus.clone.Student;

import java.math.BigDecimal;

/**
 * Created by xingding on 18/12/19.
 */
public class DataUtil {

    public static void main(String[] args) {

//        long tableSumWeight = 1;
//        long ruleWeight = 2;
//        double tableAvg = tableSumWeight/ruleWeight; //输出0.0
//        double tableAvg2 = (double)tableSumWeight/ruleWeight; //输出0.5,需要转换成double
//        System.out.println(tableAvg);
//        System.out.println(tableAvg2);
//        DataUtil t = new DataUtil();
//        double result = t.calculateTableAvg(1,2);
//        System.out.println(result);

        Student s = new Student();
        if(s.getId()== null){
            s.setId(100);
            s.setName("kang");
        } else {
            System.out.println(s.getId());
            s.setName("xing");
        }
        System.out.println(s.toString());




    }

    private double calculateTableAvg(long tableSumWeight, long ruleWeight){
        double result = (double) tableSumWeight / ruleWeight;
        BigDecimal b = new BigDecimal(result);
        return b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
