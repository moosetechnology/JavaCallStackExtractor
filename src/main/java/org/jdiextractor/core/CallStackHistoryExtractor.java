package org.jdiextractor.core;

import org.jdiextractor.config.JDIExtractorConfig;
import org.jdiextractor.service.serializer.TraceLogger;
import org.jdiextractor.service.serializer.TracePopulator;

import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.MethodEntryEvent;
import com.sun.jdi.event.StepEvent;
import com.sun.jdi.request.StepRequest;

/**
 * Extracts the call stack frame by frame during execution.
 * <p>
 * <b>Note:</b> Extremely slow, but historically accurate. Since data is
 * serialized at every step, it preserves the exact state of objects as they
 * were at the moment of execution.
 */
public class CallStackHistoryExtractor extends AbstractExtractor {

	/**
	 * The trace model built during execution
	 */
	private TracePopulator tracePopulator;

	public CallStackHistoryExtractor(VirtualMachine vm, JDIExtractorConfig config) {
		super(vm, config, true);

		this.tracePopulator = new TracePopulator(false, config.getMaxDepth());
	}

	@Override
	protected void executeExtraction() {
		try {
			this.collectFrames();

			// Serialize the trace
			TraceLogger serializer = new TraceLogger(config.getLogging());
			serializer.serialize(this.tracePopulator.getTrace());

		} catch (IncompatibleThreadStateException e) {
			// Should not happen because we are supposed to be at a breakpoint
			throw new IllegalStateException("Thread should be at a breakpoint but isn't");
		}
	}

	/**
	 * Blocks execution until the configured breakpoint is hit. Handles
	 * ClassPrepareEvent if the class is not yet loaded.
	 * 
	 * @throws IncompatibleThreadStateException
	 */
	private void collectFrames() throws IncompatibleThreadStateException {
		// Wait until the vm is at the start of the main before reacting to every steps
		this.processEventsUntil(config.getEntrypoint());

		vm.eventRequestManager().createStepRequest(this.getThread(), StepRequest.STEP_MIN, StepRequest.STEP_INTO)
				.enable();

		this.processEventsUntil(config.getEndpoint());
	}

	@Override
	protected void reactToStepEvent(StepEvent event, ThreadReference targetThread) {
		try {
			if (this.tracePopulator.getTrace().size() + 1 == targetThread.frameCount()) {
				this.createMethodWith(targetThread.frame(0));
			} else if (this.tracePopulator.getTrace().size() - 1 == targetThread.frameCount()) {
				this.tracePopulator.getTrace().removeLastElement();
			}
		} catch (IncompatibleThreadStateException e) {
			throw new IllegalStateException("Exception occured during a step event : " + e);
		}
	}

	@Override
	protected void reactToMethodEntryEvent(MethodEntryEvent event, ThreadReference targetThread) {
		// Nothing, should not happen in this scenario
	}

}
