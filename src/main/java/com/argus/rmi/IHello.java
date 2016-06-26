package com.argus.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by xingding on 16/6/26.
 * 接口必须继承Remote
 */
public interface IHello extends Remote {

    void sayHello() throws RemoteException;
    void sayHelloToSby(String name) throws RemoteException;
}
