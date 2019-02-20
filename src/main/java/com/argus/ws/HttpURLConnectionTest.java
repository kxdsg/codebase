package com.argus.ws;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by xingding on 19/2/20.
 */
public class HttpURLConnectionTest {
    public static void main(String[] args) throws Exception {
        soapRequest("18612345678","");
    }

    /**
     * 发送SOAP格式请求
     * @param phone
     * @param userID
     * @throws IOException
     */
    public static void soapRequest(String phone, String userID) throws IOException{
        //第一步：创建服务地址
        URL url = new URL("http://ws.webxml.com.cn/WebServices/MobileCodeWS.asmx?wsdl");
        //第二步：打开一个通向服务地址的连接
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        //第三步：设置参数
        //3.1发送方式设置：POST必须大写
        connection.setRequestMethod("POST");
        //3.2设置数据格式：content-type
        connection.setRequestProperty("content-type", "text/xml;charset=utf-8");
        //3.3设置输入输出，因为默认新创建的connection没有读写权限，
        connection.setDoInput(true);
        connection.setDoOutput(true);

        //第四步：组织SOAP数据，发送请求
        String soapXML = getSoapRequest(phone, userID);
        System.out.println("request: " + soapXML);
        //将信息以流的方式发送出去
        OutputStream os = connection.getOutputStream();
        os.write(soapXML.getBytes());
        //第五步：接收服务端响应，打印
        int responseCode = connection.getResponseCode();
        if(200 == responseCode){//表示服务端响应成功
            //获取当前连接请求返回的数据流
            InputStream is = connection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            StringBuilder sb = new StringBuilder();
            String temp = null;
            while(null != (temp = br.readLine())){
                sb.append(temp);
            }

            /**
             * 打印结果
             */
            System.out.println("response: " + sb.toString());

            is.close();
            isr.close();
            br.close();
        }
        os.close();
    }

    private static String getSoapRequest(String mobileCode, String userID) {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                + "\n"
                + "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""
                + " "
                + "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\""
                + " "
                + "xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
                + "\n"
                + "<soap:Body>" + "\n"
                + "<getMobileCodeInfo" + " "
                + "xmlns=\"http://WebXml.com.cn/\">" + "\n"
                + "<mobileCode>" + mobileCode + "</mobileCode>" + "\n"
                + "<userID>"+userID+"</userID>" + "\n"
                + "</getMobileCodeInfo>" + "\n"
                + "</soap:Body>" + "\n"
                + "</soap:Envelope>"
        );
        return sb.toString();
    }

    /**
     * GET方式请求
     * @param mobileCode
     * @param userID
     * @throws Exception
     */
    public static void getRequest(String mobileCode,String userID) throws Exception{
        URL url =new URL("http://ws.webxml.com.cn/WebServices/MobileCodeWS.asmx/getMobileCodeInfo?mobileCode="+mobileCode+"&userID="+userID);
        HttpURLConnection counc = (HttpURLConnection) url.openConnection();
        counc.setRequestMethod("GET");
        if(counc.getResponseCode()==HttpURLConnection.HTTP_OK){
            InputStream is=counc.getInputStream();
            ByteArrayOutputStream boas=new ByteArrayOutputStream();
            byte[] buffer=new byte[1024];
            int len;
            if((len=is.read(buffer)) != -1){
                boas.write(buffer, 0, len);
            }
            System.out.println("GET请求获取的数据:"+boas.toString());
            boas.close();
            is.close();

        }
    }
}
