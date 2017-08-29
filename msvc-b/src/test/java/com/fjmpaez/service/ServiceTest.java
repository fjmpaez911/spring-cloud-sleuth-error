package com.fjmpaez.service;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fjmpaez.Application;
import com.fjmpaez.util.AbstractSpanAccessorTest;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class ServiceTest {

    @Autowired
    private MyService service;

    @Autowired
    @Qualifier("msvc-c_url")
    private String msvcCUrl;

    @Autowired
    @Qualifier("activeRestTemplate")
    private RestTemplate template;

    private MockRestServiceServer mockServer;

    @Before
    public void init() {

        mockServer = MockRestServiceServer.createServer(template);
    }

    @Test
    public void test() throws JsonProcessingException {

        String url = msvcCUrl;

        mockServer.expect(requestTo(url)).andExpect(method(HttpMethod.GET))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andRespond(withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON));

        service.callingMsvcC();

        mockServer.verify();

    }

}
