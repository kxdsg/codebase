package com.argus.thread;

import javax.swing.plaf.TableHeaderUI;
import java.util.concurrent.*;

/**
 * @author xingding
 * @date 2017/8/17.
 */
public class PoolTest {


    public static void main(String[] args) {
        new PoolTest().test();
        /*
        ExecutorService es = Executors.newFixedThreadPool(5);
        for(int i=0;i<10;i++){
            es.submit(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + " 正在执行");
                    try {
                        TimeUnit.MILLISECONDS.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        es.shutdown();
        */
    }

    public void test(){
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5,10,200,TimeUnit.MILLISECONDS,new ArrayBlockingQueue<Runnable>(5));
        for(int i=0;i<15;i++){ //最多只能提交15个任务，其中最大线程数10可以处理10个任务，多余的5个任务加入queue中，queue的容量是5。
            MyTaskRunnable runnable = new MyTaskRunnable(i);
            executor.execute(runnable);
            System.out.println("线程池中线程数目: " + executor.getPoolSize() + ",队列中等待执行" + executor.getQueue().size()
            + ",已执行完的任务数目:" + executor.getCompletedTaskCount());
        }
        executor.shutdown();
    }

    class MyTaskRunnable implements Runnable{
        private int taskNo;

        public MyTaskRunnable(int taskNo) {
            this.taskNo = taskNo;
        }

        @Override
        public void run() {
            System.out.println("正在执行task " + taskNo);
            try {
                TimeUnit.SECONDS.sleep(4);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("task " + taskNo + " 执行完毕");
        }
    }

}
