package org.jdiextractor.tracemodel.entities;

/**
 * Container that holds a specific {@link TraceValue}.
 */
public interface ValueContainer {

    TraceValue getValue();

    void setValue(TraceValue value);

}
