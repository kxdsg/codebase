package com.argus.thread.master;

import java.util.Map;
import java.util.Set;

/**
 * Created by xingding on 18/3/25.
 */
public class MasterWorkerMain {

    public static void main(String[] args) {
        Master m = new Master(new PlusWorker(),5);//固定使用5个worker，并指定worker

        for(int i=0;i<100;i++){
            m.submit(i); //提交100个子任务
        }
        m.execute();
        int re = 0;
        Map<String,Object> resultMap = m.getResultMap();

        while (resultMap.size()>0 || !m.isComplete()){ //不需要所有的worker都执行完，即可开始计算最终结果
            Set<String> keys = resultMap.keySet();
            String key = null;
            for(String k:keys){
                key = k;
                break;
            }
            Integer i = null;
            if(key != null){
                i = (Integer)resultMap.get(key);
            }
            if(i!=null){
                re+=i;
            }
            if(key!=null){
                resultMap.remove(key);
            }
        }
        System.out.println(re);
    }
}
