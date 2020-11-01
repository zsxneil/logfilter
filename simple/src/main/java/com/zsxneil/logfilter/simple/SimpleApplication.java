package com.zsxneil.logfilter.simple;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import com.zsxneil.logfilter.simple.converter.SensitiveConverter;
import com.zsxneil.logfilter.simple.xml.LogbackDemo;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class SimpleApplication {

    public static void main(String[] args) throws JoranException {

        SpringApplication.run(SimpleApplication.class, args);

        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

        JoranConfigurator configurator = new JoranConfigurator();
        configurator.setContext(context);
        context.reset();
//        configurator.doConfigure("E:\\web\\idea-workspace\\log-filter\\simple\\src\\main\\resources\\logback-spring1.xml");
        configurator.doConfigure(LogbackDemo.insertXml());
    }

}
