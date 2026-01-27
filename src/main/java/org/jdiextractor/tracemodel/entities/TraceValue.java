package org.jdiextractor.tracemodel.entities;

import org.jdiextractor.tracemodel.TraceEntity;

import com.sun.jdi.Value;

/**
 * Abstract representation of a runtime value.
 */
public abstract class TraceValue extends TraceEntity {

	private Value value;

	public TraceValue() {

	}

	public void setValue(Value value) {
		this.value = value;
	}

	public Value getValue() {
		return value;
	}
}
