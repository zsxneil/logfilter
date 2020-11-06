package com.zsxneil.logfilter.simple;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.classic.spi.LoggerContextListener;
import ch.qos.logback.classic.spi.LoggerContextVO;
import ch.qos.logback.classic.spi.TurboFilterList;
import ch.qos.logback.classic.turbo.TurboFilter;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.status.Status;
import com.zsxneil.logfilter.simple.converter.SensitiveConverter;
import com.zsxneil.logfilter.simple.xml.LogbackDemo;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@SpringBootApplication
public class SimpleApplication {

    public static void main(String[] args) throws JoranException {

        SpringApplication.run(SimpleApplication.class, args);

        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

        System.out.println("***************************");
        List<String> frameworkPackages = context.getFrameworkPackages();
        for (String frameworkPackage : frameworkPackages) {
            System.out.println(frameworkPackage);
        }

        TurboFilterList turboFilterList = context.getTurboFilterList();
        Iterator<TurboFilter> iterator = turboFilterList.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next().getName());
        }

        List<LoggerContextListener> copyOfListenerList = context.getCopyOfListenerList();

        LoggerContextVO loggerContextRemoteView = context.getLoggerContextRemoteView();
        System.out.println(loggerContextRemoteView.getName());
        Map<String, String> propertyMap = loggerContextRemoteView.getPropertyMap();
        Set<String> keySet = propertyMap.keySet();
        for (String key : keySet) {
            System.out.println(propertyMap.get(key));
        }

        Map<String, Level> loggerMap = new ConcurrentHashMap<>();
        List<Logger> loggerList = context.getLoggerList();
        for (Logger logger : loggerList) {
            System.out.println(logger.getName() + ":" + logger.getLevel());
            if (Objects.nonNull(logger.getLevel())) {
                loggerMap.put(logger.getName(), logger.getLevel());
            }
        }

        System.out.println("***************************");
        JoranConfigurator configurator = new JoranConfigurator();
        configurator.setContext(context);
        context.stop();
        context.reset();
        configurator.doConfigure(LogbackDemo.insertXml());

        for (LoggerContextListener loggerContextListener : copyOfListenerList) {
            context.addListener(loggerContextListener);
        }

        List<Status> statuses = context.getStatusManager().getCopyOfStatusList();
        StringBuilder errors = new StringBuilder();
        for (Status status : statuses) {
            if (status.getLevel() == Status.ERROR) {
                errors.append((errors.length() > 0) ? String.format("%n") : "");
                errors.append(status.toString());
            }
        }
        if (errors.length() > 0) {
            throw new IllegalStateException(
                    String.format("Logback configuration error detected: %n%s", errors));
        }

        System.out.println("****************");

//        loggerList = context.getLoggerList();
        for (Logger logger : loggerList) {
//            logger.setLevel(Level.DEBUG);
            if (loggerMap.containsKey(logger.getName())) {
                logger.setLevel(loggerMap.get(logger.getName()));
            }
            System.out.println(logger.getName() + ":" + logger.getLevel());
        }


    }

}
