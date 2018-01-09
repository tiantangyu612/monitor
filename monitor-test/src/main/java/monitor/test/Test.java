package monitor.test;

/**
 * Created by lizhitao on 2018/1/5.
 * 监控测试
 */
public class Test {
    public static void main(String[] args) {
        JavaMethodTest javaMethodTest = new JavaMethodTest();
        while (true) {
            javaMethodTest.say();
        }
    }
}
