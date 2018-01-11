package monitor.view.web.controller;

import monitor.view.service.MonitorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * Created by bjlizhitao on 2018/1/10.
 * HelloController
 */
@Controller
@RequestMapping(value = "/api/v1")
public class HelloController {
    @Resource
    private MonitorService monitorService;

    @RequestMapping(value = "/hello")
    public ResponseEntity<String> hello() {

        monitorService.test();

        return new ResponseEntity<String>("hello", HttpStatus.OK);
    }
}
