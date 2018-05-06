package com.argus.ws;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

/**
 * Created by xingding on 18/5/5.
 */
public class CxfClient {
    public static void main(String[] args) {
        JaxWsProxyFactoryBean factoryBean = new JaxWsProxyFactoryBean();
        factoryBean.setServiceClass(Business.class);
        factoryBean.setAddress("http://localhost:8188/business");
        Business business = (Business)factoryBean.create();
        String response = business.echo("haha");
        System.out.println(response);
    }
}
