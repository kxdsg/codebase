package com.argus.ws;

import org.apache.cxf.jaxws.JaxWsServerFactoryBean;

/**
 * Created by xingding on 18/5/5.
 */
public class CxfServer {

    public static void main(String[] args) {
        Business business = new BusinessImpl();
        JaxWsServerFactoryBean serverFactoryBean = new JaxWsServerFactoryBean();
        serverFactoryBean.setServiceClass(Business.class);
        serverFactoryBean.setAddress("http://localhost:8188/business");
        serverFactoryBean.setServiceBean(business);
        serverFactoryBean.create();
    }
}
