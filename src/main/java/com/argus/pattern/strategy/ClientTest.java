package com.argus.pattern.strategy;

import com.argus.pattern.strategy.impl.AdvancedMemberStrategy;

/**
 * Created by xingding on 2017/3/28.
 */
public class ClientTest {
    public static void main(String[] args) {
        MemberStrategy strategy = new AdvancedMemberStrategy();//选择创建策略对象
        Price price = new Price(strategy);
        double result = price.quote(300);
        System.out.println("最终结果为:" + result);
    }
}
