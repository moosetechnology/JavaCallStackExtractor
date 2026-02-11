package org.jdiextractor.tracemodel.entities.traceValues;

import org.jdiextractor.service.serializer.TraceSerializer;
import org.jdiextractor.tracemodel.entities.TraceValue;

public class TraceValueMaxDepth extends TraceValue {

	@Override
	public void acceptSerializer(TraceSerializer serializer) {
		serializer.serialize(this);
	}
	
}
