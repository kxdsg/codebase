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

	public static final String SERVICE_URL = "";

	public static void main(String[] args) {
		String result = getResult();
        System.out.println(result);
    }
		
	public static String getResult(){
		//参数初始化
        Client client = null;
        Object[] objects = null;
        String resXml = "";//接口返回xml
        //第一个参数必传接口方法名
        try {
            client = new Client(new URL(SERVICE_URL));
            objects = client.invoke("methodName1", new Object[]{"param1", "param2"});
            resXml = objects[0].toString();
            System.out.println(resXml);
        } catch (Exception e) {
        	e.printStackTrace();
        }

        /*
        解析返回xml
         */
        try {
            Document doc = DocumentHelper.parseText(resXml);
            Element root = doc.getRootElement();//获取根节点
            Node result = root.selectSingleNode("/root/result");
            System.out.println(result.getText());
            return result.getText();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
	}
	
}
