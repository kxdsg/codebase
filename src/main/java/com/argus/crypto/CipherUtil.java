package com.argus.crypto;

import com.thoughtworks.xstream.XStream;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.*;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

public class CipherUtil {

	private static XStream xstream = new XStream();
    private static Cipher cipher = null;
    static {
    	try {
			// parameter "DES" specifies type of cipher we want to create
			// through the factory method. It includes algorithm, mode and
			// padding. You can define only algorithm and in that case default
			// values will be used for mode and padding.
			cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		}
    }

    public static byte[] decodeBase64 (String base64String) {
    	return Base64.decodeBase64(base64String);
    }

    public static String encodeBase64 (byte[] msg) {
    	return Base64.encodeBase64String(msg);
    }

	public static Key getKey (String base64String) {
		byte[] keyXml = Base64.decodeBase64(base64String);
		return (Key) xstream.fromXML(new String (keyXml));
	}

	public static String convertBase64 (Key key) {
		String xml = xstream.toXML(key);
		return Base64.encodeBase64String(xml.getBytes());
	}

	public static String encrypt (Key key, String plainText) {
		try {
			cipher.init(Cipher.ENCRYPT_MODE, key);

			return encodeBase64(cipher.doFinal(plainText.getBytes()));
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static String decrypt (Key key, String base64EncodedCipherText) {
		try {
			cipher.init(Cipher.DECRYPT_MODE, key);
			return new String (cipher.doFinal( decodeBase64(base64EncodedCipherText)));
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}

		return null;

	}

    private static String convertToHex(byte[] data) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9))
                    buf.append((char) ('0' + halfbyte));
                else
                    buf.append((char) ('a' + (halfbyte - 10)));
                halfbyte = data[i] & 0x0F;
            } while(two_halfs++ < 1);
        }
        return buf.toString();
    }

    public static String calcMD5 (File file) {
    	return calcMD5 (file.getAbsolutePath());
    }

	public static String calcMD5 (String fileName) {
		MessageDigest md;
		FileInputStream fis = null;
		try {
			md = MessageDigest.getInstance("MD5");

			fis = new FileInputStream (fileName);
			byte[] byteBuf = new byte[1024*100];

			int bytesRead = 0;
			while ((bytesRead = fis.read(byteBuf)) > 0) {
				md.update(byteBuf, 0, bytesRead);
			}
			byte[] md5hash = new byte[32];
			md5hash = md.digest();

			return convertToHex(md5hash);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return null;
	}

	public static Key generateKey () {
		KeyGenerator keyGen;
		try {
			keyGen = KeyGenerator.getInstance("DES");
	        keyGen.init(56);

	        return keyGen.generateKey();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static void test_encrypt () {
		Key sampleKey = generateKey ();
		String base64Str = convertBase64 (sampleKey);

		System.out.println (base64Str);

		Properties prop = new Properties ();
		prop.put("imp", base64Str);

		String host = "";
		String sid = "";
		String user = "";
		String passwd = "";

		prop.put ("host", encrypt (sampleKey, host));
		prop.put ("sid", encrypt (sampleKey, sid));
		prop.put ("user", encrypt (sampleKey, user));
		prop.put ("passwd", encrypt (sampleKey, passwd));

		try {
			prop.store(new FileOutputStream ("QueueMonitor.prop"), "enc");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void testMd5 () {
		System.out.println (calcMD5 ("d:\\test\\testface.jpg"));
	}
	public static void main(String[] args) {
		//test_encrypt ();
		testMd5 ();
	}
}
