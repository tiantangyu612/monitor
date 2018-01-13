package monitor.datahub.core;

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
public class SocketDataHub implements MonitorDataHub {
    private final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(10, 200, 30, TimeUnit.SECONDS,
            new ArrayBlockingQueue<Runnable>(2000), new ThreadPoolExecutor.DiscardPolicy());

    private static final int PORT = 16666;

    private ServerSocket serverSocket;

    @Override
    public void start() {
        System.out.println("---------------- SocketDataHub is startup, port is: " + PORT);
        try {
            serverSocket = new ServerSocket(PORT);

            while (true) {
                Socket socket = serverSocket.accept();
                threadPool.execute(new SocketHandler(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        if (null != serverSocket) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("---------------- SocketDataHub is stop, bye bye !!!");
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

                System.out.println("---------------------------------------------------------------");
                System.out.println(collectData);
                System.out.println("---------------------------------------------------------------");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (null != dataInputStream) {
                    try {
                        dataInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (null != inputStream) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (null != socket) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }
}
