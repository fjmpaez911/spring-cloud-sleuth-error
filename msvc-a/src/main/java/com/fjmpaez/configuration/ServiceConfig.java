package com.fjmpaez.configuration;

import java.nio.charset.Charset;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@RefreshScope
public class ServiceConfig {

    private static final Logger logger = LoggerFactory.getLogger(ServiceConfig.class);

    @Value("${rest-template.timeout:30000}")
    private Integer timeout;

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    @Bean("loadBalancedRestTemplate")
    @LoadBalanced
    public RestTemplate loadBalancedRestTemplate(
            @Qualifier("defaultClientHttpRequestFactory") ClientHttpRequestFactory clientHttpRequestFactory)
            throws Exception {

        return restTemplate(clientHttpRequestFactory);

    }

    @Bean("apiGatewayRestTemplate")
    public RestTemplate apiGatewayRestTemplate(
            @Qualifier("defaultClientHttpRequestFactory") ClientHttpRequestFactory clientHttpRequestFactory)
            throws Exception {

        return restTemplate(clientHttpRequestFactory);

    }

    private RestTemplate restTemplate(ClientHttpRequestFactory clientHttpRequestFactory)
            throws Exception {

        logger.debug("instantiating rest template...");

        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);
        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
        messageConverters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
        MappingJackson2HttpMessageConverter jsonMessageConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        jsonMessageConverter.setObjectMapper(objectMapper);
        messageConverters.add(jsonMessageConverter);
        restTemplate.setMessageConverters(messageConverters);

        return restTemplate;
    }

    @Bean("defaultClientHttpRequestFactory")
    public ClientHttpRequestFactory getClientHttpRequestFactory() {

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();

        logger.debug("setting request factory timeout to {} milliseconds.", getTimeout());

        requestFactory.setReadTimeout(getTimeout());
        requestFactory.setConnectTimeout(getTimeout());

        return requestFactory;
    }

}
