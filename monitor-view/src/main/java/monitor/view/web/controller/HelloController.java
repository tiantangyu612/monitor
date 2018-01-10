package monitor.view.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by bjlizhitao on 2018/1/10.
 * HelloController
 */
@Controller
@RequestMapping(value = "/api/v1")
public class HelloController {

    @RequestMapping(value = "/hello")
    public ResponseEntity<String> hello() {
        return new ResponseEntity<String>("hello", HttpStatus.OK);
    }
}
