package org.jdiextractor.config;

import com.fasterxml.jackson.databind.JsonNode;

public class CallStackHistoryExtractorConfig extends AbstractExtractorConfig {

	public static CallStackHistoryExtractorConfig fromJson(JsonNode rootNode) {
		CallStackHistoryExtractorConfig config = new CallStackHistoryExtractorConfig();
		config.fillFromJson(rootNode);
		return config;
	}

	@Override
	protected void fillFromJson(JsonNode rootNode) {
		super.fillFromJson(rootNode);
	}

}
