package org.jdiextractor.core;

import org.jdiextractor.config.JDIExtractorConfig;
import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.MethodEntryEvent;
import com.sun.jdi.event.StepEvent;
import com.sun.jdi.request.StepRequest;

/**
 * Extracts the trace of an execution, meaning all method calls and their values
 * at each instant
 * 
 * Note : To enable full trace mode of an execution, define endpoint repBefore
 * at a negative number
 */
public class TraceExtractorStep extends AbstractExtractor {

	private int frameCountBefore;

	public TraceExtractorStep(VirtualMachine vm, JDIExtractorConfig config) {
		super(vm, config, true);
	}

	@Override
	protected void executeExtraction() {

		try {
			// Wait until the vm is at the start of the main before reacting to each
			// MethodEntry
			this.processEventsUntil(config.getEntrypoint());

			// Fix for the first method being the main
			this.createMethodWith(this.getThread().frame(0));
			frameCountBefore = 1;

			vm.eventRequestManager().createStepRequest(this.getThread(), StepRequest.STEP_MIN, StepRequest.STEP_INTO)
					.enable();
			if (config.getEndpoint().getRepBefore() < 0) {
				this.processEventsUntilEnd();
			} else {
				this.processEventsUntil(config.getEndpoint());
			}

			this.serializeTrace();

		} catch (IncompatibleThreadStateException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void reactToStepEvent(StepEvent event) {
		try {
			ThreadReference targetThread = event.thread();
			int frameCountNow = targetThread.frameCount();

			if (frameCountBefore != frameCountNow) {
				if (frameCountBefore + 1 == frameCountNow) {
					this.createMethodWith(targetThread.frame(0));
				}
				frameCountBefore = frameCountNow;
			}

		} catch (IncompatibleThreadStateException e) {
			throw new IllegalStateException("Exception occured during a step event : " + e);
		}
	}

	@Override
	protected void reactToMethodEntryEvent(MethodEntryEvent event) {
		throw new IllegalStateException("Exception occured during a step event : ");
	}

}
