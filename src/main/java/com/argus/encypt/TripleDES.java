package com.argus.encypt;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 * 对称加密算法3DES
 * Created by xingding on 2016/9/8.
 */
public class TripleDES {

    // 算法名称
    public static final String KEY_ALGORITHM = "DESede";
    // 算法名称/加密模式/填充方式
    public static final String CIPHER_ALGORITHM_ECB = "DESede/ECB/PKCS5Padding";

    private KeyGenerator keyGen;
    private SecretKey secretKey;
    private Cipher cipher;


    public TripleDES() throws Exception {
        cipher = Cipher.getInstance(CIPHER_ALGORITHM_ECB);
        keyGen = KeyGenerator.getInstance(KEY_ALGORITHM);
        secretKey = keyGen.generateKey();
    }

    /**
     * 加密
     *
     * @param str
     * @return
     * @throws Exception
     */
    public byte[] encrypt(String str) throws Exception {
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return cipher.doFinal(str.getBytes());
    }

    /**
     * 解密
     *
     * @param encrypt
     * @return
     * @throws Exception
     */
    public byte[] decrypt(byte[] encrypt) throws Exception {
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return cipher.doFinal(encrypt);
    }

    public static void main(String[] args) throws Exception {
        TripleDES tripleDES = new TripleDES();
        String source = "北京欢迎你";
        System.out.println("加密前:" + source);
        byte[] encryptBytes = tripleDES.encrypt(source);
        System.out.println("加密后：" + Base64.encodeBase64String(encryptBytes));
        byte[] decryptBytes = tripleDES.decrypt(encryptBytes);
        System.out.println("解密后：" + new String(decryptBytes));
    }


}
