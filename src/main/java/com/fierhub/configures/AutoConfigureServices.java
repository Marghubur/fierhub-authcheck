package com.fierhub.configures;

import com.fierhub.service.AuthService;
import com.fierhub.service.FierhubService;
import com.fierhub.service.JwtService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@AutoConfiguration
@ConditionalOnClass({AuthService.class})
@ComponentScan(basePackages = {"com.fierhub.*"})
public class AutoConfigureServices {
    public AutoConfigureServices() {
    }

    @Bean
    @ConditionalOnMissingBean
    public AuthService authService() {
        return new AuthService();
    }

    @Bean
    @ConditionalOnMissingBean
    public FierhubService fierhubService() {
        return new FierhubService();
    }

    @Bean
    @ConditionalOnMissingBean
    public JwtService jwtService() {
        return new JwtService();
    }

    @Bean
    public RouteValidator routeValidator() {
        return new RouteValidator();
    }
}
