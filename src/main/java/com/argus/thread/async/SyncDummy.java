package com.argus.thread.async;

import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by xingding on 18/12/13.
 * 同步阻塞调用
 */
public class SyncDummy {
    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();
        RpcService rpcService = new RpcService();
        HttpService httpService = new HttpService();
        Map<String,Object> result1 = rpcService.getRpcResult();
        Map<String,Object> result2 = httpService.getHttpResult();
        System.out.println((System.currentTimeMillis()-start)/1000);
    }

    static class RpcService{
        Map<String,Object> getRpcResult() throws Exception{
            //模拟调用远程方法，需要耗时10s
            TimeUnit.SECONDS.sleep(10);
            return Maps.newHashMap();
        }
    }

    static class HttpService{
        Map<String,Object> getHttpResult() throws Exception{
            //模拟调用接口，需要耗时20s
            TimeUnit.SECONDS.sleep(20);
            return Maps.newHashMap();
        }
    }

}
