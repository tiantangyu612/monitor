package monitor.core.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by lizhitao on 2018/1/9.
 * ExceptionUtil，异常工具类
 */
public class ExceptionUtil {
    private static final int MAX_ERROR_STACK_LENGTH = 4096;

    private ExceptionUtil() {
    }

    /**
     * 获取异常 stackTrace 信息
     *
     * @param t
     * @return
     */
    public static String getStackTrace(Throwable t) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        t.printStackTrace(printWriter);
        String stackTrace = stringWriter.toString();
        if (stackTrace.length() > MAX_ERROR_STACK_LENGTH) {
            stackTrace = stackTrace.substring(0, MAX_ERROR_STACK_LENGTH);
        }

        return stackTrace;
    }
}