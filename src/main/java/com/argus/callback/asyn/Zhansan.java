package com.argus.callback.asyn;

/**
 *
 * Created by xingding on 2016/10/16.
 */
public class Zhansan implements Callback {

    private Lisi lisi;

    public Lisi getLisi() {
        return lisi;
    }

    public void setLisi(Lisi lisi) {
        this.lisi = lisi;
    }

    public void askQuestion(final String question){
        System.out.println("张三问李四的问题:" + question);
        //这里用一个线程就是异步，
        new Thread(new Runnable() {
            @Override
            public void run() {
                /**
                 * 张三调用李四中的方法，在这里注册回调接口
                 */
                lisi.execute(Zhansan.this, question);
            }
        }).start();

        //张三问完问题挂掉电话就去干其他的事情了，诳街去了
        play();
    }


    public void play(){
        System.out.println("张三说我要逛街去了");
    }


    /**
     * 张三实现接口方法
     * @param result
     */
    @Override
    public void solve(String result) {
        System.out.println("李四告诉张三答案是:" + result);
    }
}
