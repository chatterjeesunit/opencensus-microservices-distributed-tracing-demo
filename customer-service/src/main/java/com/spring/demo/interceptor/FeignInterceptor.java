package com.spring.demo.interceptor;

import feign.Request;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import io.opencensus.trace.Tracer;
import io.opencensus.trace.propagation.TextFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by "Sunit Chatterjee" created on 04/10/20
 */
@Component
public class FeignInterceptor implements RequestInterceptor {

    @Autowired
    Tracer tracer;

    @Autowired
    TextFormat textFormat;

    @Autowired
    TextFormat.Setter<Request> feignRequestSetter;

    @Override
    public void apply(RequestTemplate template) {
        textFormat.inject(tracer.getCurrentSpan().getContext(), template.request(), feignRequestSetter);
    }
}
