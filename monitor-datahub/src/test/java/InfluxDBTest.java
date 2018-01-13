import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import monitor.datahub.storage.influxdb.InfluxDBBuilder;
import monitor.datahub.storage.influxdb.InfluxDBService;
import org.influxdb.InfluxDB;
import org.influxdb.dto.QueryResult;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Created by lizhitao on 2018/1/13.
 * InfluxDBTest
 */
public class InfluxDBTest {
    private InfluxDB influxDB = null;
    private InfluxDBService influxDBService = null;

    @Before
    public void setUp() throws Exception {
        try {
            influxDB = new InfluxDBBuilder("http://127.0.0.1:8086", "monitor", "monitor").build();
            influxDBService = new InfluxDBService("monitor", influxDB);
            influxDBService.createDatabase();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test() {
        List<String> databases = influxDB.describeDatabases();
        System.out.println(databases);

        // 查询数据
        QueryResult queryResult = influxDBService.query("select * from \"monitor-view_online_JVM_classLoading\"");

//        monitor-view_online_JVM_memory
//        monitor-view_online_JVM_classLoading
//        monitor-view_online_JVM_compile
//        monitor-view_online_JVM_cpu
//        monitor-view_online_JVM_thread
//        monitor-view_online_JVM_GC
//        monitor-view_online_JVM_memoryPool
//        monitor-view_online_JVMInfo_info
//        monitor-view_online_Tomcat_tomcat
//        monitor-view_online_Tomcat_tomcatInfo


        List<QueryResult.Result> results = queryResult.getResults();
        for (QueryResult.Result result : results) {
            System.out.println(JSON.toJSONString(result, SerializerFeature.PrettyFormat));
        }
    }

    @Test
    public void testDeleteDB() {
        influxDB.deleteDatabase("monitor");
    }
}
