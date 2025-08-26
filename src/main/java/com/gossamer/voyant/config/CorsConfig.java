package com.gossamer.voyant.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;

/**
 * Configuration class to register CustomFilter as a Spring Bean.
 */
@Configuration
public class CorsConfig {

    /**
     * Registers CustomFilter with the Spring context.
     *
     * @return FilterRegistrationBean for CustomFilter
     */
    @Bean
    public FilterRegistrationBean<CorsFilter> customFilterRegistrationBean() {
        FilterRegistrationBean<CorsFilter> registrationBean = new FilterRegistrationBean<>();

        // Set the filter instance
        registrationBean.setFilter(new CorsFilter());

        // Define URL patterns to which the filter should be applied
        registrationBean.addUrlPatterns("/*");

        return registrationBean;
    }
}