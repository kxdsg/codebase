package com.argus.crypto;

import com.argus.util.LoggerUtil;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by IntelliJ IDEA.
 * User: xingding
 * Date: 3/1/12
 * Time: 4:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class CryptoUtil
{
  private static LoggerUtil logger = LoggerUtil.getInstance(CryptoUtil.class);
  private static final String base64Key = "VzNvgh4/52LFq9dIdKMeDg==";

  public static void generateKey()
  {
    KeyGenerator kgen;
    try
    {
      kgen = KeyGenerator.getInstance("AES");
      kgen.init(128);

      SecretKey skey = kgen.generateKey();
      byte[] raw = skey.getEncoded();

      SecretKey rKey = new SecretKeySpec(raw, "AES");
      byte[] rawRestore = rKey.getEncoded();

      String rawBase64 = Base64.encodeBase64String(raw);
      System.out.println(rawBase64);

      rawBase64 = Base64.encodeBase64String(rawRestore);
      System.out.println(rawBase64);
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
  }

  public static String decryptString(String cipherTxt) {
    return new String(decrypt(cipherTxt));
  }

  public static String decryptString(byte[] cipherTxt) {
    return new String(decrypt(cipherTxt));
  }

  public static byte[] decrypt(byte[] cipherTxt) {
    return decrypt(Base64.encodeBase64String(cipherTxt));
  }

  public static byte[] decrypt(String cipherTxt) {
    Cipher cipher;
    try {
      cipher = Cipher.getInstance("AES");
      byte[] keyRaw = Base64.decodeBase64(base64Key);
      SecretKey rKey = new SecretKeySpec(keyRaw, "AES");
      cipher.init(2, rKey);

      return cipher.doFinal(Base64.decodeBase64(cipherTxt));
    }
    catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (NoSuchPaddingException e) {
      e.printStackTrace();
    } catch (InvalidKeyException e) {
      e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
      e.printStackTrace();
    } catch (BadPaddingException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static String encryptBase64(String plainTxt) {
    byte[] cipherTxt = encrypt(plainTxt);

    if (cipherTxt != null) {
      return Base64.encodeBase64String(cipherTxt);
    }

    return null;
  }

  public static byte[] encrypt(byte[] plain, int offset, int length) {
    Cipher cipher;
    try {
      cipher = Cipher.getInstance("AES");
      byte[] keyRaw = Base64.decodeBase64(base64Key);
      SecretKey rKey = new SecretKeySpec(keyRaw, "AES");
      cipher.init(1, rKey);

      return cipher.doFinal(plain);
    }
    catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (NoSuchPaddingException e) {
      e.printStackTrace();
    } catch (InvalidKeyException e) {
      e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
      e.printStackTrace();
    } catch (BadPaddingException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static byte[] encrypt(byte[] plain) {
    return encrypt(plain, 0, plain.length);
  }

  public static byte[] encrypt(String plainTxt) {
    return encrypt(plainTxt.getBytes());
  }

}