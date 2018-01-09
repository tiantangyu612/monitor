package monitor.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by lizhitao on 2018/1/5.
 * 监控测试
 */
public class Test {
    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(1);
        while (true) {
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    new JavaMethodTest().say();
                }
            });
        }
    }
}
