package com.spring.demo.bff.config;

import feign.Request;
import io.opencensus.exporter.trace.ocagent.OcAgentTraceExporter;
import io.opencensus.exporter.trace.ocagent.OcAgentTraceExporterConfiguration;
import io.opencensus.trace.Tracer;
import io.opencensus.trace.Tracing;
import io.opencensus.trace.config.TraceConfig;
import io.opencensus.trace.propagation.TextFormat;
import io.opencensus.trace.samplers.Samplers;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by "Sunit Chatterjee" created on 04/10/20
 */

@Configuration
@Log4j2
public class OpenCensusConfig {

    @Value("${opencensus.agent:localhost:55678}")
    String openCensusAgent;

    @Bean
    public Tracer tracer(){
        TraceConfig traceConfig = Tracing.getTraceConfig();
        traceConfig.updateActiveTraceParams(
                traceConfig.getActiveTraceParams().toBuilder().setSampler(Samplers.alwaysSample()).build());

        log.info("OpenCensus Properties = {}", openCensusAgent);
        OcAgentTraceExporterConfiguration configuration =
                OcAgentTraceExporterConfiguration.builder()
                        .setServiceName("BFF-Service")
                        .setEnableConfig(true)
                        .setUseInsecure(true)
                        .setEndPoint(openCensusAgent).build();
        OcAgentTraceExporter.createAndRegister(configuration);
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
