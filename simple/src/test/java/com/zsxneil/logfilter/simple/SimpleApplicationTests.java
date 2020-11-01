package com.zsxneil.logfilter.simple;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SimpleApplication.class)
public class SimpleApplicationTests {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    public void test() throws InterruptedException {
        logger.info("17328752127");

        Thread.sleep(Long.MAX_VALUE);
    }

}
