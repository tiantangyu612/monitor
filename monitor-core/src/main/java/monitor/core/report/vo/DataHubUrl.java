package monitor.core.report.vo;

/**
 * Created by bjlizhitao on 2018/1/17.
 * 数据中心 url
 */
public class DataHubUrl {
    private String host;
    private int port;

    public DataHubUrl() {
    }

    public DataHubUrl(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "DataHubUrl{" +
                "host='" + host + '\'' +
                ", port=" + port +
                '}';
    }
}
