package com.argus.util;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;

import java.io.File;

/**
 * Http请求工具类
 *
 * Created by xingding on 2016/9/2.
 */
public class HttpUtil {

    private static final HttpClient client = new HttpClient();

    /**
     * 利用post请求上传文件
     */
    public static void postFile(){
        String apiUrl = "http://domain/service/upload"; //服务地址
        try {
            PostMethod postMethod = new PostMethod(apiUrl);
            //设置文本请求参数
            postMethod.setParameter("appid","app123");
            postMethod.setParameter("password","pass123");
            //文件类
            FilePart filePart = new FilePart("image", new File("D:/test/image1.jpg"));
            Part[] parts = new Part[]{filePart};
            MultipartRequestEntity multipartRequestEntity = new MultipartRequestEntity(parts, postMethod.getParams());
            postMethod.setRequestEntity(multipartRequestEntity);
            client.executeMethod(postMethod);
            System.out.println("Status: " + postMethod.getStatusCode());
            System.out.println("Response: " + postMethod.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
