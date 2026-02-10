package org.jdiextractor.launcher;

import org.jdiextractor.core.TraceExtractor;

public class TraceExtractorLauncher extends AbstractLauncher {
	
	public static void main(String[] args) throws Exception {
		mainCore(args, TraceExtractor.class);
	}

}
