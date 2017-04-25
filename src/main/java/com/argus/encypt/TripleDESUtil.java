package com.argus.encypt;

import org.apache.xerces.impl.dv.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * Created by xingding on 2016/10/31.
 */
public class TripleDESUtil {

    private static final String Algorithm = "DESede"; //算法名称
    // 算法名称/加密模式/填充方式
    public static final String CIPHER_ALGORITHM_ECB = "DESede/ECB/PKCS5Padding";

    public static final String key = "ed2a0c42518395c6af183e3a"; //加密密钥，自定义

    /**
     * 3des解密
     * @param value 待解密字符串
     * @return
     * @throws Exception
     */
    public static String decrypt3DES(String value) throws Exception {
        byte[] b = decryptMode(getKeyBytes(key), Base64.decode(value));
        return new String(b);
    }

    /**
     * 3des加密
     * @param value 待加密字符串
     * @return
     * @throws Exception
     */
    public static String encrypt3DES(String value) throws Exception {
        String str = byte2Base64(encryptMode(getKeyBytes(key), value.getBytes()));
        return str;
    }

    /**
     * 获取key 24位长的密码byte值
     * @param strKey
     * @return
     * @throws Exception
     */
    public static byte[] getKeyBytes(String strKey) throws Exception {
        if (null == strKey || strKey.length() < 1) {
            throw new Exception("key is null or empty!");
        }
        byte[] bkey = strKey.getBytes();
        int start = bkey.length;
        byte[] bkey24 = new byte[24];
        //补足24字节
        for (int i = 0; i < start; i++) {
            bkey24[i] = bkey[i];
        }
        for (int i = start; i < 24; i++) {
            bkey24[i] = '\0';
        }
        return bkey24;
    }

    /**
     *
     * @param keybyte 为加密密钥，长度为24字节
     * @param src 为被加密的数据缓冲区（源）
     * @return
     */
    public static byte[] encryptMode(byte[] keybyte, byte[] src) {
        try {
            SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
            Cipher c1 = Cipher.getInstance(CIPHER_ALGORITHM_ECB);
            c1.init(Cipher.ENCRYPT_MODE, deskey);
            return c1.doFinal(src);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (java.lang.Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param keybyte 为加密密钥，长度为24字节
     * @param src 为加密后的缓冲区
     * @return
     */
    public static byte[] decryptMode(byte[] keybyte, byte[] src) {
        try {
            SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
            Cipher c1 = Cipher.getInstance(CIPHER_ALGORITHM_ECB);
            c1.init(Cipher.DECRYPT_MODE, deskey);
            return c1.doFinal(src);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (java.lang.Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }

    /**
     * 转换成base64编码
     * @param b
     * @return
     */
    public static String byte2Base64(byte[] b) {
        return Base64.encode(b);
    }

    /**
     * 转换成十六进制字符串
     * @param b
     * @return
     */
    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1)
                hs = hs + "0" + stmp;
            else
                hs = hs + stmp;
            if (n < b.length - 1)
                hs = hs + ":";
        }
        return hs.toUpperCase();
    }

    public static void main(String[] args) throws Exception {

        String name = "{\"username\":\"usera\",\"channel\":\"shuchuang\"}"; //原始字符串
        String encodeStr = encrypt3DES(name);
        System.out.println("加密之后的字符串：" + encodeStr);
        String decodeStr = decrypt3DES(encodeStr);
        System.out.println("解密之后的字符串:" + decodeStr);
    }

}
