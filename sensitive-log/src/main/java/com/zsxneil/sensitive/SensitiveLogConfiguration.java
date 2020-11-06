package com.zsxneil.sensitive;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 敏感日志信息打码自动配置类
 *
 * @author
 * @version v1.0
 * @date 2020-11-06 20:22
 **/
@Configuration
@EnableConfigurationProperties({SensitiveLogStarterProperties.class})
public class SensitiveLogConfiguration {

    private final SensitiveLogStarterProperties sensitiveLogStarterProperties;

    public SensitiveLogConfiguration(SensitiveLogStarterProperties sensitiveLogStarterProperties) {
        this.sensitiveLogStarterProperties = sensitiveLogStarterProperties;
    }

    @Bean
    @ConditionalOnMissingBean()
    public SensitiveLogService sensitiveLogService() {
        return new SensitiveLogService(this.sensitiveLogStarterProperties);
    }

}
