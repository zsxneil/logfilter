package com.zsxneil.logfilter.sample.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private static final Logger log = LoggerFactory.getLogger(TestController.class);


    @GetMapping("/test")
    public String hello(String word) {
        log.info("word={}", word);
        return word;
    }

}
