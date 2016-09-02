package com.argus.open.weibo;

import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Scanner;

/**
 * 1. 熟悉oauth协议
 * 2. 调用微博开放接口授权发布微博
 *
 * Created by xingding on 2016/9/2.
 */
public class TestWeibo {

    public static final String clientId = "App Key";
    public static final String clientSecret = "App Secret";
    public static final String redirectUri = "Redirect Uri";
    public static final String authorizeUrl = "https://api.weibo.com/oauth2/authorize";
    public static final String accessTokenUrl = "https://api.weibo.com/oauth2/access_token";
    public static final String statusUrl = "https://api.weibo.com/2/statuses/update.json";


    public static void main(String[] args) {
        System.out.println("请点击链接登录授权");
        //1.访问授权页面，用户登录，获取code
        System.out.println(authorizeUrl+"?client_id="+clientId+"&response_type=code&redirect_uri="+redirectUri+"&forcelogin=true");
        //2. 获取access token
        System.out.println("请输入code:");
        String code=new Scanner(System.in).next();
        getAccessToken(code);
        //3. 发布微博
        System.out.println("请输入access token:");
        String accessToken =new Scanner(System.in).next();
        updateStatus("小机器人发布测试，位置：北京上地9街",accessToken);
    }

    /**
     * 调用接口获取accessToken
     * @param code
     */
    public static void getAccessToken(String code)
    {

        String parameters="client_id=" +clientId+"&client_secret=" +clientSecret+
                "&grant_type=authorization_code" +"&redirect_uri=" +redirectUri+"&code="+code;
        postUrl(accessTokenUrl, parameters);
    }

    /**
     * 更新状态,更多接口请参考 http://open.weibo.com/
     * @param content
     * @param accessToken
     */
    public static void updateStatus(String content, String accessToken){
        String parameters = "status="+content+"&access_token="+accessToken;
        postUrl(statusUrl,parameters);
        System.out.println("发布微博成功");
    }

    /**
     * Http Post请求
     * @param url
     * @param parameters
     */
    public static void postUrl(String url,String parameters)
    {
        try
        {
            trustAllHttpsCertificates();
            URLConnection conn = new URL(url).openConnection();
            conn.setDoOutput(true);// 表示需要往请求链接注入参数
            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
            out.write(parameters);
            out.flush();
            out.close();
            //接收返回
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null)
            {
                System.out.println(line);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 设置信任所有的http证书
     * @throws Exception
     */
    private static void trustAllHttpsCertificates() throws Exception
    {
        javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
        trustAllCerts[0] = new X509TrustManager()
        {
            public X509Certificate[] getAcceptedIssuers()
            {
                return null;
            }
            public void checkServerTrusted(X509Certificate[] arg0, String arg1)
                    throws CertificateException
            {}
            public void checkClientTrusted(X509Certificate[] arg0, String arg1)
                    throws CertificateException
            {}
        };
        javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, null);
        javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    }

}
