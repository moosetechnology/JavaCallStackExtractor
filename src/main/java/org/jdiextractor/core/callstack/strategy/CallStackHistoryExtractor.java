package org.jdiextractor.core.callstack.strategy;

import java.io.IOException;

import org.jdiextractor.config.BreakpointConfig;
import org.jdiextractor.config.JDIExtractorConfig;
import org.jdiextractor.core.callstack.AbstractCallStackExtractor;
import org.jdiextractor.service.breakpoint.BreakPointInstaller;
import org.jdiextractor.service.breakpoint.BreakpointWrapper;

import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.BreakpointEvent;
import com.sun.jdi.event.ClassPrepareEvent;
import com.sun.jdi.event.Event;
import com.sun.jdi.event.EventSet;
import com.sun.jdi.event.StepEvent;
import com.sun.jdi.event.VMDeathEvent;
import com.sun.jdi.event.VMDisconnectEvent;
import com.sun.jdi.request.ClassPrepareRequest;
import com.sun.jdi.request.StepRequest;

/**
 * Extracts the call stack frame by frame during execution.
 * * <p><b>Note:</b> Extremely slow, but historically accurate.
 * Since data is serialized at every step, it preserves the exact state 
 * of objects as they were at the moment of execution.
 */
public class CallStackHistoryExtractor extends AbstractCallStackExtractor {

	public CallStackHistoryExtractor(VirtualMachine vm, JDIExtractorConfig config) {
		super(vm, config, true);
	}

	@Override
	protected void executeExtraction() {
		try {
			this.collectFrames();
			stackFrameLogger.writeAll();

		} catch (IncompatibleThreadStateException e) {
			// Should not happen because we are supposed to be at a breakpoint
			throw new IllegalStateException("Thread should be at a breakpoint but isn't");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Blocks execution until the configured breakpoint is hit. Handles
	 * ClassPrepareEvent if the class is not yet loaded.
	 * 
	 * @throws IncompatibleThreadStateException
	 */
	private void collectFrames() throws IncompatibleThreadStateException {
		this.waitForEntry();

		vm.eventRequestManager().createStepRequest(this.getThread(), StepRequest.STEP_MIN, StepRequest.STEP_INTO)
				.enable();

		this.processEventsUntil(config.getEndpoint());
	}

	private void waitForEntry() throws IncompatibleThreadStateException {

		this.processEventsUntil(config.getEntrypoint());
	}

	private void processEventsUntil(BreakpointConfig bkConfig) throws IncompatibleThreadStateException {
		ThreadReference targetThread = this.getThread();

		BreakpointWrapper bkWrap = null;
		if (BreakPointInstaller.isClassLoaded(vm, bkConfig.getClassName())) {
			bkWrap = BreakPointInstaller.addBreakpoint(vm, bkConfig);
		} else {
			ClassPrepareRequest cpReq = vm.eventRequestManager().createClassPrepareRequest();
			cpReq.addClassFilter(bkConfig.getClassName());
			cpReq.enable();
		}

		vm.resume();
		EventSet eventSet;

		try {
			while ((eventSet = vm.eventQueue().remove()) != null) {
				for (Event event : eventSet) {

					if (event instanceof StepEvent) {
						this.reactToStepEvent((StepEvent) event, targetThread);
					}

					else if (event instanceof VMDeathEvent || event instanceof VMDisconnectEvent) {
						throw new IllegalStateException("VM disconnected or died before breakpoint");
					}

					else if (event instanceof BreakpointEvent) {
						if (bkWrap == null) {
							throw new IllegalStateException("Thread encountered a breakpoint while none has been set");
						}
						if (bkWrap.shouldStopAt(event)) {
							return;
						}
					}

					else if (event instanceof ClassPrepareEvent) {
						if (BreakPointInstaller.isClassLoaded(vm, bkConfig.getClassName())) {
							bkWrap = BreakPointInstaller.addBreakpoint(vm, bkConfig);
						} else {
							throw new IllegalStateException("ClassPrepareRequest caught but class is still not loaded");
						}
					}

				}

				eventSet.resume();
			}
		} catch (InterruptedException e) {
			throw new IllegalStateException("Interruption during extraction: " + e.getMessage());
		}
	}

	public void reactToStepEvent(StepEvent event, ThreadReference targetThread)
			throws IncompatibleThreadStateException {
		if (stackFrameLogger.size() + 1 == targetThread.frameCount()) {
			stackFrameLogger.push(targetThread.frame(0));
		} else if (stackFrameLogger.size() - 1 == targetThread.frameCount()) {
			stackFrameLogger.pop();
		}
	}
}
