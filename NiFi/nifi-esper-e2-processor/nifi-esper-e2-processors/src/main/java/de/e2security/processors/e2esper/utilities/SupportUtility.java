package de.e2security.processors.e2esper.utilities;

import static de.e2security.processors.e2esper.utilities.EsperProcessorLogger.success;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.nifi.logging.ComponentLog;
import org.apache.nifi.processor.exception.ProcessException;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.metric.EngineMetric;
import com.espertech.esper.client.metric.MetricEvent;
import com.espertech.esper.client.metric.StatementMetric;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

public class SupportUtility {

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
		final Optional<String> eventNameOpt = Optional.ofNullable(StringUtils.substringBetween(eventSchema, "CREATE SCHEMA ", " AS").replaceAll("'", ""));
		return eventNameOpt.orElseThrow(() -> {
			return new ProcessException("no event name provided in schema definition. Please consider the pattern 'CREATE SCHEMA <Name> AS (...)'");
		});
	}

	//Logical test (not test of method) is in nw104/EsperBehaviourTest.java
	public static String modifyUserDefinedSchema(String userSchema) {
		final Pattern pattern = Pattern.compile("[a\\s]\\W*\\("); 
		final String extendedThroughAttrMap = userSchema.replaceFirst(pattern.toString(), String.format("( %s Map,", CommonSchema.EVENT.flowFileAttributes));
		return extendedThroughAttrMap;
	}

	public static String modifyUserDefinedEPStatement(String userStmt) {
		//no any modifications are needed, if ' select *' defined by user
		if (userStmt.contains("select *".toUpperCase())) return userStmt;
		//apply functional interface to stmt and process transform logic
		final Optional<String> funcStmt = Optional.of(userStmt); 
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

	public static String retrieveStatementName(String eplStatement) {
		//checker is needed since the check against variables is not provided by nifi processor 
		Optional<String> nameOpt = Optional.ofNullable(StringUtils.substringBetween(eplStatement,"@Name(",")"));
		nameOpt.ifPresent(name -> name.replaceAll("'", ""));
		return nameOpt.orElseThrow(() -> new ProcessException(
				"No name provided in EPStatement. Please consider to provide @Name() annotation with statement name"));		
	}

	/**
	 * remove old statement if available
	 * the most appropriate place for this action would be @OnStopped or @OnScheduled annotated methods, 
	 * however the written tests fail, since TestRunner tries to stop processor during tests
	 */
	public static void destroyStmtIfAvailable(Optional<EPStatement> stmtOpt, ComponentLog logger) {
		stmtOpt.ifPresent( (stmt) -> {
			while (!stmt.isDestroyed()) {
				logger.info(String.format("trying to destroy [%s] statement", stmt.getName()));
				stmt.removeAllListeners();
				stmt.destroy();				
			}
			logger.info(String.format("[%s] statement has been successfully destroyed", stmt.getName()));
		});
	}
}
