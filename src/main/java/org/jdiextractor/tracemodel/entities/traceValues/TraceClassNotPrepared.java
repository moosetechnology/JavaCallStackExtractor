package org.jdiextractor.tracemodel.entities.traceValues;

import org.jdiextractor.service.serializer.TraceSerializer;

public class TraceClassNotPrepared extends TraceObjectReference {

	@Override
	public void acceptSerializer(TraceSerializer serializer) {
		serializer.serialize(this);
	}
}
