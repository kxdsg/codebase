package com.argus.crypto;

import com.argus.util.LoggerUtil;
import com.argus.file.FileCrawler;
import com.argus.file.FileUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.argus.file.ZipUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

/**
 * Created by IntelliJ IDEA.
 * User: xingding
 * Date: 3/1/12
 * Time: 4:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class RSAUtil {
    private static LoggerUtil logger = LoggerUtil.getInstance(RSAUtil.class);
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

    public static boolean verifySignedZipFile(String zipFileLoc, String publicKeyFile) {
        logger.info(new Object[]{"[", zipFileLoc, "] verifying using public key [", publicKeyFile, "]"});
        String fileNameNoExt = FileUtil.getFileName(zipFileLoc);

        int count = 0;
        int BUFFER = 10240;

        Map sha256Map = new HashMap();

        FileInputStream fis = null;
        ZipInputStream zis = null;
        BufferedReader br = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");

            fis = new FileInputStream(zipFileLoc);
            zis = new ZipInputStream(new BufferedInputStream(fis));
            ZipEntry entry = null;
            logger.debug(new Object[]{"[", fileNameNoExt, "] file no extension"});

            byte[] cipherHash = (byte[]) null;
            int cipherHashSize = 0;

            byte[] data = new byte[BUFFER];

            int noFiles = 0;

            while ((entry = zis.getNextEntry()) != null) {
                if (logger.isDebug()) {
                    logger.debug(new Object[]{"[", entry.getName(), "] processing entry in file"});
                }

                String entryName = entry.getName();
                if (entry.isDirectory()) {
                    continue;
                }

                if ((fileNameNoExt + ".zip.rsa").equals(entryName)) {
                    cipherHash = new byte[2048];

                    logger.debug(new Object[]{"[", entryName, "] found the cipherText MD5 hash file"});
                    while ((count = zis.read(cipherHash, cipherHashSize, 2048 - cipherHashSize)) != -1) {
                        cipherHashSize = count + cipherHashSize;
                        logger.debug(new Object[]{"[", entryName, "] read bytes of key " + cipherHashSize});
                    }
                } else if ((fileNameNoExt + ".zip.sha256").equals(entryName)) {
                    logger.debug(new Object[]{"[", entryName, "] found the src.zip hash file"});

                    br = new BufferedReader(new InputStreamReader(zis));
                    String lineFeed = null;
                    System.out.println("SHA 256 hash");
                    while ((lineFeed = br.readLine()) != null) {
                        System.out.println(lineFeed);
                    }

                    br = null;
                } else {
                    logger.debug(new Object[]{"[", entryName, "] found a normal file"});
                    while ((count = zis.read(data, 0, BUFFER)) != -1)
                        md.update(data, 0, count);

                    ++noFiles;
                }
            }

            logger.debug(new Object[]{"Checking if there is only one file in the zip"});
            if (noFiles != 1) {
                logger.debug(new Object[]{"The no. of files != 1!"});
                logger.debug(new Object[]{"[", zipFileLoc, "] VERIFICATION FAILED!"});
                return false;
            }

            byte[] hashEncode = md.digest();
            String hashEncodeBase64 = encodeLines(hashEncode);
            String plainHashBase64 = decryptFile(publicKeyFile, cipherHash, 0, cipherHashSize);
            logger.debug(new Object[]{"[" + hashEncodeBase64 + "] <--- file hash"});
            logger.debug(new Object[]{"[" + plainHashBase64 + "] <-- RSA decrypted hash"});

            if (hashEncodeBase64.equals(plainHashBase64)) {
                logger.info(new Object[]{"[", zipFileLoc, "] VERIFICATION PASS!"});
                return true;
            }
            logger.info(new Object[]{"[", zipFileLoc, "] VERIFICATION FAILED!"});
            return false;
        } catch (FileNotFoundException e) {
            logger.error(e, new Object[]{"[", zipFileLoc, "] cannot be found"});
        } catch (IOException e) {
            logger.error(e, new Object[]{"[", zipFileLoc, "] encounted IOEXception"});
        } catch (NoSuchAlgorithmException e) {
            logger.error(e, new Object[]{"[", zipFileLoc, "] Algorithmn cannot be found"});
        } finally {
            if (fis != null)
                try {
                    fis.close();
                } catch (IOException e) {
                    logger.error(e, new Object[]{" Error encounterd while closing FileInputStream "});
                }

            if (zis != null)
                try {
                    zis.close();
                } catch (IOException e) {
                    logger.error(e, new Object[]{" Error encounterd while closing ZipInputStream "});
                }

            if (br != null)
                try {
                    br.close();
                } catch (IOException e) {
                    logger.error(e, new Object[]{" Error encounterd while closing BufferedReader "});
                }
        }

        logger.info(new Object[]{"[", zipFileLoc, "] VERIFICATION FAILED!"});
        return false;
    }

    public static void signZipFile(String zipFileLoc, String privateKeyFile) throws Exception {
        logger.info(new Object[]{"[", zipFileLoc, "] signing using private key [", privateKeyFile, "]"});

        String fileShaHash = performSHAHash(zipFileLoc);
        logger.debug(new Object[]{"[", zipFileLoc, "] SHA hash for file [", fileShaHash, "]"});
        byte[] cipherHash = encryptFile(privateKeyFile, fileShaHash);

        String fileNameNoExt = FileUtil.getFileName(zipFileLoc);
        String newZipFile = FileUtil.getFileDir(zipFileLoc) + fileNameNoExt + ".szip";
        String rsaSignatureFile = FileUtil.getFileName(zipFileLoc) + ".zip.rsa";
        String srcHashListFile = FileUtil.getFileName(zipFileLoc) + ".zip.sha256";

        logger.debug(new Object[]{"[", newZipFile, "] creating and adding encrypted signature file [", rsaSignatureFile, "]"});

        boolean isSrcZipExist = ZipUtil.isExistZip(zipFileLoc, "src.zip");
        if (!(isSrcZipExist)) {
            throw new Exception("[" + zipFileLoc + "]/src.zip does not exist!");
        }

        FileOutputStream dest = null;
        ZipOutputStream out = null;
        PrintStream outp = null;
        try {
            dest = new FileOutputStream(newZipFile);
            out = new ZipOutputStream(new BufferedOutputStream(dest));

            int BUFFER = 2048;
            int count = 0;
            byte[] data = new byte[BUFFER];

            FileInputStream fi = new FileInputStream(zipFileLoc);
            ZipEntry entry = new ZipEntry(FileUtil.getFileName(zipFileLoc) + ".zip");

            out.putNextEntry(entry);
            BufferedInputStream origin = new BufferedInputStream(fi, BUFFER);
            while ((count = origin.read(data, 0, BUFFER)) != -1)
                out.write(data, 0, count);

            origin.close();

            entry = new ZipEntry(rsaSignatureFile);
            out.putNextEntry(entry);
            out.write(cipherHash);

            if (isSrcZipExist) {
                entry = new ZipEntry(srcHashListFile);
                out.putNextEntry(entry);
                outp = new PrintStream(out);

                long randomNum = System.currentTimeMillis();

                String tmpDir = "c:\\temp\\" + randomNum + "\\";

                ZipUtil.unzip(zipFileLoc, tmpDir, "src.zip");
                ZipUtil.unzip(tmpDir + "src.zip", tmpDir);
                FileUtil.delFile(tmpDir + "src.zip");
                FileCrawler fileCrawler = new FileCrawler("c:\\temp\\" + randomNum);
                File curDir = null;

                while ((curDir = fileCrawler.getNextDir()) != null) {
                    List fileList = fileCrawler.getFileList();
                    for (Iterator localIterator = fileList.iterator(); localIterator.hasNext();) {
                        String hashString;
                        File curFile = (File) localIterator.next();
                        String shortFilePath = FileUtil.stripDir(curFile.getAbsolutePath(), tmpDir);
                        try {
                            hashString = Hex.encodeHexString(Base64.decodeBase64(HashUtil.genSHA256Hash(curFile.getAbsolutePath())));
                        } catch (NoSuchAlgorithmException e) {
                            logger.error(new Object[]{"Unable to obtain SHA256 Algorithm"});
                            throw e;
                        }
                        logger.debug(new Object[]{hashString, "   ", shortFilePath});
                        outp.println(hashString + "   " + shortFilePath);
                        outp.flush();
                    }
                }

                FileUtil.delDir(tmpDir);
            }

            out.flush();
            out.close();
            logger.info(new Object[]{"[", newZipFile, "] created "});
        } catch (FileNotFoundException e) {
            logger.error(e, new Object[]{" [", newZipFile, "] cannot be created"});
            throw e;
        } catch (IOException e) {
            logger.error(e, new Object[]{" [", newZipFile, "] IOError while creating file."});
            throw e;
        } finally {
            if (dest != null)
                try {
                    dest.close();
                } catch (IOException e) {
                    logger.error(e, new Object[]{" Error encounterd while closing OutputStream "});
                }


            if (out != null)
                try {
                    out.close();
                } catch (IOException e) {
                    logger.error(e, new Object[]{" Error encounterd while closing ZipOutputStream "});
                }


            if (outp != null) {
                outp.flush();
                outp.close();
            }
        }
    }

    public static void generateKey() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        generateKey("public.key", "private.key");
    }

    public static void generateKey(String publicKeyFile, String privateKeyFile)
            throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        logger.info(new Object[]{"[", publicKeyFile, "] : public key [", privateKeyFile, "]"});
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

            saveToFile(publicKeyFile, pub.getModulus(), pub.getPublicExponent());
            saveToFile(privateKeyFile, priv.getModulus(), priv.getPrivateExponent());
        } catch (NoSuchAlgorithmException e) {
            logger.error(e, new Object[]{" Unable to obtain algorithm RSA "});
            throw e;
        } catch (InvalidKeySpecException e) {
            logger.error(e, new Object[]{" Unable to create Public and Private keys"});
            throw e;
        } catch (IOException e) {
            logger.error(e, new Object[]{" IOException encountered while saving keys"});
            throw e;
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

    public static void main(String[] args) {
        try {
            verifySignedZipFile("samplePkg.szip", "public.key");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}