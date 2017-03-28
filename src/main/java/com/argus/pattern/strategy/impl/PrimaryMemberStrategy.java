package com.argus.pattern.strategy.impl;

import com.argus.pattern.strategy.MemberStrategy;

/**
 * Created by xingding on 2017/3/28.
 * 初级会员折扣类
 */
public class PrimaryMemberStrategy implements MemberStrategy {

    @Override
    public double calcPrice(double bookPrice) {
        System.out.println("初级会员没有折扣");
        return bookPrice;
    }
}
