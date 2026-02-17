package org.jdiextractor.config;

import org.jdiextractor.config.components.BreakpointConfig;
import org.jdiextractor.config.components.LoggingConfig;
import org.jdiextractor.config.components.VmConfig;

import com.fasterxml.jackson.databind.JsonNode;

public class AbstractExtractorConfig {

	protected final int DEFAULT_MAX_DEPTH = 20;

	protected BreakpointConfig entrypoint;
	protected VmConfig vm;
	protected LoggingConfig logging;
	protected int maxDepth;
	
	protected void fillFromJson(JsonNode rootNode) {
		this.vm = VmConfig.fromJson(rootNode.get("vm"));
		this.entrypoint = BreakpointConfig.fromJson(rootNode.get("entrypoint"));
		this.logging = LoggingConfig.fromJson(rootNode.get("logging"));
		this.maxDepth = rootNode.has("maxDepth") ? rootNode.get("maxDepth").asInt() : DEFAULT_MAX_DEPTH;
	}
	

	public BreakpointConfig getEntrypoint() {
		return entrypoint;
	}
	
	public VmConfig getVm() {
		return vm;
	}

	public LoggingConfig getLogging() {
		return logging;
	}


	public int getMaxDepth() {
		return maxDepth;
	}

}