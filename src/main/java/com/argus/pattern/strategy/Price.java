package com.argus.pattern.strategy;

/**
 * Created by xingding on 2017/3/28.
 */
public class Price {
    private MemberStrategy strategy;

    public Price(MemberStrategy strategy){
        this.strategy = strategy;
    }

    public double quote(double bookPrice){
        return this.strategy.calcPrice(bookPrice);
    }
}
