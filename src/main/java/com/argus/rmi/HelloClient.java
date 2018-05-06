package com.argus.rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Created by xingding on 16/6/26.
 * 基于Java自身技术实现远程调用方式的系统间通信,java.rmi
 * 与RPC的区别
 * 1. RPC: Remote Procedure Call Protocol 远程过程调用协议，
 * 它是一种通过网络从远程计算机程序上请求服务，而不需要了解底层网络技术的协议。
 * 简言之，RPC使得程序能够像访问本地系统资源一样，去访问远端系统资源。
 * 典型的RPC框架包含：RMI，Hessian, Thrift等
 *
 * 2. RMI: Remote Method Invocation 远程方法调用，
 *  利用java.rmi包实现，基于Java远程方法协议(Java Remote Method Protocol) 和java的原生序列化。
 */
public class HelloClient {

    public static void main(String[] args) {
        try {
            IHello rhello = (IHello)Naming.lookup("rmi://localhost:8888/RHello");
            System.out.println(rhello.getClass());
            rhello.sayHello();
            rhello.sayHelloToSby("argus");
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
