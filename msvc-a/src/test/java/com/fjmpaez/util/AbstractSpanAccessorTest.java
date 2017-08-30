package com.fjmpaez.util;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;

import java.util.Random;

import org.junit.Before;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;

public class AbstractSpanAccessorTest {

    @MockBean
    private Tracer tracer;

    @MockBean
    private Span span;

    private Random random = new Random();

    @Before
    public void mockSpan() {
        long id = createId();
        span = Span.builder().name("mock").traceId(id).spanId(id).build();
        doReturn(span).when(tracer).getCurrentSpan();
        doReturn(span).when(tracer).createSpan(anyString());
    }

    private long createId() {
        return random.nextLong();
    }

}
