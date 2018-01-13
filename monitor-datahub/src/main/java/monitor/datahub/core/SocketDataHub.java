package monitor.datahub.core;

import monitor.datahub.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by lizhitao on 2018/1/11.
 * 使用 Socket 收集采集器采集的数据
 */
public class SocketDataHub extends AbstractMonitorDataHub {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(10, 200, 30, TimeUnit.SECONDS,
            new ArrayBlockingQueue<Runnable>(2000), new ThreadPoolExecutor.DiscardPolicy());

    private static final int PORT = 16666;

    private ServerSocket serverSocket;

    /**
     * 启动 Socket 数据收集器收集数据
     */
    @Override
    public void start() {
        try {
            serverSocket = new ServerSocket(PORT);

            while (true) {
                Socket socket = serverSocket.accept();
                threadPool.execute(new SocketHandler(socket));
            }
        } catch (IOException e) {
            logger.info("new ServerSocket error, cause: ", e);
        }

        logger.info("---------------- SocketDataHub is startup, port is: " + PORT);
    }

    /**
     * 关闭 Socket 数据收集器
     */
    @Override
    public void stop() {
        IOUtils.closeQuiet(serverSocket);
        logger.info("---------------- SocketDataHub is stop, bye bye !!!");
    }

    /**
     * socket 连接处理
     */
    private class SocketHandler implements Runnable {
        private Socket socket;

        public SocketHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            InputStream inputStream = null;
            DataInputStream dataInputStream = null;
            try {
                inputStream = socket.getInputStream();

                dataInputStream = new DataInputStream(inputStream);
                String collectData = dataInputStream.readUTF();

                // 存储监控数据
                getMonitorStorageFactory().crateMonitorStorage().storageData(collectData);
            } catch (IOException e) {
                logger.error("socket get input stream error, cause: ", e);
            } finally {
                IOUtils.closeQuiet(dataInputStream);
                IOUtils.closeQuiet(inputStream);
                IOUtils.closeQuiet(socket);
            }
        }
    }
}
