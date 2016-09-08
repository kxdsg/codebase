package com.argus.encypt;

import com.argus.util.HexUtil;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.SecureRandom;

/**
 * 对称加密算法AES
 * Created by xingding on 2016/9/8.
 */
public class AESUtil {

    public static final String AES = "AES";
    public static final String KEY = "a1s2d3f4g5h6j7k8";

    public static Key genKey() throws Exception{
        KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);
        keyGenerator.init(128,new SecureRandom(KEY.getBytes()));
        SecretKey secretKey = keyGenerator.generateKey();
        return secretKey;
    }

    /**
     * 加密
     * @param source
     * @return
     * @throws Exception
     */
    public static byte[] encypt(String source) throws Exception{
        Key secretKey = genKey();
        byte[] encodeFormat = secretKey.getEncoded();
        SecretKeySpec secreKeySpec = new SecretKeySpec(encodeFormat,AES);
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.ENCRYPT_MODE, secreKeySpec);
        byte[] result = cipher.doFinal( source.getBytes());
        return result;
    }

    /**
     * 解密
     * @param content
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(byte[] content) throws Exception{
        Key secretKey = genKey();
        byte[] encodeFormat = secretKey.getEncoded();
        SecretKeySpec secreKeySpec = new SecretKeySpec(encodeFormat,AES);
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.DECRYPT_MODE, secreKeySpec);
        byte[] result = cipher.doFinal(content);
        return result;
    }


    public static void main(String[] args) throws Exception{
        String source = "北京欢迎你";
        System.out.println("加密前:" + source);
        byte[] encryptBytes = encypt(source);
        System.out.println("加密后:" + HexUtil.byte2HexStr(encryptBytes));
        byte[] decryptBytes = decrypt(encryptBytes);
        System.out.println("解密后:" + new String(decryptBytes));

    }


}
