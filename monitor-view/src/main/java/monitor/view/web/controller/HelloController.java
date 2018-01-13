package monitor.view.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import monitor.view.dao.influxdb.InfluxDBBuilder;
import monitor.view.dao.influxdb.InfluxDBService;
import monitor.view.service.MonitorService;
import org.influxdb.InfluxDB;
import org.influxdb.dto.QueryResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by bjlizhitao on 2018/1/10.
 * HelloController
 */
@Controller
@RequestMapping(value = "/api/v1")
public class HelloController {
    @Resource
    private MonitorService monitorService;

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
     * 类加载信息
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/classLoading")
    public String classLoading(Model model) {
        // 查询数据
        QueryResult queryResult = influxDBService.query("select * from \"monitor-view_online_JVM_classLoading\"");
        List<String> classLoading = Lists.newArrayList();
        List<QueryResult.Result> results = queryResult.getResults();

        List<String> fenLei = Lists.newArrayList();
        List<String> times = Lists.newArrayList();

        List<Map<String, Object>> datas = Lists.newArrayList();

        for (QueryResult.Result result : results) {
            List<QueryResult.Series> seriesList = result.getSeries();
            for (QueryResult.Series series : seriesList) {
                List<String> columns = series.getColumns();

                for (String column : columns) {
                    if (column.equals("time")) {

                    } else {
                        fenLei.add(column);
                    }
                }

                List<List<Object>> values = series.getValues();
                List<Double> doubleDatas1 = Lists.newArrayList();
                List<Double> doubleDatas2 = Lists.newArrayList();
                List<Double> doubleDatas3 = Lists.newArrayList();

                for (List<Object> value : values) {
                    String time = (String) value.get(0);
                    times.add(time);

                    double loadedClassCount = (double) value.get(1);
                    double totalLoadedClassCount = (double) value.get(2);
                    double unloadedClassCount = (double) value.get(3);

                    doubleDatas1.add(loadedClassCount);
                    doubleDatas2.add(totalLoadedClassCount);
                    doubleDatas3.add(unloadedClassCount);
                }


                Map<String, Object> dataMap1 = Maps.newHashMap();
                Map<String, Object> dataMap2 = Maps.newHashMap();
                Map<String, Object> dataMap3 = Maps.newHashMap();

                dataMap1.put("name", "loadedClassCount");
                dataMap1.put("type", "line");
                dataMap1.put("stack", "数量");
                dataMap1.put("data", doubleDatas1);

                dataMap2.put("name", "totalLoadedClassCount");
                dataMap2.put("type", "line");
                dataMap2.put("stack", "数量");
                dataMap2.put("data", doubleDatas2);


                dataMap3.put("name", "unloadedClassCount");
                dataMap3.put("type", "line");
                dataMap3.put("stack", "数量");
                dataMap3.put("data", doubleDatas3);

                datas.add(dataMap1);
                datas.add(dataMap2);
                datas.add(dataMap3);
            }

            System.out.println(JSON.toJSONString(result, SerializerFeature.PrettyFormat));
        }
        model.addAttribute("classLoading", classLoading);
        model.addAttribute("fenLei", JSON.toJSONString(fenLei));
        model.addAttribute("datas", JSON.toJSONString(datas));
        model.addAttribute("times", JSON.toJSONString(times));

        return "jvm/classLoading";
    }
}
