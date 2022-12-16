package com.tail.smart.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;

@Configuration
public class SmartLogStartConfig {
    @Autowired
    protected SmartLogConfig smartLogConfig;

    @Bean
    @ConditionalOnProperty(prefix = "smartlog", name = "enable", havingValue = "true")
    public FilterRegistrationBean trace() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new SmartHttpFilter(smartLogConfig));
        filterRegistrationBean.setUrlPatterns(new ArrayList<>(Arrays.asList("/*")));
        return filterRegistrationBean;
    }

    @Bean
    public ApplicationListener refresh() {
        return new StartedRefreshEventListener();
    }
}
