package monitor.agent.exception;

import java.io.FileNotFoundException;

/**
 * Created by lizhitao on 2018/1/11.
 * monitor jar 不存在抛出异常
 */
public class MonitorJarNotFoundException extends FileNotFoundException {
    public MonitorJarNotFoundException() {
        super();
    }

    public MonitorJarNotFoundException(String s) {
        super(s);
    }
}
