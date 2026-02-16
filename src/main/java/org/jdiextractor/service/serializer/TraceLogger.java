package org.jdiextractor.service.serializer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import org.jdiextractor.config.LoggingConfig;
import org.jdiextractor.tracemodel.entities.Trace;

public class TraceLogger {

	
	private LoggingConfig loggingConfig;
	
	/**
	 * Whether the values are independents between all element of the trace or not
	 */
	private boolean valueIdependents;

	/**
	 * Constructor of TraceLogger
	 * 
	 * @param loggingConfig information to instantiate the logger
	 * @param valueIdependents, Whether the values are independents between all element of the trace or not
	 */
	public TraceLogger(LoggingConfig loggingConfig, boolean valueIdependents) {
		this.loggingConfig = loggingConfig;
		this.valueIdependents = valueIdependents;
	}

	public void serialize(Trace trace) {
		BufferedWriter output;
		try {
			output = new BufferedWriter(
					new FileWriter(this.loggingConfig.getOutputName() + "." + this.loggingConfig.getExtension()));

			TraceSerializerJson serializer = new TraceSerializerJson(output,valueIdependents);
			serializer.serialize(trace);

			output.flush();
			output.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
