package monitor.datahub.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by lizhitao on 2018/1/13.
 * IO 工具包
 */
public class IOUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(IOUtils.class);

    private IOUtils() {
    }

    /**
     * 关闭 inputStream 流
     *
     * @param closeable
     */
    public static void closeQuiet(Closeable closeable) {
        if (null != closeable) {
            try {
                closeable.close();
            } catch (IOException e) {
                LOGGER.error("closeQuiet error, cause: ", e);
            }
        }
    }
}
