package org.jdiextractor.tracemodel.entities;

import org.jdiextractor.tracemodel.TraceEntity;

public class AssignationRight extends TraceEntity implements ValueContainer {

	private TraceValue value;

	public AssignationRight() {

	}

	public TraceValue getValue() {
		return value;
	}

	public void setValue(TraceValue value) {
		this.value = value;
	}

}
