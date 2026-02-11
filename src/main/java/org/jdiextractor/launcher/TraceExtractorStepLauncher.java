package org.jdiextractor.launcher;

import org.jdiextractor.core.TraceExtractorStep;

public class TraceExtractorStepLauncher extends AbstractLauncher {
	
	public static void main(String[] args) throws Exception {
		mainCore(args, TraceExtractorStep.class);
	}

}
