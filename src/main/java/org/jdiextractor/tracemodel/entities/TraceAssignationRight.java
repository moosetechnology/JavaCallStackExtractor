package org.jdiextractor.tracemodel.entities;

import org.jdiextractor.tracemodel.TraceEntity;

public class TraceAssignationRight extends TraceEntity implements TraceValueContainer {

	private TraceValue value;

	public TraceAssignationRight() {

	}

	public TraceValue getValue() {
		return value;
	}

	public void setValue(TraceValue value) {
		this.value = value;
	}

}
