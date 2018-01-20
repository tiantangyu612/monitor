package monitor.view.web.controller.monitor;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import monitor.core.annotation.Monitor;
import monitor.core.util.CollectionUtils;
import monitor.view.domain.vo.JavaMethodInfo;
import monitor.view.web.controller.base.BaseController;
import org.influxdb.dto.QueryResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

/**
 * Created by lizhitao on 2018/1/14.
 * JavaMethodContorller
 */
@Controller
@RequestMapping(value = "/monitor/method")
public class JavaMethodController extends BaseController {

    /**
     * method 执行信息
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/invoke/{application}/{cluster}")
    @Monitor
    public String invoke(@PathVariable("application") String application,
                         @PathVariable("cluster") String cluster, Model model) {
        String measurement = application + "_" + cluster + "_JavaMethod_method";
        String sql = "select sum(errorCount) as errorCount,sum(invokedCount) as invokedCount,max(maxConcurrency) as maxConcurrency," +
                "max(maxTime) as maxTime,sum(ms0_10) as ms0_10,sum(ms10_100) as ms10_100,sum(ms100_1000) as ms100_1000," +
                "sum(s1_10) as s1_10,sum(s10_n) as s10_n,sum(totalTime) as totalTime from \"" + measurement + "\" group by class,method";

        // 查询数据
        QueryResult queryResult = influxDBService.query(sql);

        if (!queryResult.hasError()) {
            List<QueryResult.Result> results = queryResult.getResults();
            if (CollectionUtils.isNotEmpty(results)) {
                QueryResult.Result result = results.get(0);

                List<QueryResult.Series> seriesList = result.getSeries();
                List<JavaMethodInfo> javaMethodInfos = Lists.newArrayList();
                if (CollectionUtils.isNotEmpty(seriesList)) {
                    for (QueryResult.Series series : seriesList) {
                        Map<String, String> tageMap = series.getTags();
                        String clazz = tageMap.get("class");
                        String method = tageMap.get("method");

                        List<String> columns = series.getColumns();
                        model.addAttribute("columns", columns);

                        List<List<Object>> dataList = series.getValues();

                        for (List<Object> dataItem : dataList) {
                            JavaMethodInfo javaMethodInfo = new JavaMethodInfo();
                            javaMethodInfo.setClazz(clazz);
                            javaMethodInfo.setMethod(method);

                            javaMethodInfo.setTime((String) dataItem.get(0));
                            javaMethodInfo.setErrorCount(JSON.parseObject(dataItem.get(1).toString(), Long.class));
                            javaMethodInfo.setInvokedCount(JSON.parseObject(dataItem.get(2).toString(), Long.class));
                            javaMethodInfo.setMaxConcurrency(JSON.parseObject(dataItem.get(3).toString(), Long.class));
                            javaMethodInfo.setMaxTime(JSON.parseObject(dataItem.get(4).toString(), Long.class));
                            javaMethodInfo.setMs0_10(JSON.parseObject(dataItem.get(5).toString(), Long.class));
                            javaMethodInfo.setMs100_1000(JSON.parseObject(dataItem.get(6).toString(), Long.class));
                            javaMethodInfo.setMs10_100(JSON.parseObject(dataItem.get(7).toString(), Long.class));
                            javaMethodInfo.setS10_n(JSON.parseObject(dataItem.get(8).toString(), Long.class));
                            javaMethodInfo.setS1_10(JSON.parseObject(dataItem.get(9).toString(), Long.class));
                            javaMethodInfo.setTotalTime(JSON.parseObject(dataItem.get(10).toString(), Long.class));

                            javaMethodInfos.add(javaMethodInfo);
                        }
                    }

                    model.addAttribute("javaMethodInfos", javaMethodInfos);
                }
            }
        }

        return "monitor/method/method";
    }
}
