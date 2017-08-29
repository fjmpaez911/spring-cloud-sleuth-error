package com.fjmpaez.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateLoadBalancedConfig {

    private static final Logger logger = LoggerFactory
            .getLogger(RestTemplateLoadBalancedConfig.class);

    @Value("${rest-template.loadBalanced:true}")
    private boolean loadBalanced;

    @Autowired
    @Qualifier("loadBalancedRestTemplate")
    private RestTemplate loadBalancedTemplate;

    @Autowired
    @Qualifier("apiGatewayRestTemplate")
    private RestTemplate apiGatewayTemplate;

    public boolean isLoadBalanced() {
        return loadBalanced;
    }

    public void setLoadBalanced(boolean loadBalanced) {
        this.loadBalanced = loadBalanced;
    }

    @Bean("activeRestTemplate")
    @DependsOn({ "loadBalancedRestTemplate", "apiGatewayRestTemplate" })
    public RestTemplate getRestTemplate() {
        if (isLoadBalanced()) {
            logger.warn("Load Balanced Rest Template");
            return loadBalancedTemplate;
        } else {
            logger.warn("Api Gateway Rest Template");
            return apiGatewayTemplate;
        }
    }

}
