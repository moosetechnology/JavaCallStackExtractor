package org.jdiextractor.tracemodel.entities.traceValues;

import org.jdiextractor.service.serializer.TraceSerializer;
import org.jdiextractor.tracemodel.entities.TraceValue;

public class TracePrimitiveValue extends TraceValue {

	private String type;

	private Object value;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	@Override
	public void acceptSerializer(TraceSerializer serializer) {
		serializer.serialize(this);
	}

}
