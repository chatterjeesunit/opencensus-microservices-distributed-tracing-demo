package com.spring.demo.bff.config;

import feign.Request;
import io.opencensus.trace.Tracer;
import io.opencensus.trace.Tracing;
import io.opencensus.trace.propagation.TextFormat;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by "Sunit Chatterjee" created on 04/10/20
 */

@Configuration
public class OpenCensusConfig {

    @Bean
    public Tracer tracer(){
        return Tracing.getTracer();
    }


    @Bean
    public TextFormat textFormat() {
        return Tracing.getPropagationComponent().getB3Format();
    }

//    @Bean
//    public TracingAsyncClientHttpRequestInterceptor tracingAsyncClientHttpRequestInterceptor(TextFormat textFormat) {
//        return TracingAsyncClientHttpRequestInterceptor.create(textFormat, null);
//    }

    @Bean
    public TextFormat.Getter<HttpServletRequest> getter() {
        return new TextFormat.Getter<>() {
            @Override
            public String get(HttpServletRequest httpRequest, String headerName) {
                return httpRequest.getHeader(headerName);
            }
        };
    }

    @Bean
    public TextFormat.Setter<Request> setter() {
        return new TextFormat.Setter<>() {
            public void put(Request feignRequest, String headerName, String headerValue) {
                feignRequest.requestTemplate().header(headerName, headerValue);
            }
        };
    }
}
