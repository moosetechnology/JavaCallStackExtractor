package org.jdiextractor.config;

import org.jdiextractor.config.components.BreakpointConfig;

import com.fasterxml.jackson.databind.JsonNode;

public class TraceExtractorStepConfig extends AbstractExtractorConfig {

	protected BreakpointConfig endpoint;

	public static TraceExtractorStepConfig fromJson(JsonNode rootNode) {
		TraceExtractorStepConfig config = new TraceExtractorStepConfig();
		config.fillFromJson(rootNode);
		return config;
	}

	@Override
	protected void fillFromJson(JsonNode rootNode) {
		super.fillFromJson(rootNode);

		this.endpoint = BreakpointConfig.fromJson(rootNode.get("endpoint"));
	}

	public BreakpointConfig getEndpoint() {
		return endpoint;
	}
}
