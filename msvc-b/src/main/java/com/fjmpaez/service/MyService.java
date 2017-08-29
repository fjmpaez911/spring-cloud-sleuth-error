package com.fjmpaez.service;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class MyService {

    private static Logger LOGGER = LoggerFactory.getLogger(MyService.class);

    @Autowired
    @Qualifier("msvc-c_url")
    private String msvcCUrl;

    @Autowired
    @Qualifier("activeRestTemplate")
    private RestTemplate template;

    private HttpHeaders headers;

    @PostConstruct
    public void init() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    private HttpEntity<?> getHttpEntity() {
        return new HttpEntity<String>(headers);
    }

    public void callingMsvcC() {

        String url = msvcCUrl;
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);

        HttpEntity<?> httpRequest = getHttpEntity();

        LOGGER.info("Calling [{}]", uriBuilder.build().encode().toUriString());

        template.exchange(uriBuilder.build().encode().toUri(), HttpMethod.GET, httpRequest,
                String.class);
    }
}
