package com.argus.util;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * HTTP请求工具类
 * @author xingding
 *
 */
public class HttpUtils {

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

	public static String doPost(final String url, final String params) throws Exception {
		if (url == null || params == null) {
			throw new NullPointerException("url or params can not be null.");
		}
		HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
		conn.setRequestProperty("Content-Length", "" + params.length());
		conn.connect();
		// conn.setConnectTimeout("60000");

		OutputStream out = conn.getOutputStream();
		out.write(params.getBytes("UTF-8"));
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
				Entry<String, String> entry = (Entry<String, String>) iter.next();
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
				Iterator<Entry<String, Object>> iter = headerParams.entrySet().iterator();
				while (iter.hasNext()) {
					Entry<String, Object> entry = iter.next();
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
			Iterator<Entry<String, Object>> iter = headers.entrySet().iterator();

			while (iter.hasNext()) {
				Entry<String, Object> entry = iter.next();
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

    public static void post(String url,String json){
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

	public static void main(String[] args) {

		String result = httpGet("http://api.dataduoduo.com/cgi/token", 
				"appid=16063000742143&secret=26AA1CFD-DB08-41B5-97CD-84CFDCAE09E9",null);
		System.out.println(result);
		
	}

}