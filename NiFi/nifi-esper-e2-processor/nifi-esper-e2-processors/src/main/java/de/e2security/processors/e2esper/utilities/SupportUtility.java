package de.e2security.processors.e2esper.utilities;

import static de.e2security.processors.e2esper.utilities.EsperProcessorLogger.success;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

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
			//TODO: process statement metric
		} else if (underlying instanceof EngineMetric) {
			EngineMetric engineMetric = (EngineMetric) underlying;
			//TODO: process engine metric
		}
		return json;
	}

	public static String retrieveClassNameFromSchemaEPS(String eventSchema) {
		String eventName = StringUtils.substringBetween(eventSchema, "create schema ", " as").replaceAll("'", "");
		return eventName;
	}

	@SuppressWarnings("deprecation") //TODO: check replacement method
	//Logical test (not test of method) is in nw104/EsperBehaviourTest.java
	public static String modifyUserDefinedSchema(String userSchema) {
		Pattern pattern = Pattern.compile("[a\\s]\\W*\\("); 
		Optional<String> replaced = Optional.ofNullable(
				StringUtils.replaceFirst(userSchema, 
						pattern.toString(), 
						String.format("( %s Map,", CommonSchema.EVENT.flowFileAttributes)));
		//TODO: check by propertyDescriptor validator instead
		return replaced.orElseThrow(() -> new RuntimeException(""));
	}

	public static String modifyUserDefinedEPStatement(String userStmt) {
		Optional<String> stmt = Optional.of(userStmt); //apply functional interface
		//TODO: make with patterns and matcher conditional (functional)
		//TODO: validator -> only UPPPERCASE for ESPER KEYWORDS
		//quick dummy solution -> patterns should be considered
		String result = "";
		if (!userStmt.contains("PATTERN")) {
			if (userStmt.contains("SELECT RSTREAM")) {
				result = StringUtils.replaceFirst(userStmt, "SELECT RSTREAM", 
						String.format("SELECT RSTREAM %s,", CommonSchema.EVENT.flowFileAttributes));
			} else if (userStmt.contains("SELECT IRSTREAM")) {
				result = StringUtils.replaceFirst(userStmt, "SELECT IRSTREAM", 
						String.format("SELECT IRSTREAM %s,", CommonSchema.EVENT.flowFileAttributes));
			} else if (userStmt.contains("SELECT ISTREAM")) {
				result = StringUtils.replaceFirst(userStmt, "SELECT ISTREAM", 
						String.format("SELECT ISTREAM %s,", CommonSchema.EVENT.flowFileAttributes));
			} else {
				result = StringUtils.replaceFirst(userStmt, "SELECT", 
						String.format("SELECT %s,", CommonSchema.EVENT.flowFileAttributes));
			} 
		} else { //on Pattern -> initialize flow file attributes ONLY by first event.
			if (userStmt.contains("SELECT RSTREAM")) {
				result = StringUtils.replaceFirst(userStmt, "SELECT RSTREAM", 
						String.format("SELECT RSTREAM a.%s,", CommonSchema.EVENT.flowFileAttributes));
			} else if (userStmt.contains("SELECT IRSTREAM")) {
				result = StringUtils.replaceFirst(userStmt, "SELECT IRSTREAM", 
						String.format("SELECT IRSTREAM a.%s,", CommonSchema.EVENT.flowFileAttributes));
			} else if (userStmt.contains("SELECT ISTREAM")) {
				result = StringUtils.replaceFirst(userStmt, "SELECT ISTREAM", 
						String.format("SELECT ISTREAM a.%s,", CommonSchema.EVENT.flowFileAttributes));
			} else {
				result = StringUtils.replaceFirst(userStmt, "SELECT", 
						String.format("SELECT a.%s,", CommonSchema.EVENT.flowFileAttributes));
			}
		}
		return result;
	}

}
