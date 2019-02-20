package com.argus.ws;

import org.codehaus.xfire.client.Client;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

import java.net.URL;

/**
 * 接口测试类
 * xfire client call
 * @author xingding
 *
 */

public class XFireClient {

	public static void main(String[] args) {
		String response = invoke();
        System.out.println("response:" + response);
        //解析返回xml,根据接口返回报文编写解析代码
		/*
        try {
            Document doc = DocumentHelper.parseText(response);
            Element root = doc.getRootElement();//获取根节点
            Node result = root.selectSingleNode("/root/result");
            System.out.println(result.getText());
        } catch (Exception e) {
            e.printStackTrace();
        }
        */
    }
		
	public static String invoke(){
		//参数初始化
        Client client = null;
        Object[] objects = null;
        String resXml = "";//接口返回xml
        //第一个参数必传接口方法名
        try {
            client = new Client(new URL("http://ws.webxml.com.cn/WebServices/MobileCodeWS.asmx?wsdl"));
            objects = client.invoke("getMobileCodeInfo", new Object[]{"18662494898",""});
            resXml = objects[0].toString();
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return resXml;
	}
	
}
