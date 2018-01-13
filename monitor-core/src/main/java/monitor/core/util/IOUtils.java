package monitor.core.util;

import monitor.core.log.MonitorLogFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by lizhitao on 2018/1/13.
 * IO 工具包
 */
public class IOUtils {
    private static final Logger LOGGER = MonitorLogFactory.getLogger(IOUtils.class);

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
                LOGGER.log(Level.SEVERE, "closeQuiet error, cause: ", e);
            }
        }
    }
}
