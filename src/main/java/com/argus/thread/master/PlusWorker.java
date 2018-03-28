package com.argus.thread.master;

/**
 * Created by xingding on 18/3/25.
 *
 * 计算1*1*1+2*2*2+...+100*100*100
 */
public class PlusWorker extends Worker{

    public Object handle(Object input){
        Integer i = (Integer)input;
        return i*i*i;
    }

}
