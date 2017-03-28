package com.argus.pattern.strategy.impl;

import com.argus.pattern.strategy.MemberStrategy;

/**
 * Created by xingding on 2017/3/28.
 * 高级会员折扣类
 */
public class AdvancedMemberStrategy implements MemberStrategy {
    @Override
    public double calcPrice(double bookPrice) {
        System.out.println("高级会员折扣20%");
        return bookPrice*0.8;
    }
}
