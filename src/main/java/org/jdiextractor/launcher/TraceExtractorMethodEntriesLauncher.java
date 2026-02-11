package org.jdiextractor.launcher;

import org.jdiextractor.core.TraceExtractorMethodEntries;

public class TraceExtractorMethodEntriesLauncher extends AbstractLauncher {
	
	public static void main(String[] args) throws Exception {
		mainCore(args, TraceExtractorMethodEntries.class);
	}

}
