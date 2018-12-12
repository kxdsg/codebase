package com.argus.util;

import com.argus.common.Constants;
import com.argus.common.ServiceRuntimeException;
import com.google.gson.Gson;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
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
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Http请求工具类
 *
 * Created by xingding on 2016/9/2.
 */
public class HttpUtil {

    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);
    private static final HttpClient client = new HttpClient();
    private static final  Gson gson = new Gson();
    private static CloseableHttpClient httpClient = null;
    public static final String DEFAULT_ENCODING="utf-8";

    private static PoolingHttpClientConnectionManager connMgr;
    private static RequestConfig requestConfig;
    private static final int MAX_TIMEOUT = 120000;

    static {
        // 设置连接池
        connMgr = new PoolingHttpClientConnectionManager();
        // 设置连接池大小
        connMgr.setMaxTotal(100);
        connMgr.setDefaultMaxPerRoute(connMgr.getMaxTotal());

        RequestConfig.Builder configBuilder = RequestConfig.custom();
        // 设置连接超时
        configBuilder.setConnectTimeout(MAX_TIMEOUT);
        // 设置读取超时
        configBuilder.setSocketTimeout(MAX_TIMEOUT);
        // 设置从连接池获取连接实例的超时
        configBuilder.setConnectionRequestTimeout(MAX_TIMEOUT);
        // 在提交请求之前 测试连接是否可用
        configBuilder.setStaleConnectionCheckEnabled(true);
        requestConfig = configBuilder.build();
    }
    /**
     * 生产HttpClient实例
     * 公开，静态的工厂方法，需要使用时才去创建该单体
     */
    public static CloseableHttpClient getHttpClient() {
        if (httpClient == null) {
            httpClient = HttpClients.createDefault();
        }
        return httpClient;
    }

    /**
     * GET请求 url为请求的路径 带参数
     * @param url 请求路径
     * @return String
     */
    public static String doGet(String url) {
        CloseableHttpResponse response = null;
        String result=null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        try {
            response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
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
     * 发送 POST 请求（HTTP），K-V形式
     * Content-Type: application/x-www-form-urlencoded; charset=UTF-8
     * @param apiUrl API接口URL（必填项）
     * @param params 参数map
     * @return HttpResponse对象
     */
    public static Object doPost(String apiUrl, Map<String, Object> params) {
        return doPost(apiUrl,params,null);
    }

    /**
     * GET请求 url为请求的路径 带header参数
     * @param url 请求路径
     * @return String
     */
    public static String doGet(String url ,Map<String,Object> headers) {
        CloseableHttpResponse response = null;
        String result=null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        //设置header 参数
        if (headers != null) {
            Set<String> keys = headers.keySet();
            for (Iterator<String> i = keys.iterator(); i.hasNext();) {
                String key = (String) i.next();
                httpGet.addHeader(key, headers.get(key).toString());
            }
        }
        try {
            response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, "UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
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
     *Content-Type: application/x-www-form-urlencoded; charset=UTF-8
     * @param apiUrl
     * @param params
     * @param headers
     * @return
     */
    public static Object doPost(String apiUrl, Map<String,Object> params,Map<String,Object> headers) {
        String result = "";
        CloseableHttpResponse response = null;
        CloseableHttpClient httpClient = getHttpClient();
        HttpPost httpPost = new HttpPost(apiUrl);
        httpPost.setConfig(requestConfig);
        List<NameValuePair> pairList = new ArrayList<NameValuePair>(params.size());
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            if(entry.getValue() != null && !"".equals(entry.getValue())){
                NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue().toString());
                pairList.add(pair);
            }
        }
        httpPost.setEntity(new UrlEncodedFormEntity(pairList, Charset.forName("UTF-8")));
        //设置header 参数
        if (headers != null) {
            Set<String> keys = headers.keySet();
            for (Iterator<String> i = keys.iterator(); i.hasNext();) {
                String key = (String) i.next();
                httpPost.addHeader(key, headers.get(key).toString());
            }
        }
        try {
            response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, "UTF-8");
            if(statusCode != HttpStatus.SC_OK){
                throw new ServiceRuntimeException(Constants.RTN_CODE_ERROR, Constants.RTN_MESSAGE_ERROR);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServiceRuntimeException(Constants.RTN_CODE_ERROR, Constants.RTN_MESSAGE_ERROR);
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public static String httpGet(String url) {
        String rtnString = null;
        try {
            GetMethod getMethod = new GetMethod(url);
            HttpClient client = new HttpClient();
            client.executeMethod(getMethod);
            rtnString = getMethod.getResponseBodyAsString();
            System.out.println("Status Code: " + getMethod.getStatusCode());
            System.out.println("Response: " + getMethod.getResponseBodyAsString());
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rtnString;
    }

    public static String postJsonString(String url, String jsonParam) throws Exception {
        if (url == null || jsonParam == null) {
            throw new NullPointerException("url or params can not be null.");
        }
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setRequestProperty("Content-Length", "" + jsonParam.length());
        conn.connect();
        // conn.setConnectTimeout("60000");

        OutputStream out = conn.getOutputStream();
        out.write(jsonParam.getBytes("UTF-8"));
        out.flush();
        out.close();
		/*
		 * if(conn.getResponseCode() == -1){ return null; } else if(con)
		 */
        int code = conn.getResponseCode();
        InputStream input = conn.getInputStream();
        byte[] bytes = new byte[1024];
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int len = -1;
        while ((len = input.read(bytes)) != -1) {
            bos.write(bytes, 0, len);
        }
        input.close();
        bos.flush();
        String rtnStr = bos.toString("UTF-8");
        bos.close();
        return rtnStr;
    }

    /**
     * refer to apache http client util to do post request
     *
     * @param url
     * @param params
     */
    public static void httpPost(String url, Map params) {
        try {
            // 1. create http client object
            HttpClient httpClient = new HttpClient();
            // 2. set content charset, default is utf-8
            httpClient.getParams().setContentCharset("UTF-8");
            // 3. create post method object, parameter is url
            PostMethod postMethod = new PostMethod(url);
            Iterator iter = params.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, String> entry = (Map.Entry<String, String>) iter.next();
                // 4. set form parameters
                postMethod.addParameter(entry.getKey(), entry.getValue());
            }
            // 5. execute post method
            httpClient.executeMethod(postMethod);
            // 6. get response body data
            String responseMsg = postMethod.getResponseBodyAsString().trim();
            System.out.println("[Response]:" + responseMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String httpGet(String address, String urlParams, Map headerParams){
        StringBuffer sb = new StringBuffer();
        try {

            URL url = new URL(address + "?" + urlParams);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (headerParams != null && headerParams.size() > 0) {
                Iterator<Map.Entry<String, Object>> iter = headerParams.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry<String, Object> entry = iter.next();
                    conn.setRequestProperty(entry.getKey(), entry.getValue().toString());
                }
            }
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
            String line = null;
            while((line = reader.readLine())!=null){
                sb.append(line).append("\r\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally{

        }
        return sb.toString();
    }

    /**
     * 东区服务平台
     *
     * http请求
     *
     * @param url
     *            请求地址
     * @param method
     *            请求方法
     * @param params
     *            参数
     * @param headers
     *            请求头
     * @param encoding
     *            编码
     * @param timeout
     *            链接超时时间（毫秒）
     * @return
     * @throws IOException
     */
    public static String request(String url, String method, String params, Map<String, Object> headers, String encoding,
                                 int timeout) throws IOException {
        URL u = new URL(url);
        URLConnection uc = u.openConnection();
        HttpURLConnection connection = (HttpURLConnection) uc;
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestMethod(method);
        connection.setConnectTimeout(timeout);

        if (headers != null && headers.size() > 0) {
            Iterator<Map.Entry<String, Object>> iter = headers.entrySet().iterator();

            while (iter.hasNext()) {
                Map.Entry<String, Object> entry = iter.next();
                connection.setRequestProperty(entry.getKey(), entry.getValue().toString());
            }
        }

        OutputStream out = connection.getOutputStream();

        if (params != null) {
            out.write(params.getBytes(encoding));
        }

        out.flush();
        out.close();
        InputStream in = connection.getInputStream();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] b = new byte[1024];
        int i;
        while ((i = in.read(b)) != -1)
            bos.write(b, 0, i);
        in.close();
        return new String(bos.toByteArray(), encoding);
    }

    public static String post(String url,String json){
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
            return new String(bos.toByteArray(),"utf-8");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String get(String httpUrl, String httpArg){
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
    public static String httpPostJson (String url, Map<String, Object> params) {
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

    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");

            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    public static void main(String[] args){
        Header[] headers = new Header[]{new BasicHeader("area","900100"), new BasicHeader("user","{'name':'admin','userid':'10000'}")};
    	String ut=HttpUtil.doGet("http://host/detail.do?id=1", 2000, headers, null);
    	System.out.println(ut);
//        String result = httpGet("http://api.dataduoduo.com/cgi/token",
//                "appid=16063000742143&secret=26AA1CFD-DB08-41B5-97CD-84CFDCAE09E9",null);
//        System.out.println(result);
    }

}
