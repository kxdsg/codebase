package com.argus.crawler;

import com.argus.util.HttpUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 简单的java爬虫获取图片
 * 1. http请求获取网页源码
 * 2. 正则表达式获取目前url
 * 3. http请求获取图片
 * Created by xingding on 2016/9/21.
 */
public class CrawlImage {

    public static final String dirPath = "d:\\test\\img\\";

    static {
        File file = new File(dirPath);
        if(!file.exists()){
            file.mkdir();
        }
    }

    public static void main(String[] args) {
        String result = HttpUtil.get("http://www.baidu.com");
        String regex = "hidefocus.+?src=//(.+?) width"; //正则表达式，匹配百度logo图片路径
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(result);
        if(matcher.find()){
            String imgUrl = matcher.group(1);
            if(imgUrl.startsWith("www")){
                imgUrl = "http://" + imgUrl;
            }
            System.out.println("download image:" + imgUrl);
            downloadFile(imgUrl);
        }
    }

    /**
     * http请求获取图片并保存到本地文件夹
     * @param url
     */
    public static void downloadFile(String url) {
        String fileName = url.substring(url.lastIndexOf("/")+1);
        String filePath = dirPath.concat(fileName);
        CloseableHttpResponse response = null;
        BufferedOutputStream out = null;
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(url);
            response = httpClient.execute(httpGet);
            if (response != null && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                InputStream in = entity.getContent();
                out = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
                byte[] buffer = new byte[1024];
                int len = -1;
                while ((len = in.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }
                out.flush();
                out.close();
                System.out.println("saved image:" + filePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity()); //关闭HttpEntity的流(InputStream)
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
