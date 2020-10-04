package com.spring.demo;

import io.opencensus.exporter.trace.ocagent.OcAgentTraceExporter;
import io.opencensus.exporter.trace.ocagent.OcAgentTraceExporterConfiguration;
import io.opencensus.trace.Tracing;
import io.opencensus.trace.config.TraceConfig;
import io.opencensus.trace.samplers.Samplers;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CustomerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomerServiceApplication.class, args);
		TraceConfig traceConfig = Tracing.getTraceConfig();
		traceConfig.updateActiveTraceParams(
				traceConfig.getActiveTraceParams().toBuilder().setSampler(Samplers.alwaysSample()).build());

		OcAgentTraceExporterConfiguration configuration =
				OcAgentTraceExporterConfiguration.builder()
						.setServiceName("Customer-Service")
						.setEnableConfig(true)
						.setUseInsecure(true)
						.setEndPoint("open-census-agent:55678").build();
		OcAgentTraceExporter.createAndRegister(configuration);
	}

}
