package de.e2security.processors.e2esper.utilities;

import static de.e2security.processors.e2esper.utilities.EsperProcessorLogger.success;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
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

	//Logical test (not test of method) is in nw104/EsperBehaviourTest.java
	public static String modifyUserDefinedSchema(String userSchema) {
		Pattern pattern = Pattern.compile("[a\\s]\\W*\\("); 
		Optional<String> replaced = Optional.ofNullable(
				userSchema.replaceFirst(pattern.toString(), 
						String.format("( %s Map,", CommonSchema.EVENT.flowFileAttributes)));
		//TODO: check by propertyDescriptor validator instead
		return replaced.orElseThrow(() -> new RuntimeException(""));
	}

	public static String modifyUserDefinedEPStatement(String userStmt) {
		//no any modifications are needed, if ' select *' defined by user
		if (userStmt.contains("SELECT *")) return userStmt;
		//apply functional interface to stmt and process transform logic
		final Optional<String> funcStmt = Optional.of(userStmt); 
		//TODO: validator -> only UPPPERCASE for ESPER KEYWORDS
		final List<String> patterns = new ArrayList<>();
		patterns.add("select rstream".toUpperCase());
		patterns.add("select irstream".toUpperCase());
		patterns.add("select istream".toUpperCase());
		Predicate<String> isPatternStmt = (stm) -> stm.contains("pattern".toUpperCase());
		BiFunction<String,String,Optional<String>> replaceForPattern = (stm,pattern) -> {
			return  Optional.of(stm.replace(pattern, String.format("%s a.%s as %s,",
					pattern, 
					CommonSchema.EVENT.flowFileAttributes, 
					CommonSchema.EVENT.flowFileAttributes)));
		};
		BiFunction<String,String,Optional<String>> replaceForNotPattern = (stm,pattern) -> {
			return Optional.of(stm.replace(pattern, 
					String.format("%s %s,", pattern, CommonSchema.EVENT.flowFileAttributes)));
		};
		String pattern =
			    patterns.stream()
			   .filter(p -> userStmt.contains(p))
			   .findFirst()
			   .orElse("select".toUpperCase());
	    Optional<String> optResult = funcStmt.filter(isPatternStmt)
	    		.map(stmt -> replaceForPattern.apply(stmt,pattern))
	    		.orElse(replaceForNotPattern.apply(funcStmt.get(),pattern));
		return optResult.get();
	}
}
