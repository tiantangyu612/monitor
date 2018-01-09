package monitor.test;


import monitor.core.annotation.Monitor;

import java.util.Random;

/**
 * Created by lizhitao on 2018/1/5.
 * JavaMethodTest
 */
public class JavaMethodTest {
    @Monitor
    public void say() {
        try {
            Thread.sleep(new Random().nextInt(1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
