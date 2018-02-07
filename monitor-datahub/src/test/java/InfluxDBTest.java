import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import monitor.datahub.storage.influxdb.InfluxDBBuilder;
import monitor.datahub.storage.influxdb.InfluxDBService;
import org.influxdb.InfluxDB;
import org.influxdb.dto.QueryResult;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

public class InfluxDBTest {
    private InfluxDB influxDB = null;
    private InfluxDBService influxDBService = null;

    @Before
    @Ignore
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
    @Ignore
    public void test() {
        List<String> databases = influxDB.describeDatabases();
        System.out.println(databases);



        // 查询数据
        QueryResult queryResult = influxDBService.query("SHOW MEASUREMENTS");

        List<QueryResult.Result> results = queryResult.getResults();
        for (QueryResult.Result result : results) {
            System.out.println(JSON.toJSONString(result, SerializerFeature.PrettyFormat));
        }
    }

    @Test
    @Ignore
    public void testDeleteDB() {
        influxDB.deleteDatabase("monitor");
    }
}
