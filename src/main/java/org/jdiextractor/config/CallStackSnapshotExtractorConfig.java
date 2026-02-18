package org.jdiextractor.config;

import com.fasterxml.jackson.databind.JsonNode;

public class CallStackSnapshotExtractorConfig extends AbstractExtractorConfig {

	public static CallStackSnapshotExtractorConfig fromJson(JsonNode rootNode) {
		CallStackSnapshotExtractorConfig config = new CallStackSnapshotExtractorConfig();
		config.fillFromJson(rootNode);
		return config;
	}

	@Override
	protected void fillFromJson(JsonNode rootNode) {
		super.fillFromJson(rootNode);

	}

}
