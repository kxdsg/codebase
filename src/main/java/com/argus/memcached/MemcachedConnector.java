package com.argus.memcached;

import net.spy.memcached.MemcachedClient;

import java.net.InetSocketAddress;
import java.util.concurrent.Future;

/**
 * Created by xingding on 18/12/11.
 */
public class MemcachedConnector {

    public static void main(String[] args) {
        try{
            // 连接本地的 Memcached 服务
            MemcachedClient mcc = new MemcachedClient(new InetSocketAddress("127.0.0.1", 11211));
            System.out.println("Connection to server sucessful.");

            // 存储数据
            Future fo = mcc.set("runoob", 900, "Free Education");
            // 查看存储状态
            System.out.println("set status:" + fo.get());
            // 输出值
            System.out.println("runoob value in cache - " + mcc.get("runoob"));

            mcc.add("add",100,"value1");
            mcc.add("add",200,"value2"); //如果key已经存在，返回已经存在的值
            System.out.println("add value in cache - " + mcc.get("add"));

            // 关闭连接
            mcc.shutdown();

        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }

    }
}
