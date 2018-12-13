package com.argus.thread.async;


import com.google.common.collect.Maps;

import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by xingding on 18/12/13.
 */
public class AsyncDummy {
    final static ExecutorService executor = Executors.newFixedThreadPool(2);

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        RpcService rpcService = new RpcService();
        HttpService httpService = new HttpService();
        Future<Map<String,Object>> future1 = null;
        Future<Map<String,Object>> future2 = null;
        try {
            future1 = executor.submit(new Callable<Map<String, Object>>() {
                @Override
                public Map<String, Object> call() throws Exception {
                    return rpcService.getRpcResult();
                }
            });
            future2 = executor.submit(new Callable<Map<String, Object>>() {
                @Override
                public Map<String, Object> call() throws Exception {
                    return httpService.getHttpResult();
                }
            });
            //获取结果的get方法是阻塞的
            Map<String,Object> result1 = future1.get();
            Map<String,Object> result2 = future2.get();
            System.out.println((System.currentTimeMillis()-start)/1000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(future1!=null){
                future1.cancel(true);
            }
            if(future2!=null){
                future2.cancel(true);
            }
        }
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
