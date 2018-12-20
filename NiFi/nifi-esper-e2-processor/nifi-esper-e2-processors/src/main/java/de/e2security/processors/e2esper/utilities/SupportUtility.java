package de.e2security.processors.e2esper.utilities;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.nifi.logging.ComponentLog;

import static de.e2security.processors.e2esper.utilities.EsperProcessorLogger.success;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class SupportUtility {
	
	public static Map<String,Object> transformEventToMap(String eventAsJson) {
		Map<String,Object> eventAsMap = new HashMap<>();
		ObjectMapper mapper = new ObjectMapper();
		try {
			eventAsMap = mapper.readValue(eventAsJson, new TypeReference<Map<String,Object>>(){});
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return eventAsMap;
	}

	public static void parseMultipleEventSchema(String eventSchemaDeclaration, EPAdministrator admin, ComponentLog logger) {
		String staticRef = "SupportUtility.parseMultipleEventSchema";
		String[] eventSchemas = eventSchemaDeclaration.split("\\|");
		for (String schema : eventSchemas) {
			admin.createEPL(schema); //do not try to catch failure; this is a runtime critical one; 
			logger.debug(success("IMPLEMENTED SCHEMA", schema)); //debug if error wasn't thrown
		}		
	}
	
}
