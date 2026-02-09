package org.jdiextractor.core.trace;

import org.jdiextractor.config.JDIExtractorConfig;
import org.jdiextractor.core.AbstractExtractor;
import org.jdiextractor.service.serializer.TraceLogger;
import org.jdiextractor.service.serializer.TracePopulator;
import org.jdiextractor.tracemodel.entities.Trace;
import org.jdiextractor.tracemodel.entities.TraceMethod;

import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.MethodEntryEvent;
import com.sun.jdi.event.StepEvent;
import com.sun.jdi.request.MethodEntryRequest;

public class TraceExtractor extends AbstractExtractor {

	/**
	 * The trace model built during execution
	 */
	private TracePopulator tracePopulator;

	public TraceExtractor(VirtualMachine vm, JDIExtractorConfig config) {
		super(vm, config);
		this.tracePopulator = new TracePopulator(config.getMaxDepth());
	}

	@Override
	protected void executeExtraction() {
		// Wait until the vm is at the start of the main before reacting to each
		// MethodEntry
		try {
			this.processEventsUntil(config.getEntrypoint());

			MethodEntryRequest entryRequest = vm.eventRequestManager().createMethodEntryRequest();
			entryRequest.addThreadFilter(getThread());
			entryRequest.enable();

			this.processEventsUntil(config.getEndpoint());

			TraceLogger serializer = new TraceLogger(config.getLogging(), config.getMaxDepth());
			serializer.serialize(this.tracePopulator.getTrace());

		} catch (IncompatibleThreadStateException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void reactToStepEvent(StepEvent event, ThreadReference targetThread) {
		// Nothing, should not happen in this scenario
	}

	@Override
	protected void reactToMethodEntryEvent(MethodEntryEvent event, ThreadReference targetThread) {
		this.tracePopulator.newMethodFrom(event.method());
	}

}
