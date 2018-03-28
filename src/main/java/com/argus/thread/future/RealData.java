package com.argus.thread.future;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Created by xingding on 18/3/25.
 */
public class RealData implements Callable {

    private String param;

    public RealData(String param) {
        this.param = param;
    }

    @Override
    public Object call() throws Exception {
        //这里是真实的业务逻辑，其执行可能较慢
        StringBuffer sb = new StringBuffer();
        for(int i=0;i<10;i++){
            sb.append(param);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
