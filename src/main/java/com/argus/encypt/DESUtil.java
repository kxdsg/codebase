package com.argus.encypt;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.Key;
import java.security.SecureRandom;

/**
 * 对称加密算法DES
 * Created by xingding on 2016/9/8.
 */
public class DESUtil {
    //算法名称
    public static final String DES = "DES";
    /*
    工作模式：ECB：电子密码本模式、CBC：加密分组链接模式、CFB：加密反馈模式、OFB：输出反馈模式
    填充方式：NoPadding:不填充、ZerosPadding: 0填充、PKCS5Padding
     */
    public static final String CIPHER_ALGORITHM = "DES/ECB/PKCS5Padding";

    //密钥字符串,自定义
    public static final String KEY = "a1!s2@d3#f4$g5%h6^j7&k8*92WWQS2123UYWY$";

    /**
     * 生成密钥key对象
     * @return
     * @throws Exception
     */
    public static SecretKey genKey() throws Exception{
        DESKeySpec desKeySpec = new DESKeySpec(KEY.getBytes());
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey secretKey = secretKeyFactory.generateSecret(desKeySpec);
        return secretKey;
    }

    /**
     * 加密数据
     * @param data
     * @return
     * @throws Exception
     */
    public static String encrypt(String data) throws Exception{
        Key desKey = genKey();
        //实例化Cipher对象,用于完成实际的加密操作
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        SecureRandom secureRandom = new SecureRandom();
        cipher.init(Cipher.ENCRYPT_MODE, desKey, secureRandom);
        byte[] results = cipher.doFinal(data.getBytes());
        //加密后的结果用base64字符串传输
        return Base64.encodeBase64String(results);
    }

    /**
     * 解密数据
     * @param data
     * @return
     * @throws Exception
     */
    public static String decrypt(String data) throws Exception{
        Key desKey = genKey();
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE,desKey);
        byte[] results = cipher.doFinal(Base64.decodeBase64(data));
        return new String(results);
    }

    public static void main(String[] args) throws Exception {
        String source = "i am plain text";
        System.out.println("加密前: " + source);
        String encryptData = encrypt(source);
        System.out.println("加密后: " + encryptData);
        String decryptData = decrypt(encryptData);
        System.out.println("解密后: " + decryptData);
    }

}
