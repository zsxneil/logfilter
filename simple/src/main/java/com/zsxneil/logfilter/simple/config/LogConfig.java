package com.zsxneil.logfilter.simple.config;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggerContextListener;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.Layout;
import ch.qos.logback.core.encoder.Encoder;
import ch.qos.logback.core.joran.action.ConversionRuleAction;
import ch.qos.logback.core.pattern.ConverterUtil;
import ch.qos.logback.core.pattern.PatternLayoutBase;
import com.zsxneil.logfilter.simple.converter.SensitiveConverter;

import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.xml.sax.Attributes;

import java.util.Map;
import java.util.Set;

//@Configuration
public class LogConfig {

    Logger logger = (Logger)LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

    public void config() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        ConverterUtil.setContextForConverters(loggerContext, new SensitiveConverter());
        ConverterUtil.startConverters(new SensitiveConverter());
        Map<String, String> copyOfPropertyMap = loggerContext.getCopyOfPropertyMap();
        Set<String> keySet = copyOfPropertyMap.keySet();
        for (String key : keySet) {
            System.out.println();
        }
//        JoranConfigurator.
        JoranConfigurator configurator = new JoranConfigurator();
        configurator.setContext(loggerContext);
        configurator.getInterpretationContext();
//        configurator.registerSafeConfiguration();

        ConversionRuleAction conversionRuleAction = new ConversionRuleAction();
//        conversionRuleAction.begin(null, "", null);
        conversionRuleAction.setContext(loggerContext);
//        loggerContext.
//        Attributes

        Map<String, String> defaultConverterMap = PatternLayout.defaultConverterMap;

        PatternLayout patternLayout = new PatternLayout();

        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(loggerContext);
        encoder.setPattern("%-5level [%thread]: %message%n");
        encoder.start();



        FileAppender<ILoggingEvent> fileAppender = (FileAppender<ILoggingEvent>) logger.getAppender("FILE");
        fileAppender.setEncoder(encoder);

    }

}
