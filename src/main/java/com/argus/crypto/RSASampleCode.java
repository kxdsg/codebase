package com.argus.crypto;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Created by IntelliJ IDEA.
 * User: xingding
 * Date: 3/1/12
 * Time: 4:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class RSASampleCode {
    private static final String systemLineSeparator = System.getProperty("line.separator");
    private static final char[] map1 = new char[64];
    private static final byte[] map2;

    static {
        int i = 0;
        for (char c = 'A'; c <= 'Z'; c = (char) (c + '\1'))
            map1[(i++)] = c;
        for (char c = 'a'; c <= 'z'; c = (char) (c + '\1'))
            map1[(i++)] = c;
        for (char c = '0'; c <= '9'; c = (char) (c + '\1'))
            map1[(i++)] = c;
        map1[(i++)] = '+';
        map1[(i++)] = '/';

        map2 = new byte[128];

        for (i = 0; i < map2.length; ++i)
            map2[i] = -1;
        for (i = 0; i < 64; ++i)
            map2[map1[i]] = (byte) i;
    }

    public static void generateKey() {
        Key publicKey = null;
        Key privateKey = null;
        KeyPair kp = null;
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(4096);
            kp = kpg.genKeyPair();
            publicKey = kp.getPublic();
            privateKey = kp.getPrivate();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        KeyFactory fact = null;
        try {
            fact = KeyFactory.getInstance("RSA");
            RSAPublicKeySpec pub = (RSAPublicKeySpec) fact.getKeySpec(kp.getPublic(), RSAPublicKeySpec.class);
            RSAPrivateKeySpec priv = (RSAPrivateKeySpec) fact.getKeySpec(kp.getPrivate(), RSAPrivateKeySpec.class);

            saveToFile("public.key", pub.getModulus(), pub.getPublicExponent());
            saveToFile("private.key", priv.getModulus(), priv.getPrivateExponent());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.exit(-1);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
            System.exit(-1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String performSHAHash(String fileName) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-1");

            byte[] digestBuffer = new byte[10240];
            FileInputStream fis = new FileInputStream(fileName);

            int bytesRead = -1;
            while ((bytesRead = fis.read(digestBuffer)) != -1) {
                md.update(digestBuffer, 0, bytesRead);
            }

            byte[] hashEncode = md.digest();
            return encodeLines(hashEncode);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static byte[] encryptFile(String privateKeyFile, String plainHash) {
        PrivateKey privateKey;
        try {
            privateKey = readPrivateKeyFromFile(privateKeyFile);
            return encryptFile(privateKey, plainHash);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] encryptFile(PrivateKey privKey, String plainHash) {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("RSA");
            cipher.init(1, privKey);
            byte[] cipherData = cipher.doFinal(plainHash.getBytes());
            return cipherData;
        } catch (NoSuchAlgorithmException e) {
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

    public static String decryptFile(String pubKeyFile, byte[] secretHash, int offset, int length) {
        PublicKey pubKey;
        try {
            pubKey = readPublicKeyFromFile(pubKeyFile);
            return decryptFile(pubKey, secretHash, offset, length);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decryptFile(String pubKeyFile, byte[] secretHash) {
        PublicKey pubKey;
        try {
            pubKey = readPublicKeyFromFile(pubKeyFile);
            return decryptFile(pubKey, secretHash, 0, secretHash.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decryptFile(PublicKey pubKey, byte[] secretHash, int offset, int length) {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("RSA");
            cipher.init(2, pubKey);
            byte[] plainData = cipher.doFinal(secretHash, offset, length);
            return new String(plainData);
        } catch (NoSuchAlgorithmException e) {
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

    public static void saveToFile(String fileName, BigInteger mod, BigInteger exp) throws IOException {
        ObjectOutputStream oout = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(fileName)));
        try {
            oout.writeObject(mod);
            oout.writeObject(exp);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Unexpected error");
        } finally {
            oout.close();
        }
    }

    public static PublicKey readPublicKeyFromFile(String keyFileName)
            throws IOException {
        InputStream in = new FileInputStream(keyFileName);
        ObjectInputStream oin = new ObjectInputStream(new BufferedInputStream(in));
        try {
            PublicKey localPublicKey1;
            BigInteger m = (BigInteger) oin.readObject();
            BigInteger e = (BigInteger) oin.readObject();
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(m, e);
            KeyFactory fact = KeyFactory.getInstance("RSA");
            PublicKey pubKey = fact.generatePublic(keySpec);
            return pubKey;
        } catch (Exception e) {
        } finally {
            oin.close();
        }
        return null;
    }

    public static PrivateKey readPrivateKeyFromFile(String keyFileName)
            throws IOException {
        InputStream in = new FileInputStream(keyFileName);
        ObjectInputStream oin = new ObjectInputStream(new BufferedInputStream(in));
        try {
            PrivateKey localPrivateKey1;
            BigInteger m = (BigInteger) oin.readObject();
            BigInteger e = (BigInteger) oin.readObject();
            RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(m, e);
            KeyFactory fact = KeyFactory.getInstance("RSA");
            PrivateKey pubKey = fact.generatePrivate(keySpec);
            return pubKey;
        } catch (Exception e) {
        } finally {
            oin.close();
        }
        return null;
    }

    public static String encodeString(String s) {
        return new String(encode(s.getBytes()));
    }

    public static String encodeLines(byte[] in) {
        return encodeLines(in, 0, in.length, 76, systemLineSeparator);
    }

    public static String encodeLines(byte[] in, int iOff, int iLen, int lineLen, String lineSeparator) {
        int blockLen = lineLen * 3 / 4;
        if (blockLen <= 0)
            throw new IllegalArgumentException();
        int lines = (iLen + blockLen - 1) / blockLen;
        int bufLen = (iLen + 2) / 3 * 4 + lines * lineSeparator.length();
        StringBuilder buf = new StringBuilder(bufLen);
        int ip = 0;
        while (ip < iLen) {
            int l = Math.min(iLen - ip, blockLen);
            buf.append(encode(in, iOff + ip, l));
            buf.append(lineSeparator);
            ip += l;
        }
        return buf.toString();
    }

    public static char[] encode(byte[] in) {
        return encode(in, 0, in.length);
    }

    public static char[] encode(byte[] in, int iLen) {
        return encode(in, 0, iLen);
    }

    public static char[] encode(byte[] in, int iOff, int iLen) {
        int oDataLen = (iLen * 4 + 2) / 3;
        int oLen = (iLen + 2) / 3 * 4;
        char[] out = new char[oLen];
        int ip = iOff;
        int iEnd = iOff + iLen;
        int op = 0;
        while (ip < iEnd) {
            int i0 = in[(ip++)] & 0xFF;
            int i1 = (ip < iEnd) ? in[(ip++)] & 0xFF : 0;
            int i2 = (ip < iEnd) ? in[(ip++)] & 0xFF : 0;
            int o0 = i0 >>> 2;
            int o1 = (i0 & 0x3) << 4 | i1 >>> 4;
            int o2 = (i1 & 0xF) << 2 | i2 >>> 6;
            int o3 = i2 & 0x3F;
            out[(op++)] = map1[o0];
            out[(op++)] = map1[o1];
            out[op] = ((op < oDataLen) ? map1[o2] : '=');
            ++op;
            out[op] = ((op < oDataLen) ? map1[o3] : '=');
            ++op;
        }
        return out;
    }

    public static String decodeString(String s) {
        return new String(decode(s));
    }

    public static byte[] decodeLines(String s) {
        char[] buf = new char[s.length()];
        int p = 0;
        for (int ip = 0; ip < s.length(); ++ip) {
            char c = s.charAt(ip);
            if ((c != ' ') && (c != '\r') && (c != '\n') && (c != '\t'))
                buf[(p++)] = c;
        }
        return decode(buf, 0, p);
    }

    public static byte[] decode(String s) {
        return decode(s.toCharArray());
    }

    public static byte[] decode(char[] in) {
        return decode(in, 0, in.length);
    }

    public static byte[] decode(char[] in, int iOff, int iLen) {
        if (iLen % 4 != 0)
            throw new IllegalArgumentException(
                    "Length of Base64 encoded input string is not a multiple of 4.");
        do
            --iLen;
        while ((iLen > 0) && (in[(iOff + iLen - 1)] == '='));

        int oLen = iLen * 3 / 4;
        byte[] out = new byte[oLen];
        int ip = iOff;
        int iEnd = iOff + iLen;
        int op = 0;
        while (ip < iEnd) {
            int i0 = in[(ip++)];
            int i1 = in[(ip++)];
            int i2 = (ip < iEnd) ? in[(ip++)] : 65;
            int i3 = (ip < iEnd) ? in[(ip++)] : 65;
            if ((i0 > 127) || (i1 > 127) || (i2 > 127) || (i3 > 127))
                throw new IllegalArgumentException(
                        "Illegal character in Base64 encoded data.");
            int b0 = map2[i0];
            int b1 = map2[i1];
            int b2 = map2[i2];
            int b3 = map2[i3];
            if ((b0 < 0) || (b1 < 0) || (b2 < 0) || (b3 < 0))
                throw new IllegalArgumentException(
                        "Illegal character in Base64 encoded data.");
            int o0 = b0 << 2 | b1 >>> 4;
            int o1 = (b1 & 0xF) << 4 | b2 >>> 2;
            int o2 = (b2 & 0x3) << 6 | b3;
            out[(op++)] = (byte) o0;
            if (op < oLen)
                out[(op++)] = (byte) o1;
            if (op < oLen)
                out[(op++)] = (byte) o2;
        }
        return out;
    }

    public static void testSigningFile() {
        String md5Hash = performSHAHash("sampleCode.txt");
        System.out.println("Original Hash\n" + md5Hash + "\n\n");

        byte[] cipherHash = encryptFile("private.key", md5Hash);
        String plainHash = decryptFile("public.key", cipherHash);
        System.out.println("Plain Hash\n" + plainHash + "\n\n");
    }

    public static void expFileName() {
        File fileName = new File("sampleCode.name.txt");

        String fileNameStr = fileName.getName();
        int extensionPos = fileNameStr.indexOf(".txt");
        String fileNameNoExt = fileNameStr.substring(0, extensionPos);
        System.out.println(fileNameNoExt);
        System.out.println(fileName.getName());
    }

    public static void verifySignedZipFile(String zipFileLoc) {
        int extensionPos = zipFileLoc.indexOf("signed.zip");
        String fileNameNoExt = zipFileLoc.substring(0, extensionPos - 1);
        int count = 0;

        int BUFFER = 10240;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");

            FileInputStream fis = new FileInputStream(zipFileLoc);
            ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
            ZipEntry entry = null;
            System.out.println("fileNameNoExt " + fileNameNoExt);

            byte[] cipherHash = new byte[2048];
            int cipherHashSize = 0;

            byte[] data = new byte[BUFFER];
            while ((entry = zis.getNextEntry()) != null) {
                System.out.println(entry.getName());

                if ((fileNameNoExt + ".zip.rsa").equals(entry.getName())) {
                    System.out.println("Reading cipherHash" + entry.getName());

                    while ((count = zis.read(cipherHash, cipherHashSize, 2048 - cipherHashSize)) != -1) {
                        cipherHashSize = count + cipherHashSize;
                        System.out.println("cipherHashSize " + cipherHashSize);
                    }
                } else {
                    System.out.println("Calculating file hash for " + entry.getName());

                    while ((count = zis.read(data, 0, BUFFER)) != -1)
                        md.update(data, 0, count);
                }
            }

            byte[] hashEncode = md.digest();
            String hashEncodeBase64 = encodeLines(hashEncode);
            String plainHashBase64 = decryptFile("public.key", cipherHash, 0, cipherHashSize);
            System.out.println("[" + hashEncodeBase64 + "]");
            System.out.println("[" + plainHashBase64 + "]");
            if (hashEncodeBase64.equals(plainHashBase64)) {
                System.out.println("The Signatures match");
                return;
            }
            System.out.println("ERROR!! The Signatures match");
            System.exit(-1);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static void signZipFile(String zipFileLoc) {
        String fileShaHash = performSHAHash(zipFileLoc);
        byte[] cipherHash = encryptFile("private.key", fileShaHash);

        int extensionPos = zipFileLoc.indexOf(".zip");
        String fileNameNoExt = zipFileLoc.substring(0, extensionPos);
        String newZipFile = fileNameNoExt + ".signed.zip";
        String signatureFile = zipFileLoc + ".rsa";
        try {
            FileOutputStream dest = new FileOutputStream(newZipFile);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));

            int BUFFER = 2048;
            int count = 0;
            byte[] data = new byte[BUFFER];

            FileInputStream fi = new FileInputStream(zipFileLoc);
            ZipEntry entry = new ZipEntry(zipFileLoc);

            out.putNextEntry(entry);
            BufferedInputStream origin = new BufferedInputStream(fi, BUFFER);
            while ((count = origin.read(data, 0, BUFFER)) != -1)
                out.write(data, 0, count);

            origin.close();

            entry = new ZipEntry(signatureFile);
            out.putNextEntry(entry);
            out.write(cipherHash);

            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void verifyZipFile(String signedZipFile) {
    }

    public static void main(String[] args) {
        verifySignedZipFile("sampleCode.name.signed.zip");
    }
}