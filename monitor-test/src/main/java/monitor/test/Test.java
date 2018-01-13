package monitor.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by lizhitao on 2018/1/5.
 * 监控测试
 */
public class Test {
    public static void main(String[] args) {
        final JavaMethodTest test = new JavaMethodTest();

        /*while (true){
            test.say();
        }*/
        Runnable task = new Runnable() {
            @Override
            public void run() {
                test.say();
                test.getName("test");
            }
        };
        ExecutorService threadPool = Executors.newFixedThreadPool(20);
        while (true) {
            threadPool.execute(task);
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
