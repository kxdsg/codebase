package com.argus.pattern.strategy.impl;

import com.argus.pattern.strategy.MemberStrategy;

/**
 * Created by xingding on 2017/3/28.
 * 中级会员折扣类
 */
public class IntermediateMemberStrategy implements MemberStrategy {
    @Override
    public double calcPrice(double bookPrice) {
        System.out.println("中级会员折扣10%");
        return bookPrice*0.9;
    }
}
