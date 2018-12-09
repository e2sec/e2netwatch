package de.e2security.processors.e2esper.utilities;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.nifi.processor.ProcessContext;

import com.espertech.esper.client.EPAdministrator;
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

	public static void parseMultipleEventSchema(String eventSchemaDeclaration, EPAdministrator admin) {
		String[] eventSchemas = eventSchemaDeclaration.split("\\|");
		for (String schema : eventSchemas) {
			admin.createEPL(schema);
		}		
	}

}
