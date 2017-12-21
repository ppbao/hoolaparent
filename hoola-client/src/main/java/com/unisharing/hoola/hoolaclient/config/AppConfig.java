package com.unisharing.hoola.hoolaclient.config;

import com.unisharing.hoola.hoolaclient.web.interceptor.AuthInterceptor;
import com.unisharing.hoola.hoolaclient.web.interceptor.PerformanceMonitorInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 *
 * to add an interceptor is quite easy
 */
@Configuration
public class AppConfig  extends WebMvcConfigurerAdapter {
    @Autowired
    private AuthInterceptor authInterceptor;
    @Autowired
    private PerformanceMonitorInterceptor performanceMonitorInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
       registry.addInterceptor(authInterceptor);
       registry.addInterceptor(performanceMonitorInterceptor);
    }
    @Bean
    public AuthInterceptor authInterceptor() {
        return new AuthInterceptor();
    }
    @Bean
    public PerformanceMonitorInterceptor performanceInterceptor() {
        return new PerformanceMonitorInterceptor();
    }
}

