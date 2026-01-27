package org.jdiextractor.launcher;

import java.io.File;
import java.io.IOException;

import org.jdiextractor.config.JDIExtractorConfig;
import org.jdiextractor.core.AbstractExtractor;
import org.jdiextractor.service.connector.JDIAttach;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jdi.VirtualMachine;

public abstract class AbstractLauncher {

	private static long startTime;

	protected static void mainCore(String[] args, Class<? extends AbstractExtractor> clazz) throws Exception {
		startRecordTime();

		JDIExtractorConfig config = configFrom(args);

		// creating the VmManager using JDIAttach to find the vm
		VirtualMachine vm = (new JDIAttach()).attachToJDI(config.getVm());

		AbstractExtractor.launch(clazz, vm, config);

		// Properly disconnecting
		vm.dispose();

		endRecordTime();
	}

	private static void startRecordTime() {
		startTime = System.nanoTime();
	}

	private static void endRecordTime() {
		System.out.println("Execution took : " + (System.nanoTime() - startTime) + " nanoseconds");
	}

	private static JDIExtractorConfig configFrom(String[] args) {
		// reading the config file
		String configFileName;
		JsonNode configNode = null;
		if (args.length == 0) {
			configFileName = "config.json";
		} else {
			configFileName = args[0];
		}
		try {
			ObjectMapper mapper = new ObjectMapper();
			configNode = mapper.readTree(new File(configFileName));

		} catch (IOException e) {
			e.printStackTrace();
		}

		return JDIExtractorConfig.fromJson(configNode);
	}
}
