package com.argus.util;

import com.argus.common.Constants;
import com.argus.common.ServiceRuntimeException;
import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

/**
 *  http请求公共类
 *  1. GET提交
 *  2. POST FORM提交
 *  3. POST JSON提交
 */
public class HttpClientUtil {

    private static PoolingHttpClientConnectionManager connMgr;
    private static RequestConfig requestConfig;
    private static final int MAX_TIMEOUT = 120000;
    private static CloseableHttpClient httpClient = null;
    private static Gson gson = new Gson();

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
    public static String doPost(String apiUrl, Map<String, Object> params) {
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

    /**
     * POST请求
     * Content Type: application/json
     * 1. 接收json参数
     * 2. 可传入header参数
     * @param apiUrl
     * @param jsonStr
     * @return
     */
    public static Object doPost(String apiUrl, String jsonStr,Map<String,Object> header) {
        String result = "";
        CloseableHttpResponse response = null;
        CloseableHttpClient httpClient = getHttpClient();
        HttpPost httpPost = new HttpPost(apiUrl);
        httpPost.setConfig(requestConfig);
        StringEntity req = new StringEntity(jsonStr,"utf-8");//解决中文乱码问题
        req.setContentEncoding("UTF-8");
        req.setContentType("application/json");
        httpPost.setEntity(req);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");

        //设置header参数
        if(header!=null){
            Iterator<String> iter=header.keySet().iterator();
            while(iter.hasNext()){
                String key=iter.next();
                httpPost.setHeader(key,header.get(key)==null?"":header.get(key).toString());
            }
        }

        try {
            response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, "UTF-8");
            System.out.println("http response is "+result);
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

}
