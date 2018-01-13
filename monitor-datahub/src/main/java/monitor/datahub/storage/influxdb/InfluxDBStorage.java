package monitor.datahub.storage.influxdb;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import monitor.datahub.storage.MonitorStorage;
import monitor.datahub.vo.ReportData;
import org.influxdb.InfluxDB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by lizhitao on 2018/1/13.
 * 使用 InfluxDB 存储采集数据
 */
public class InfluxDBStorage implements MonitorStorage {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private InfluxDB influxDB = null;
    private InfluxDBService influxDBService = null;

    public InfluxDBStorage() {
        init();
    }

    private void init() {
        try {
            influxDB = new InfluxDBBuilder("http://127.0.0.1:8086", "monitor", "monitor").build();
            influxDBService = new InfluxDBService("monitor", influxDB);
            influxDBService.createDatabase();
        } catch (Throwable e) {
            logger.error("init InfluxDBStorage error, cause: ", e);
        }
    }

    @Override
    public boolean storageData(String reportDataJson) {
        if (null == influxDBService) {
            logger.error("influxDBService is null");
            return false;
        }

        ReportData reportData = JSON.parseObject(reportDataJson, ReportData.class);
        String application = reportData.getApplication();
        String cluster = reportData.getCluster();

        Map<String, Map<String, List<Map<String, Object>>>> reportDataMap = reportData.getReportData();
        for (Map.Entry<String, Map<String, List<Map<String, Object>>>> reportDataEntry : reportDataMap.entrySet()) {
            String collectorName = reportDataEntry.getKey();
            Map<String, List<Map<String, Object>>> collectorItemsMap = reportDataEntry.getValue();

            Map<String, String> tagsToAdd = Maps.newHashMap();
            Map<String, Object> fields = Maps.newHashMap();

            for (Map.Entry<String, List<Map<String, Object>>> collectDataItemsEntry : collectorItemsMap.entrySet()) {
                String collectorItemName = collectDataItemsEntry.getKey();
                List<Map<String, Object>> collectorItemsData = collectDataItemsEntry.getValue();

//                tagsToAdd.put(collectorName, collectorItemName);

                for (Map<String, Object> collectorItem : collectorItemsData) {
                    for (Map.Entry<String, Object> collectorItemEntry : collectorItem.entrySet()) {
                        String itemKey = collectorItemEntry.getKey();
                        Object itemValue = collectorItemEntry.getValue();

                        fields.put(itemKey, itemValue);
                    }
                }

                if (!fields.isEmpty()) {
                    influxDBService.insert(getMeasurement(application, cluster, collectorName, collectorItemName), tagsToAdd, fields);
                }

                tagsToAdd.clear();
                fields.clear();
            }
        }
        return true;
    }

    /**
     * 获取存储的 Measurement 名称
     *
     * @param application       应用名
     * @param cluster           集群名
     * @param collectorName     采集器名称
     * @param collectorItemName 采集项名称
     * @return
     */
    private String getMeasurement(String application, String cluster, String collectorName, String collectorItemName) {
        System.out.println(application + "_" + cluster + "_" + collectorName + "_" + collectorItemName);
        return application + "_" + cluster + "_" + collectorName + "_" + collectorItemName;
    }

    @Override
    public void close() throws IOException {
        if (null != influxDB) {
            influxDB.close();
        }
    }
}
