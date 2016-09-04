package com.argus.crypto;

import com.argus.util.LoggerUtil;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

/**
 * Created by IntelliJ IDEA.
 * User: xingding
 * Date: 3/1/12
 * Time: 4:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class HashUtil
{
  private static LoggerUtil logger = LoggerUtil.getInstance(HashUtil.class);

  public static String genSHA256Hash(InputStream inp)
    throws NoSuchAlgorithmException, IOException
  {
    MessageDigest md = null;
    try {
      md = MessageDigest.getInstance("SHA-256");
    } catch (NoSuchAlgorithmException e) {
      logger.error(e, new Object[] { "unable to hash, because SHA-256 algor cannot be found!" });
      throw e;
    }

    byte[] buf = new byte[2048];
    int bytesRead = 0;
    try
    {
      String str;
      while ((bytesRead = inp.read(buf)) != -1)
        md.update(buf, 0, bytesRead);

      byte[] digestHash = md.digest();
      return Base64.encodeBase64String(digestHash);
    }
    catch (IOException e) {
      throw e;
    } finally {
      if (inp != null)
        try {
          inp.close();
        } catch (IOException e) {
          logger.error(e, new Object[] { "cannot close InputStream " });
        }
    }
  }

  public static String genSHA256Hash(String filePath) throws NoSuchAlgorithmException, IOException
  {
    MessageDigest md = null;
    try {
      md = MessageDigest.getInstance("SHA-256");
    } catch (NoSuchAlgorithmException e) {
      logger.error(e, new Object[] { "[", filePath, "] unable to hash, because SHA-256 algor cannot be found!" });
      throw e;
    }

    BufferedInputStream bis = null;
    try
    {
      String str;
      bis = new BufferedInputStream(new FileInputStream(filePath));

      return genSHA256Hash(bis);
    }
    catch (FileNotFoundException e) {
      throw e;
    }
    catch (IOException e) {
      throw e;
    } finally {
      if (bis != null)
        try {
          bis.close();
        } catch (IOException e) {
          logger.error(e, new Object[] { "[", filePath, "] cannot close BufferedStream " });
        }
    }
  }

  public static void main(String[] args) {
    String hexStr;
    try {
      hexStr = Hex.encodeHexString(Base64.decodeBase64(genSHA256Hash("testJAva.out")));
      System.out.println(hexStr);
    }
    catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}