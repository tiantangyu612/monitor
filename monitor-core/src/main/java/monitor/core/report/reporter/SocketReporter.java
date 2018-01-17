package monitor.core.report.reporter;

import com.alibaba.fastjson.JSON;
import monitor.core.log.MonitorLogFactory;
import monitor.core.report.vo.ReportData;
import monitor.core.report.vo.DataHubUrl;
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
public class SocketReporter extends RoundRobinReporter {
    private static final Logger LOGGER = MonitorLogFactory.getLogger(SocketReporter.class);

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
            DataHubUrl dataHubHost = getDataHubUrl();

            Socket sendDataSocket = new Socket();
            sendDataSocket.setSoTimeout(5000);
            sendDataSocket.connect(new InetSocketAddress(dataHubHost.getHost(), dataHubHost.getPort()));

            return sendDataSocket;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "createSendDataSocket error, cause: ", e);
        }

        return null;
    }
}
