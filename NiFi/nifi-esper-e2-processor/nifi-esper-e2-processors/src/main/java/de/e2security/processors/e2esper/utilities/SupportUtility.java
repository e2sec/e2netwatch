package de.e2security.processors.e2esper.utilities;

import static de.e2security.processors.e2esper.utilities.EsperProcessorLogger.success;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.apache.nifi.logging.ComponentLog;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.metric.EngineMetric;
import com.espertech.esper.client.metric.MetricEvent;
import com.espertech.esper.client.metric.StatementMetric;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

public final class SupportUtility {
	
	private static Gson gson = new Gson();
	
	@Deprecated
	public static Map<String,Object> transformEventToMap(String eventAsJson) throws IOException {
		Map<String,Object> eventAsMap = new HashMap<>();
		ObjectMapper mapper = new ObjectMapper();
		eventAsMap = mapper.readValue(eventAsJson, new TypeReference<Map<String,Object>>(){});
		final Map<String,Object> _eventAsMap = eventAsMap;
		esper_lb.ifPresent((value) -> _eventAsMap.put("esper_lb", (String) value));
		return _eventAsMap;
	}
	
	public static String transformEventMapToJson(Map eventAsMap) {
		return gson.toJson(eventAsMap);
	}

	public static void parseMultipleEventSchema(String eventSchemaDeclaration, EPAdministrator admin, ComponentLog logger) {
		String[] eventSchemas = eventSchemaDeclaration.split("\\|");
		for (String schema : eventSchemas) {
			admin.createEPL(schema); //do not try to catch failure; this is a runtime critical one; 
			logger.debug(success("IMPLEMENTED SCHEMA", schema)); //debug if error wasn't thrown
		}		
	}

	public static String transformMetricEventToJson(MetricEvent underlying) {
		String json = gson.toJson(underlying);
		if (underlying instanceof StatementMetric) {
			StatementMetric stmtMetric = (StatementMetric) underlying;
		} else if (underlying instanceof EngineMetric) {
			EngineMetric engineMetric = (EngineMetric) underlying;
		}
		return json;
	}

	public static String retrieveClassNameFromSchemaEPS(String eventSchema) {
		String eventName = StringUtils.substringBetween(eventSchema, "create schema ", " as");
		return eventName;
	}
	
}
