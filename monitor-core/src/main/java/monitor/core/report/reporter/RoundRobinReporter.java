package monitor.core.report.reporter;

import monitor.core.report.vo.DataHubUrl;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by bjlizhitao on 2018/1/17.
 * RoundRobinReporter
 */
public abstract class RoundRobinReporter extends AbstractReporter {
    private static final AtomicInteger SEQUENCE = new AtomicInteger(0);

    /**
     * 获取 DataHubUrl
     *
     * @return
     */
    protected DataHubUrl getDataHubUrl() {
        List<DataHubUrl> dataHubUrls = getDataHubUrls();
        if (dataHubUrls.size() > 1) {
            int dataHubUrlCount = dataHubUrls.size();
            int currentIndex = SEQUENCE.getAndIncrement() % dataHubUrlCount;
            return dataHubUrls.get(currentIndex);
        } else {
            return dataHubUrls.get(0);
        }
    }
}
