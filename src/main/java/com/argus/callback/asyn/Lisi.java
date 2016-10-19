package com.argus.callback.asyn;

/**
 * Created by xingding on 2016/10/16.
 */
public class Lisi {

    public void execute(Callback callback, String question){
        System.out.println("李四说我现在忙，一会儿告诉你答案");
        for(int i=0;i<10000;i++){}
        String result = "2";
        //李四再回调张三中的solve方法
        callback.solve(result);
    }
}
