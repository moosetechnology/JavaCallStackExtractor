package org.jdiextractor.tracemodel.entities.traceValues;

import org.jdiextractor.tracemodel.entities.TraceValue;

public abstract class TraceObjectReference extends TraceValue {

	protected long uniqueID;

	private String type;

	public long getUniqueID() {
		return uniqueID;
	}

	public void setUniqueID(long uniqueID) {
		this.uniqueID = uniqueID;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return this.type;
	}
}
