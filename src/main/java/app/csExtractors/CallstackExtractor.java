package app.csExtractors;

import java.util.List;
import java.util.ListIterator;

import com.fasterxml.jackson.databind.JsonNode;
import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.StackFrame;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.VirtualMachine;

import app.breakpoint.BreakPointInstaller;
import app.breakpoint.BreakpointWrapper;
import app.vmManager.VmManager;

public class CallstackExtractor {

	StackExtractor extractor;
	JsonNode config;

	public CallstackExtractor(JsonNode loggingConfig, int maxDepth) {
		this.extractor = new StackExtractor(loggingConfig, maxDepth);
	}
	
	public CallstackExtractor(JsonNode config) {
		this.extractor = new StackExtractor(config.get("logging"), config.get("maxDepth").intValue());
		this.config = config;
	}
	
	public static void extract(VirtualMachine vm, JsonNode config) throws InterruptedException {
		VmManager vmManager = new VmManager(vm);
		// Adding the breakpoint
		BreakpointWrapper bkWrap = BreakPointInstaller.addBreakpoint(vm, config.get("breakpoint"));

		// resuming the process of the thread
		vmManager.resumeThread(config.get("entryMethod").textValue());
		
		vmManager.waitForBreakpoint(bkWrap);
		
		CallstackExtractor csExtractor = new CallstackExtractor(config.get("logging"), config.get("maxDepth").intValue());
		csExtractor.extractCallStack(vmManager.getThreadNamed(config.get("entryMethod").textValue()));

		// properly disconnecting
		vmManager.disposeVM();
	}

	/**
	 * Extract the call stack on the searched VM starting form the given thread and stopping at the method described
	 * 
	 * @param thread the thread to study
	 */
	public void extractCallStack(ThreadReference thread) {

		try {
			extractor.getLogger().framesStart();
			// iterating from the end of the list to start the logging from the first method called
			List<StackFrame> frames = thread.frames();
			ListIterator<StackFrame> it = frames.listIterator(frames.size());

			// doing the first iteration separately because the logging potentially need
			// to know if we are at the first element or not to join with a special character
			extractor.getLogger().frameLineStart(1);

			// extracting the stack frame
			extractor.extract(it.previous());
			extractor.getLogger().frameLineEnd();

			for (int i = 2; i <= frames.size(); i++) {
				extractor.getLogger().joinElementListing();

				extractor.getLogger().frameLineStart(i);
				// extracting the stack frame
				extractor.extract(it.previous());
				extractor.getLogger().frameLineEnd();
			}
			extractor.getLogger().framesEnd();
		} catch (IncompatibleThreadStateException e) {
			// Should not happen because we are supposed to be at a breakpoint
			throw new IllegalStateException("Thread should be at a breakpoint but isn't");
		}

		// close the writer in the logger
		extractor.closeLogger();

	}

}
