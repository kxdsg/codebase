package com.argus.encypt;

import com.argus.util.HexUtil;

import java.security.MessageDigest;

/**
 * 消息摘要算法MD5
 */
public class MD5Util {

    public static final String MD5 = "MD5";

    public static String md5Digest(String pwd) throws Exception {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(MD5);
            messageDigest.update(pwd.getBytes("UTF-8"));
            byte[] bytes = messageDigest.digest();
            return HexUtil.byte2HexStr(bytes);
        } catch (Exception e) {
           e.printStackTrace();
        }
        return null;
    }


    public static void main(String[] args) throws Exception {
        System.out.println(MD5Util.md5Digest("abcd"));
    }
}
