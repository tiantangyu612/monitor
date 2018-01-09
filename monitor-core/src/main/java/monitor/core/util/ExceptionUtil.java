package monitor.core.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtil {
    public static int MAX_ERROR_STACK_LENGTH = 4096;

    public ExceptionUtil() {
    }

    public static String getThrowableStackTrace(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        String s = sw.toString();
        if (s.length() > MAX_ERROR_STACK_LENGTH) {
            s = s.substring(0, MAX_ERROR_STACK_LENGTH);
        }

        return s;
    }
}