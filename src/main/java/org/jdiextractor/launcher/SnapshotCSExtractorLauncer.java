package org.jdiextractor.launcher;

import org.jdiextractor.core.callstack.strategy.CallStackSnapshotExtractor;

/**
 * Attach to a java virtual machine to extract the call stack to a text file
 * 
 * Extracts the call stack only once, when the breakpoint is reached.
 * * <p><b>Note:</b> Fast, but object states are captured at the very end. 
 * If an object was modified during execution, older frames will show the 
 * <i>current</i> modified value, not the value at the time of the call.
 */
public class SnapshotCSExtractorLauncer extends AbstractLauncher {
	
	public static void main(String[] args) throws Exception {
		mainCore(args, CallStackSnapshotExtractor.class);
	}

}
