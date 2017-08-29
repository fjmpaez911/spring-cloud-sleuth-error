package com.fjmpaez.configuration;

import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
@RefreshScope
@EnableConfigurationProperties(MsvcCUrlConfig.class)
public class ServiceCConfig {

    @Autowired
    @Qualifier("msvcCUrlConfig")
    private MsvcCUrlConfig urlConfig;

    @Bean("msvc-c_url")
    public String getUrl() throws MalformedURLException {

        String url = null;

        if (urlConfig.getPort() != 0) {
            url = new URL(urlConfig.getProtocol(), urlConfig.getHost(), urlConfig.getPort(),
                    urlConfig.getPath()).toString();
        } else {
            url = new URL(urlConfig.getProtocol(), urlConfig.getHost(), urlConfig.getPath())
                    .toString();
        }

        return url;
    }

}
