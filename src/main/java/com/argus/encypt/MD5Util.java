package com.argus.encypt;

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
            byte[] digest = messageDigest.digest();
            return byte2hex(digest);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return "";
    }

    /**
     * 二进制转16进制
     *
     * @param b
     * @return
     */
    private static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0xFF);
            if (stmp.length() == 1)
                hs = hs + "0" + stmp;
            else
                hs = hs + stmp;
        }
        return hs.toLowerCase();
    }

    public static void main(String[] args) throws Exception {
        System.out.println(MD5Util.md5Digest("abcd"));
    }
}
