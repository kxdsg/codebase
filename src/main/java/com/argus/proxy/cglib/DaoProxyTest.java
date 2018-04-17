package com.argus.proxy.cglib;

import net.sf.cglib.proxy.Enhancer;

/**
 * @author xingding
 * @date 2017/10/17.
 */
public class DaoProxyTest {

    public static void main(String[] args) {
        DaoProxy daoProxy = new DaoProxy();

//        Enhancer enhancer = new Enhancer();
//        enhancer.setSuperclass(DaoImpl.class);
//        enhancer.setCallback(daoProxy);
//        Dao dao = (DaoImpl)enhancer.create();

        Dao dao = (DaoImpl)daoProxy.getProxy(DaoImpl.class);
        dao.insert();

    }
}
