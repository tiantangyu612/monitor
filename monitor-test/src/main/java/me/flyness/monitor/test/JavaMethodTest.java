package me.flyness.monitor.test;


import monitor.core.annotation.Monitor;

/**
 * Created by lizhitao on 2018/1/5.
 * JavaMethodTest
 */
public class JavaMethodTest {
    public static final int NUM = 10;

    @Monitor
    public void say() {
        System.out.println("hello");
    }
}
