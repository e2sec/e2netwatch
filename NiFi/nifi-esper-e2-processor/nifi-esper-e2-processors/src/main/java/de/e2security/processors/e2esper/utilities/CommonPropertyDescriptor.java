package de.e2security.processors.e2esper.utilities;

import java.util.ArrayList;
import java.util.List;

import org.apache.nifi.components.PropertyDescriptor;
import org.apache.nifi.expression.ExpressionLanguageScope;
import org.apache.nifi.processor.util.StandardValidators;

import de.e2security.nifi.controller.esper.EsperService;

public class CommonPropertyDescriptor {
	
	public static final PropertyDescriptor ESPER_ENGINE = new PropertyDescriptor.Builder().name("EsperEngine")
			.displayName("EsperEngineService")
			.description("esper main engine")
			.required(true)
			.identifiesControllerService(EsperService.class)
			.build();
	
	public static final PropertyDescriptor EPL_STATEMENT = new PropertyDescriptor.Builder()
			.name("EplStatement")
			.displayName("EplStatement")
			.description("epl statement. @Name('<NAME>') anno should be provided. Events can be retrieved with Esper Producer by provided name")
			.required(true)
			.addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
			.expressionLanguageSupported(ExpressionLanguageScope.VARIABLE_REGISTRY)
			.build();

	public static final PropertyDescriptor EVENT_SCHEMA = new PropertyDescriptor.Builder()
			.name("InputEventSchema")
			.displayName("InputEventSchema")
			.description("define schema with EPL as string. 'create schema <NAME> as ( <parameter_name type> , ...)'; Keywords 'create' 'schema' 'as' are case-sensitive in this pattern")
			.required(true)
			.addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
			.expressionLanguageSupported(ExpressionLanguageScope.VARIABLE_REGISTRY)
			.build();
	
	public static List<PropertyDescriptor> getDescriptors() {
		final List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
		descriptors.add(EVENT_SCHEMA);
		descriptors.add(EPL_STATEMENT);
		descriptors.add(ESPER_ENGINE);
		return descriptors;
	}
}
