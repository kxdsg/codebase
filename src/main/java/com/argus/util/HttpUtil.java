package com.argus.util;

import com.google.gson.Gson;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * Http请求工具类
 *
 * Created by xingding on 2016/9/2.
 */
public class HttpUtil {

    private static final HttpClient client = new HttpClient();
    private static final  Gson gson = new Gson();

    /**
     * 利用post请求上传文件,使用commons-httpclient, MultipartRequestEntity
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

    /**
     * post请求,使用commons-httpclient, StringRequestEntity
     * @param url
     * @param params
     * @return
     */
    public static String httpPost (String url, Map<String, Object> params) {
        String rtnString = null;
        PostMethod post = new PostMethod(url);
        try {
            String str = gson.toJson(params);
            System.out.println(str);
            RequestEntity re = new StringRequestEntity(str, "application/json", "utf-8");
            post.setRequestEntity(re);
            client.executeMethod(post);
            rtnString = post.getResponseBodyAsString();
            System.out.println("Access System authenticate, Status: " + post.getStatusCode());
            System.out.println("Access System authenticate, Response: " + rtnString);

        } catch (Exception e) {
            e.printStackTrace();
            // 调用异常, 返回异常报文
        } finally {
            post.releaseConnection();
        }
        return rtnString;
    }

    /**
     * post请求,使用jdk, 请求数据格式是json
     * @param url
     * @param json
     */
    public static void doPost(String url,String json){
        try {
            URL u = new URL(url);
            URLConnection uc = u.openConnection();
            HttpURLConnection connection = (HttpURLConnection) uc;
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type","application/json; charset=utf-8");
            connection.setRequestProperty("Content-Length",String.valueOf(json.length()));
            OutputStream out = connection.getOutputStream();
            Writer wout = new OutputStreamWriter(out,"utf-8");
            wout.write(json);
            wout.flush();
            wout.close();
            InputStream in =  connection.getInputStream();
            ByteArrayOutputStream bos= new ByteArrayOutputStream();
            byte[]b= new byte[1024];
            int i;
            while (( i=in.read(b))!=-1) bos.write(b, 0, i);
            in.close();
            System.out.println(new String(bos.toByteArray(),"utf-8"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * get请求, 使用jdk
     * @param httpUrl
     * @param httpArg
     * @return
     */
    public static String doGet(String httpUrl, String httpArg){
        BufferedReader reader = null;
        String result = null;
        StringBuffer sbf = new StringBuffer();
        httpUrl = httpUrl + "?" + httpArg;
        System.out.println(httpUrl);

        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestMethod("GET");
            // 填入apikey到HTTP header
//	        connection.setRequestProperty("apikey",  "您自己的apikey");
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }
            reader.close();
            result = sbf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


}
