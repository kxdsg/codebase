package com.argus.encypt;

import com.argus.util.HexUtil;
import org.apache.commons.lang.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 消息摘要算法SHA1
 * Created by xingding on 2016/9/13.
 */
public class SHA1Util {

    public static final String SHA1 = "SHA-1";

    /**
     *
     * @param source
     * @return
     */
    public static byte[] encode(byte[] source){
        try {
            MessageDigest md = MessageDigest.getInstance(SHA1);
            md.update(source);
            return md.digest();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encodeString(String source){
        if(StringUtils.isBlank(source)){
            return null;
        }
        byte[] encodeBytes = encode(source.getBytes());
        return HexUtil.byte2HexStr(encodeBytes);
    }

    public static void main(String[] args) throws Exception{
        System.out.println(SHA1Util.encodeString("123"));
    }

}
