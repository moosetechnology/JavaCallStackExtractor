package org.jdiextractor.core;

import org.jdiextractor.config.JDIExtractorConfig;
import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.MethodEntryEvent;
import com.sun.jdi.event.StepEvent;
import com.sun.jdi.request.StepRequest;

public class TraceExtractorStep extends AbstractExtractor {

	private int frameCountBefore;

	public TraceExtractorStep(VirtualMachine vm, JDIExtractorConfig config) {
		super(vm, config, true);
	}

	@Override
	protected void executeExtraction() {
		// Wait until the vm is at the start of the main before reacting to each
		// MethodEntry
		try {
			this.processEventsUntil(config.getEntrypoint());
			
			// Fix for the first method being the main
			this.createMethodWith(this.getThread().frame(0));
			frameCountBefore = 1;

			vm.eventRequestManager().createStepRequest(this.getThread(), StepRequest.STEP_MIN, StepRequest.STEP_INTO)
					.enable();

			this.processEventsUntil(config.getEndpoint());

			this.serializeTrace();

		} catch (IncompatibleThreadStateException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void reactToStepEvent(StepEvent event, ThreadReference targetThread) {
		try {
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
	protected void reactToMethodEntryEvent(MethodEntryEvent event, ThreadReference targetThread) {
		throw new IllegalStateException("Exception occured during a step event : ");
	}

}
