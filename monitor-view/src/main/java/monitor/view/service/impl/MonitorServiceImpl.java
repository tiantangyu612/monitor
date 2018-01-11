package monitor.view.service.impl;

import monitor.core.annotation.Monitor;
import monitor.view.service.MonitorService;
import org.springframework.stereotype.Service;

/**
 * Created by bjlizhitao on 2018/1/11.
 * MonitorServiceImpl
 */
@Service
public class MonitorServiceImpl implements MonitorService {
    @Monitor
    @Override
    public void test() {
        System.out.println("test -------------------------------------------------");
    }
}
