package monitor.view.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import monitor.core.util.CollectionUtils;
import monitor.view.dao.influxdb.InfluxDBBuilder;
import monitor.view.dao.influxdb.InfluxDBService;
import monitor.view.service.MonitorService;
import org.influxdb.InfluxDB;
import org.influxdb.dto.QueryResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by bjlizhitao on 2018/1/10.
 * HelloController
 */
@Controller
@RequestMapping(value = "/monitor/jvm")
public class JVMController {
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
     * 内存信息
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/memory")
    public String memory(Model model) {
        buildLineChartData("memory", "monitor-view_online_JVM_memory", model);
        return "jvm/memory";

    }

    /**
     * 类加载信息
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/classLoading")
    public String classLoading(Model model) {
        buildLineChartData("classLoading", "monitor-view_online_JVM_classLoading", model);
        return "jvm/classLoading";
    }

    /**
     * 编译信息
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/compile")
    public String compile(Model model) {
        buildLineChartData("compile", "monitor-view_online_JVM_compile", model);
        return "jvm/classLoading";
    }

    /**
     * cpu 信息
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/cpu")
    public String cpu(Model model) {
        buildLineChartData("cpu", "monitor-view_online_JVM_cpu", model);
        return "jvm/classLoading";
    }

    /**
     * thread 信息
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/thread")
    public String thread(Model model) {
        buildLineChartData("thread", "monitor-view_online_JVM_thread", model);
        return "jvm/classLoading";
    }

    /**
     * gc 信息
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/gc")
    public String gc(Model model) {
        buildLineChartData("gc", "monitor-view_online_JVM_GC", model);
        return "jvm/classLoading";
    }

    /**
     * memory pool 信息
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/memoryPool")
    public String memoryPool(Model model) {
        buildLineChartData("memoryPool", "monitor-view_online_JVM_memoryPool", model);
        return "jvm/memory";
    }

    /**
     * tomcat 信息
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/tomcat")
    public String tomcat(Model model) {
        buildLineChartData("tomcat", "monitor-view_online_Tomcat_tomcat", model);
        return "jvm/classLoading";
    }

    /**
     * 构建折线图数据
     *
     * @param collectorItemName 采集项名称
     * @param measurement       表名
     * @param model
     */
    private void buildLineChartData(String collectorItemName, String measurement, Model model) {
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
