package com.argus.util;

import ru.lanwen.verbalregex.VerbalExpression;

/**
 * Created by xingding on 19/2/6.
 */
public class VerbalExpUtil {

    public static void main(String[] args) {
        VerbalExpression ve = VerbalExpression.regex().startOfLine().then("a").anything()
                .endOfLine().then("b").build();
        System.out.println(ve.toString());

        VerbalExpression testRegex = VerbalExpression.regex()
                .startOfLine().then("http").maybe("s")
                .then("://")
                .maybe("www.").anythingBut(" ")
                .endOfLine()
                .build();

        String s = "a123b";
        System.out.println(ve.test(s));
        String url = "http://www.baidu.com";
        System.out.println(testRegex.toString());
        System.out.println(testRegex.test(url));
    }
}
