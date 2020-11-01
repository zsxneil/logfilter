package com.zsxneil.logfilter.simple.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private Logger log = LoggerFactory.getLogger(TestController.class);

    @GetMapping("/test")
    public String test() {
        log.info("hello 17328752127");
        return "hello";
    }

}
