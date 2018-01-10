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
            Thread.sleep(Math.abs(new Random().nextInt(19)) + 1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
