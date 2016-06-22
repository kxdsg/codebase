package com.argus.image;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;

/**
 * 图片文件传输
 * Created by xingding on 16/6/22.
 */
public class ImageUtil {


    /**
     * 根据图片文件生成对应的base64编码
     * @param imgFile
     * @return
     */
    public static String encodeImg(String imgFile){
        //FileInputStream转成byte[]
        InputStream is = null;
        byte[] data = null;
        try {
            is = new FileInputStream(imgFile);
            data = new byte[is.available()];
            is.read(data);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);
    }

    /**
     * 根据base64编码还原成图片文件
     * @param imgStr
     * @param destImg
     */
    public static void decodeImage(String imgStr, String destImg){
        if(imgStr == null){
            return;
        }
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            byte[] b = decoder.decodeBuffer(imgStr);
            OutputStream os =  new FileOutputStream(destImg);
            os.write(b);
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        String imgStr = ImageUtil.encodeImg("/Users/xingding/Pictures/sun.jpg");
        System.out.println(imgStr);
        ImageUtil.decodeImage(imgStr, "/Users/xingding/Pictures/dest.jpg");


    }

}
