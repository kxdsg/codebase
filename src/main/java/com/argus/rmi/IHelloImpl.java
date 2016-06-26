package com.argus.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by xingding on 16/6/26.
 * 实现类继承了UnicastRemoteObject,那么这个对象就是远程对象,客户端每次在调用的时候,lookup返回的对象类型是com.sun.proxy.$Proxy0
 * 实际是Stub存根对象
 */
public class IHelloImpl extends UnicastRemoteObject implements IHello {

    /**
     * 这个默认的构造方法必须有,因为继承的UnicastRemoteObject的构造方法抛出了RemoteException
     * @throws RemoteException
     */
    public IHelloImpl() throws RemoteException{}

    public void sayHello() throws RemoteException {
        System.out.println("hello, rmi");
    }

    public void sayHelloToSby(String name) throws RemoteException {
        System.out.println("hello, " + name);
    }
}
