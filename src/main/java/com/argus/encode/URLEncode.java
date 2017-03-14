package com.argus.encode;

import com.argus.util.HexUtil;

import java.io.UnsupportedEncodingException;

/**
 * Created by xingding on 2017/3/14.
 * 浏览器编码URL实际上就是将非ASCII字符（例如中文）按照某种编码格式（例如UTF-8）编码成16进制后，再将每个16进制表示的字节前加上“%”。
 */
public class URLEncode {

    public static void main(String[] args) {
        try {
            String test = "健康";
            byte[] bytes = test.getBytes("UTF-8");
            String hex = HexUtil.byte2HexStr(bytes);
            System.out.println(hex);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


}
