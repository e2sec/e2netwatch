package de.e2security.processors.e2esper.utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.nifi.components.PropertyDescriptor;
import org.apache.nifi.components.ValidationContext;
import org.apache.nifi.components.ValidationResult;
import org.apache.nifi.components.Validator;
import org.apache.nifi.expression.ExpressionLanguageScope;
import org.apache.nifi.processor.util.StandardValidators;

import de.e2security.nifi.controller.esper.EsperService;

public class CommonPropertyDescriptor {
	//TODO: read from the file
	private final static String EPL_RES_KEYWORDS = "after all and as at asc avedev avg between by case cast coalesce context count create current_timestamp "
										 + "dataflow day days delete define desc distinct else end escape events every exists expression false first "
										 + "for from full group having hour hours in initiated inner insert instanceof into irstream is istream join "
										 + "last lastweekday left limit like max match_recognize matched matches median measures merge metadatasql min "
										 + "minute minutes msec millisecond milliseconds new not null offset on or order outer output partition pattern "
										 + "prev prior regexp retain-union retain-intersection right rstream sec second seconds select set some snapshot "
										 + "sql start stddev sum terminated then true unidirectional until update using variable weekday when where while "
										 + "window ";

	private static Validator stmtNameValidator = new Validator() {
		@Override
		public ValidationResult validate(String subject, String input, ValidationContext context) {
			if (context.isExpressionLanguageSupported(subject) && context.isExpressionLanguagePresent(input)) {
				return new ValidationResult.Builder().subject(subject).input(input).explanation("Expression Language Present").valid(true).build();
			}
			boolean containsName = input.matches(".*@Name(.+).+");
			return new ValidationResult.Builder().subject(subject).input(input).explanation("Statement should contain @Name attribute").valid(containsName).build();
		}
	};
	
	private static Validator eplKeywordsValidator = new Validator() {
		
		final ArrayList<String> keywordsUpperCaseAsArr = 
				new ArrayList<>(Arrays.asList(EPL_RES_KEYWORDS.split(" "))).stream()
					.map(kw -> kw.toUpperCase())
					.collect(Collectors.toCollection(ArrayList::new));
		final Set<String> keywordsUpperCaseAsSet = new HashSet<>(keywordsUpperCaseAsArr);
		
		@Override
		public ValidationResult validate(String subject, String input, ValidationContext context) {
			if (context.isExpressionLanguageSupported(subject) && context.isExpressionLanguagePresent(input)) {
				return new ValidationResult.Builder().subject(subject).input(input).explanation("Expression Language Present").valid(true).build();
			}
			
			final ArrayList<String> inputArr = new ArrayList<>(Arrays.asList(input.split(" ")));
			boolean res = inputArr.stream()
					.filter(token -> keywordsUpperCaseAsSet.contains(token.toUpperCase())) //filter out not keywords
					.allMatch(kwToken -> keywordsUpperCaseAsSet.contains(kwToken)); //match kwords against kwords in upper case
			
			return new ValidationResult.Builder().subject(subject).input(input).explanation("Esper keywords should be in upper case").valid(res).build();
		}
		
	};
	

	public static final PropertyDescriptor ESPER_ENGINE = new PropertyDescriptor.Builder().name("EsperEngine")
			.displayName("EsperEngineService")
			.description("esper main engine")
			.required(true)
			.identifiesControllerService(EsperService.class)
			.build();

	public static final PropertyDescriptor EPL_STATEMENT = new PropertyDescriptor.Builder()
			.name(CommonSchema.PropertyDescriptorNames.EplStatement.toString())
			.displayName(CommonSchema.PropertyDescriptorNames.EplStatement.toString())
			.description("epl statement. @Name(<NAME>) anno should be provided. Events can be retrieved with Esper Producer by provided name")
			.required(true)
			.addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
			.addValidator(stmtNameValidator)
			.addValidator(eplKeywordsValidator)
			.expressionLanguageSupported(ExpressionLanguageScope.VARIABLE_REGISTRY)
			.build();

	public static final PropertyDescriptor EVENT_SCHEMA = new PropertyDescriptor.Builder()
			.name(CommonSchema.PropertyDescriptorNames.EventSchema.toString())
			.displayName(CommonSchema.PropertyDescriptorNames.EventSchema.toString())
			//TODO: write validator regarding stmt pattern
			.description("schema for input event defined with EPL regarding the following pattern: '@Name(<_NAME>) CREATE SCHEMA <NAME> AS (<property_name> <property_type>); Esper Keywords should be written in UPPER CASE")
			.required(true)
			.addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
			.addValidator(stmtNameValidator)
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
