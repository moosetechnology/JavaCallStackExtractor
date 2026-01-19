package app;

import java.io.File;
import java.io.IOException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jdi.VirtualMachine;

import app.csExtractors.CallstackExtractor;
import app.vmAttach.JDIAttach;

/**
 * Attach to a java virtual machine to extract the call stack to a text file
 */
public class JDICallstackExtractor {

	public static void main(String[] args) throws Exception {
		// reading the config file
		String configFileName;
		JsonNode config = null;
		if (args.length == 0) {
			configFileName = "config.json";
		} else {
			configFileName = args[0];
		}
		try {
			ObjectMapper mapper = new ObjectMapper();
			config = mapper.readTree(new File(configFileName));

		} catch (IOException e) {
			e.printStackTrace();
		}

		// creating the VmManager using JDIAttach to find the vmx
		JDIAttach jdiAttach = new JDIAttach();
		VirtualMachine vm = jdiAttach.attachToJDI(config.get("vm"));

		CallstackExtractor.extract(vm, config);
	}

}
