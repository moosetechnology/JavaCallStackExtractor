package org.jdiextractor.core.callstack.strategy;

import java.io.IOException;

import org.jdiextractor.config.JDIExtractorConfig;
import org.jdiextractor.core.callstack.AbstractCallStackExtractor;
import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.MethodEntryEvent;
import com.sun.jdi.event.StepEvent;

/**
 * Extracts the call stack only once, when the breakpoint is reached. *
 * <p>
 * <b>Note:</b> Fast, but object states are captured at the very end. If an
 * object was modified during execution, older frames will show the
 * <i>current</i> modified value, not the value at the time of the call.
 */
public class CallStackSnapshotExtractor extends AbstractCallStackExtractor {

	public CallStackSnapshotExtractor(VirtualMachine vm, JDIExtractorConfig config) {
		super(vm, config, false);
	}

	@Override
	protected void executeExtraction() {
		try {
			this.waitForBreakpoint();
			this.processFrames(this.getThread());

		} catch (IncompatibleThreadStateException e) {
			// Should not happen because we are supposed to be at a breakpoint
			throw new IllegalStateException("Thread should be at a breakpoint but isn't");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void waitForBreakpoint() throws IncompatibleThreadStateException {
		this.processEventsUntil(config.getEndpoint());
	}

	@Override
	protected void reactToStepEvent(StepEvent event, ThreadReference targetThread) {
		// Nothing, should not happen in this scenario
	}
	
	@Override
	protected void reactToMethodEntryEvent(MethodEntryEvent event, ThreadReference targetThread) {
		// Nothing, should not happen in this scenario
	}

}
