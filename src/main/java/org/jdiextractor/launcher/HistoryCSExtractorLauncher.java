package org.jdiextractor.launcher;

import org.jdiextractor.core.callstack.strategy.CallStackHistoryExtractor;

/**
 * Attach to a java virtual machine to extract the call stack to a text file
 * 
 * Extracts the call stack frame by frame during execution.
 * * <p><b>Note:</b> Extremely slow, but historically accurate.
 * Since data is serialized at every step, it preserves the exact state 
 * of objects as they were at the moment of execution.
 */
public class HistoryCSExtractorLauncher extends AbstractLauncher {

	public static void main(String[] args) throws Exception {
		mainCore(args, CallStackHistoryExtractor.class);
	}

}
