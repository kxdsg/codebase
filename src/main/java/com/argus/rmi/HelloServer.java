package com.argus.rmi;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 * Created by xingding on 16/6/26.
 */
public class HelloServer {

    public static void main(String[] args) {
        try {
            //实现实例
            IHello rhello = new IHelloImpl();

            //本地主机上的远程对象注册表Registry的实例，并指定端口为8888
            LocateRegistry.createRegistry(8888);
            //把远程对象注册到RMI注册服务器上，并命名为RHello
            //绑定的URL标准格式为：rmi://host:port/name
            Naming.bind("rmi://localhost:8888/RHello",rhello);
            System.out.println("远程对象绑定成功!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
