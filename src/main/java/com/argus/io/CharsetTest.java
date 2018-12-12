package com.argus.io;

import com.argus.util.HexUtil;

import java.io.*;

/**
 *
 * 编码测试
 * Created by xingding on 2016/11/12.
 */
public class CharsetTest {

    public static void main(String[] args)  {
        CharsetTest inst = new CharsetTest();
        inst.testJavaEncode();
    }

    /**
     * IO流中的编码
     */
    private void testFileIO(){
        String file = "/Users/xingding/testdata/files/stream.txt";
        String charset = "UTF-8";
        try {
            //文件字节流
            FileOutputStream fos = new FileOutputStream(file);
            //字节转换成写字符流
            OutputStreamWriter writer  = new OutputStreamWriter(fos,charset);
            try {
                writer.write("我爱你，中国");
            } finally {
                writer.close();
            }

            //读文件字节流
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader reader = new InputStreamReader(fis, charset);
            StringBuffer sb = new StringBuffer();
            char[] buffer = new char[64]; //字符缓冲
            int count = 0;
            try {
                while ((count=reader.read(buffer))!=-1){
                    sb.append(buffer,0,count);
                }
                System.out.println(sb.toString());
            } finally {
                reader.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void testJavaEncode(){
        try {
            String name = "君山";
            toHex(name.toCharArray());
            toHex(name.getBytes("ISO-8859-1"));
            toHex(name.getBytes("GBK"));
            toHex(name.getBytes("UTF-16")); //java内存编码采用UTF-16编码
            toHex(name.getBytes("UTF-8")); //适合网络传输
            toHex(name.getBytes());//取决于当前的运行环境,例如ide,tomcat...
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void toHex(char[] b) {
        for (int i = 0; i < b.length; i++) {
            // %x表示将整数格式化为16进制整数
            System.out.printf("%x " , (int)b[i]);
        }
        System.out.println();
    }

    public static void toHex(byte[] b) {
        for (int i = 0; i < b.length; i++) {
            System.out.printf("%x " , b[i]);
        }
        System.out.println();
    }
}
