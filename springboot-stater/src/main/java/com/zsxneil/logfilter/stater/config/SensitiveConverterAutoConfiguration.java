package com.zsxneil.logfilter.stater.config;

import com.zsxneil.logfilter.stater.properties.SensitiveConverterProperties;
import com.zsxneil.logfilter.stater.service.LogbackConfigService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(SensitiveConverterProperties.class)
public class SensitiveConverterAutoConfiguration {

    private final SensitiveConverterProperties sensitiveConverterProperties;

    public SensitiveConverterAutoConfiguration(SensitiveConverterProperties sensitiveConverterProperties) {
        this.sensitiveConverterProperties = sensitiveConverterProperties;
    }

    @Bean
    @ConditionalOnMissingBean
    public LogbackConfigService logbackConfigService() {
        return new LogbackConfigService(this.sensitiveConverterProperties);
    }
}
