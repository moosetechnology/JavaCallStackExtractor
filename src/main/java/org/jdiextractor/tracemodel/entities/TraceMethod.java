package org.jdiextractor.tracemodel.entities;

import java.util.ArrayList;
import java.util.List;

import com.sun.jdi.Method;

/**
 * Represents a specific method execution call in the trace.
 */
public class TraceMethod extends TraceElement {

	private TraceInvocation invocation = null;

	private TraceReceiver receiver = null;

	private List<TraceArgument> arguments = new ArrayList<>();
	
	private String signature;

	public TraceMethod() {

	}

	public TraceInvocation getInvocation() {
		return invocation;
	}

	public void setInvocation(TraceInvocation invocation) {
		this.invocation = invocation;
	}

	public TraceReceiver getReceiver() {
		return receiver;
	}

	public void setReceiver(TraceReceiver receiver) {
		this.receiver = receiver;
	}

	public List<TraceArgument> getArguments() {
		return arguments;
	}

	public void setArguments(List<TraceArgument> arguments) {
		this.arguments = arguments;
	}

	public void addArgument(TraceArgument argument) {
		this.arguments.add(argument);
	}
	
	public void setSignature(String signature) {
		this.signature = signature;
	}
	
	public String getSignature() {
		return this.signature;
	}

	public static TraceMethod from(Method method) {
		TraceMethod traceMethod = new TraceMethod();
		traceMethod.setSignature(method.signature());
		return traceMethod;
	}

	

}