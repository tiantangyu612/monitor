package monitor.view.web.controller.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import monitor.core.util.CollectionUtils;
import monitor.view.dao.influxdb.InfluxDBBuilder;
import monitor.view.dao.influxdb.InfluxDBService;
import org.influxdb.InfluxDB;
import org.influxdb.dto.QueryResult;
import org.springframework.ui.Model;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * Created by lizhitao on 2018/1/14.
 * BaseController
 */
public class BaseController {
    private InfluxDB influxDB = null;
    private InfluxDBService influxDBService = null;

    @PostConstruct
    public void init() {
        try {
            influxDB = new InfluxDBBuilder("http://127.0.0.1:8086", "monitor", "monitor").build();
            influxDBService = new InfluxDBService("monitor", influxDB);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * 构建折线图数据
     *
     * @param collectorItemName 采集项名称
     * @param measurement       表名
     * @param model
     */
    protected void buildLineChartData(String collectorItemName, String measurement, Model model) {
        // 查询数据
        QueryResult queryResult = influxDBService.query("select * from \"" + measurement + "\"");
        System.out.println(JSON.toJSONString(queryResult.getResults(), SerializerFeature.PrettyFormat));
        if (!queryResult.hasError()) {
            // 数据分类
            List<String> categories = Lists.newArrayList();
            // x 轴分类
            List<String> xAxisData = Lists.newArrayList();
            // 分类的数据
            List<Map<String, Object>> finalData = Lists.newArrayList();

            List<QueryResult.Result> queryResults = queryResult.getResults();
            if (CollectionUtils.isNotEmpty(queryResults)) {
                QueryResult.Result result = queryResults.get(0);
                List<QueryResult.Series> seriesList = result.getSeries();
                if (CollectionUtils.isNotEmpty(seriesList)) {
                    QueryResult.Series series = seriesList.get(0);

                    List<String> columns = series.getColumns();
                    if (CollectionUtils.isNotEmpty(columns)) {
                        for (String column : columns) {
                            if (!"time".equals(column)) {
                                categories.add(column);
                            }
                        }
                    }

                    // 分类个数
                    int categorySize = categories.size();
                    List<List<Object>> datas = Lists.newArrayList();
                    for (int i = 0; i < categorySize; i++) {
                        datas.add(Lists.newArrayList());
                    }

                    List<List<Object>> rows = series.getValues();
                    if (CollectionUtils.isNotEmpty(rows)) {
                        for (List<Object> row : rows) {
                            for (int i = 0; i < categorySize; i++) {
                                if (i == 0) {
                                    xAxisData.add(String.valueOf(row.get(0)));
                                }

                                datas.get(i).add(row.get(i + 1));
                            }
                        }
                    }

                    for (int i = 0; i < categorySize; i++) {
                        Map<String, Object> finalDataItem = Maps.newHashMap();
                        finalDataItem.put("name", categories.get(i));
                        finalDataItem.put("type", "line");
                        finalDataItem.put("data", datas.get(i));

                        finalData.add(finalDataItem);
                    }
                }
            }

            model.addAttribute("hasData", true);
            model.addAttribute("title", collectorItemName);
            model.addAttribute("categories", JSON.toJSONString(categories));
            model.addAttribute("xAxisData", JSON.toJSONString(xAxisData));
            model.addAttribute("series", JSON.toJSONString(finalData));
        } else {
            model.addAttribute("hasData", false);
        }
    }
}
