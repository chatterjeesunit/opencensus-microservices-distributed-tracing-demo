package com.spring.demo.bff.filter;


import io.opencensus.common.Scope;
import io.opencensus.trace.Span;
import io.opencensus.trace.SpanBuilder;
import io.opencensus.trace.SpanContext;
import io.opencensus.trace.Tracer;
import io.opencensus.trace.propagation.SpanContextParseException;
import io.opencensus.trace.propagation.TextFormat;
import io.opencensus.trace.samplers.Samplers;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
@Component
public class TracingFilter extends OncePerRequestFilter {

    @Autowired
    private Tracer tracer;

    @Autowired
    private TextFormat textFormat;

    @Autowired
    private TextFormat.Getter<HttpServletRequest> requestGetter;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        SpanContext spanContext;
        SpanBuilder spanBuilder;

        String spanName = request.getMethod() + " " + request.getRequestURI();

        try {
            spanContext = textFormat.extract(request, requestGetter);
            spanBuilder = tracer.spanBuilderWithRemoteParent(spanName, spanContext);
            log.info("Parent span found in request headers for request {} , TraceId = {}", spanName, spanContext.getTraceId());
        } catch (SpanContextParseException e) {
            spanBuilder = tracer.spanBuilder(spanName);
            log.warn("Parent Span is not present for request {}", spanName);
        }

        Span span = spanBuilder.setRecordEvents(true).setSampler(Samplers.alwaysSample()).startSpan();

        try (Scope s = tracer.withSpan(span)) {
            filterChain.doFilter(request, response);
        }finally {
            span.end();
        }
    }
}