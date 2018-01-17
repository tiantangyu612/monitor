package monitor.core.report;

import com.alibaba.fastjson.JSON;
import monitor.core.log.MonitorLogFactory;
import monitor.core.report.vo.ReportData;
import monitor.core.util.IOUtils;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by lizhitao on 2018/1/10.
 * 使用 socket 上报数据
 */
public class MonitorSocketReporter extends AbstractMonitorReporter {
    private static final Logger LOGGER = MonitorLogFactory.getLogger(MonitorSocketReporter.class);

    /**
     * 数据上报服务器 host
     */
    private static final String dataHubHost = "localhost";
    /**
     * 数据上报端口号
     */
    private static final int PORT = 16666;

    /**
     * 上报数据
     *
     * @param reportData
     * @return
     */
    @Override
    public boolean reportData(ReportData reportData) {
        try {
            String reportJsonData = JSON.toJSONString(reportData);

            return sendDataToDataHub(reportJsonData);
        } catch (Throwable e) {
            String errorMsg = "reportData failure!!!!";
            LOGGER.log(Level.SEVERE, errorMsg, e);
            return false;
        }
    }

    /**
     * 将数据上报到数据中心
     *
     * @param reportJsonData
     * @return
     */
    private boolean sendDataToDataHub(String reportJsonData) {
        Socket socket = createSendDataSocket();
        if (null != socket) {
            OutputStream outputStream = null;
            DataOutputStream dataOutputStream = null;

            try {
                outputStream = socket.getOutputStream();
                dataOutputStream = new DataOutputStream(outputStream);
                dataOutputStream.writeUTF(reportJsonData);

                return true;
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "sendDataToDataHub error, cause: ", e);
            } finally {
                IOUtils.closeQuiet(dataOutputStream);
                IOUtils.closeQuiet(outputStream);
                IOUtils.closeQuiet(socket);
            }
        }

        return false;
    }

    /**
     * 创建连接到数据中心的 socket 连接
     *
     * @return
     */
    private Socket createSendDataSocket() {
        try {
            Socket sendDataSocket = new Socket();
            sendDataSocket.setSoTimeout(5000);
            sendDataSocket.connect(new InetSocketAddress(dataHubHost, PORT));

            return sendDataSocket;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "createSendDataSocket error, cause: ", e);
        }

        return null;
    }
}
