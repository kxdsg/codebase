package com.argus.util;

import com.google.gson.Gson;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

/**
 * Http请求工具类
 *
 * Created by xingding on 2016/9/2.
 */
public class HttpUtil {

    private static final HttpClient client = new HttpClient();
    private static final  Gson gson = new Gson();
    private static CloseableHttpClient httpClient = null;
    public static final String DEFAULT_ENCODING="utf-8";
    private static Logger logger = Logger.getLogger(HttpUtil.class);

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
     * get请求，使用最新的httpclient
     * @param url
     * @return
     */
    public static String get(String url){
        CloseableHttpResponse response = null;
        String result = "";
        try {
            //获取httpclient实例
            CloseableHttpClient httpclient = HttpClients.createDefault();
            //获取方法实例。GET
            HttpGet httpGet = new HttpGet(url);
            //执行方法得到响应
           response = httpclient.execute(httpGet);
            //如果正确执行而且返回值正确，即可解析
            if (response != null && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                //从输入流中解析结果
                result = EntityUtils.toString(entity, "UTF-8");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(response != null){
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
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
        if(StringUtils.isNotBlank(httpArg)){
            httpUrl = httpUrl + "?" + httpArg;
        }
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

    public static CloseableHttpClient getHttpClient() {
        if (httpClient == null) {
            httpClient = HttpClients.createDefault();
        }
        return httpClient;
    }

    public static String doGet(String url, int httpConnectionTimeout, Header[] headers, String encoding){
        CloseableHttpClient httpClient = HttpUtil.getHttpClient();
        HttpGet httpget = null;
        CloseableHttpResponse response = null;
        try {
            httpget = new HttpGet(url);

            // 设置请求和传输超时时间
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(httpConnectionTimeout)
                    .setConnectTimeout(httpConnectionTimeout)
                    .build();

            httpget.setConfig(requestConfig);


            //设置http header信息
            if(headers != null && headers.length != 0){
                httpget.setHeaders(headers);
            }

            response = httpClient.execute(httpget);

            if(encoding==null || "".equals(encoding.trim())){
                encoding=DEFAULT_ENCODING;
            }

            return EntityUtils.toString(response.getEntity(), encoding);
        } catch (ConnectTimeoutException e) {
            logger.error("http connection time out", e);
            throw new RuntimeException("http connection time out", e);
        } catch (UnsupportedEncodingException e) {
            logger.error("unsupported encoding exception", e);
            throw new RuntimeException("unsupported encoding exception", e);
        } catch (ClientProtocolException e) {
            logger.error("client protocol exception", e);
            throw new RuntimeException("client protocol exception", e);
        } catch (IOException e) {
            logger.error("io exception", e);
            throw new RuntimeException("io exception", e);
        }  finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                logger.warn("close response error", e);
            }
            try {
                if (httpget != null) {
                    httpget.releaseConnection();
                }
            } catch (Exception e) {
                logger.warn("release http connection error", e);
            }
        }
    }

    public static void main(String[] args){
        Header[] headers = new Header[]{new BasicHeader("area","900100"), new BasicHeader("user","{'name':'admin','userid':'10000'}")};
    	String ut=HttpUtil.doGet("http://host/detail.do?id=1", 2000, headers, null);
    	System.out.println(ut);
    }

}
