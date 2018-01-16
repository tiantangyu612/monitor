package monitor.view.web.controller.monitor;

import monitor.core.annotation.Monitor;
import monitor.core.util.CollectionUtils;
import monitor.view.web.controller.base.BaseController;
import org.influxdb.dto.QueryResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Created by lizhitao on 2018/1/14.
 * tomcat 监控信息
 */
@Controller
@RequestMapping(value = "/monitor/tomcat")
public class TomcatController extends BaseController {
    /**
     * tomcat 信息
     *
     * @param application
     * @param cluster
     * @param model
     * @return
     */
    @RequestMapping(value = "/tomcatThread/{application}/{cluster}")
    @Monitor
    public String tomcat(@PathVariable("application") String application,
                         @PathVariable("cluster") String cluster, Model model) {
        String measurement = application + "_" + cluster + "_Tomcat_tomcat";
        String sql = "select currentThreadCount,currentThreadsBusy,currentThreadsBusyMax,maxThreads from \"" + measurement + "\"";
        buildLineChartData("tomcat", sql, model);
        getTomcatInfo(model);

        return "tomcat/tomcat";
    }

    private void getTomcatInfo(Model model) {
        String measurement = "monitor-view_online_Tomcat_tomcatInfo";
        // 查询数据
        QueryResult queryResult = influxDBService.query("select tomcatVersion from \"" + measurement + "\" order by time desc limit 1");

        if (!queryResult.hasError()) {
            List<QueryResult.Result> results = queryResult.getResults();
            if (CollectionUtils.isNotEmpty(results)) {
                QueryResult.Result result = results.get(0);

                List<QueryResult.Series> seriesList = result.getSeries();
                if (CollectionUtils.isNotEmpty(seriesList)) {
                    QueryResult.Series series = seriesList.get(0);

                    List<List<Object>> dataList = series.getValues();

                    if (CollectionUtils.isNotEmpty(dataList)) {
                        List<Object> dataItem = dataList.get(0);

                        if (CollectionUtils.isNotEmpty(dataItem) && dataItem.size() > 1) {
                            String tomcatVersion = (String) dataItem.get(1);
                            model.addAttribute("tomcatVersion", tomcatVersion);
                        }
                    }
                }
            }
        }
    }
}
