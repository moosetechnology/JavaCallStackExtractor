package org.jdiextractor.core;

import org.jdiextractor.config.JDIExtractorConfig;
import org.jdiextractor.service.serializer.TraceLogger;
import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.MethodEntryEvent;
import com.sun.jdi.event.StepEvent;
import com.sun.jdi.request.MethodEntryRequest;

public class TraceExtractorMethodEntries extends AbstractExtractor {

	public TraceExtractorMethodEntries(VirtualMachine vm, JDIExtractorConfig config) {
		super(vm, config, true);
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

			TraceLogger serializer = new TraceLogger(config.getLogging());
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
