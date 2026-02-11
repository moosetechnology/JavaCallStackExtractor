package org.jdiextractor.tracemodel.entities.traceValues;

import java.util.ArrayList;
import java.util.List;

import org.jdiextractor.service.serializer.TraceSerializer;

public class TraceClassReference extends TraceObjectReference {

	private List<TraceField> fields = new ArrayList<>();

	public void setFields(List<TraceField> fields) {
		this.fields = fields;
	}

	public List<TraceField> getFields() {
		return this.fields;
	}

	public void addField(TraceField traceField) {
		this.fields.add(traceField);

	}
	
	@Override
	public void acceptSerializer(TraceSerializer serializer) {
		serializer.serialize(this);
	}

}
