package com.argus.encypt;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
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
    DES、AES 或者 3DES 属于块加密算法，一般来说原文必须是 8 的整数倍，所以块加密算法除子加密模式之外，还涉及到一个填充模式。
    如果用 NoPadding 的话，那么必须保证原文字节是 8 的倍数，否则的话需要使用其他的填充模式。
     */
    public static final String CIPHER_ALGORITHM = "DES/ECB/PKCS5Padding";

    //密钥字符串,自定义,长度=8,如果长度<8,会抛出异常"Wrong key size"
//    public static final String KEY = "1qazxsw2";
    public static final String KEY = initKey();


    /**
     * 生成密钥key字符串(16进制表示)
     * @return
     */
    public static String initKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(DES);
            keyGenerator.init(56);
            SecretKey secretKey = keyGenerator.generateKey();
            byte[] secretKeyBytes = secretKey.getEncoded();
            String key = Hex.encodeHexString(secretKeyBytes);
            System.out.println("key: " + key);
            return key;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

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
        String source = "welcome 北京";
        System.out.println("加密前: " + source);
        String encryptData = encrypt(source);
        System.out.println("加密后: " + encryptData);
        String decryptData = decrypt(encryptData);
        System.out.println("解密后: " + decryptData);
    }

}
