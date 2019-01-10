package de.e2security.processors.e2esper.processor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.nifi.annotation.lifecycle.OnScheduled;
import org.apache.nifi.annotation.lifecycle.OnStopped;
import org.apache.nifi.components.PropertyDescriptor;
import org.apache.nifi.processor.AbstractSessionFactoryProcessor;
import org.apache.nifi.processor.ProcessContext;
import org.apache.nifi.processor.ProcessSession;
import org.apache.nifi.processor.ProcessSessionFactory;
import org.apache.nifi.processor.ProcessorInitializationContext;
import org.apache.nifi.processor.Relationship;
import org.apache.nifi.processor.exception.ProcessException;
import org.apache.nifi.processor.util.StandardValidators;

import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPStatement;

import de.e2security.nifi.controller.esper.EsperService;
import de.e2security.processors.e2esper.listener.EsperListener;

public class EsperProducer extends AbstractSessionFactoryProcessor {
	
	public static final PropertyDescriptor ESPER_ENGINE = new PropertyDescriptor.Builder().name("EsperEngine")
			.displayName("EsperEngineService")
			.description("esper main engine")
			.required(true)
			.identifiesControllerService(EsperService.class)
			.build();
	
	public static final PropertyDescriptor EPSTMT_NAME = new PropertyDescriptor.Builder()
			.name("EPStatementName")
			.displayName("EPStatement_Name")
			.description("name of EPStatement the result to be produced of")
			.required(true)
			.addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
			.build();
	
	public static List<PropertyDescriptor> getDescriptors() {
		final List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
		descriptors.add(ESPER_ENGINE);
		descriptors.add(EPSTMT_NAME);
		return descriptors;
	}
	
	public static final Relationship SUCCEEDED_REL = new Relationship.Builder()
			.name("succeeded event")
			.description("esper event matched epl statement on listener")
			.build();
	
	private EPServiceProvider esperEngine;
	private EPStatement epstmt;
	private String stmtName;
	
	@OnStopped public void stop(final ProcessContext context) {}
	
	@OnScheduled public void start(final ProcessContext context) {
		final EsperService esperService = context.getProperty(ESPER_ENGINE).asControllerService(EsperService.class);
		stmtName = context.getProperty(EPSTMT_NAME).getValue();
		/*
		 * instantiated on controller's ENABLEMENT. execute() returns the shared instance back;
		 * no NULL check is required -> the processor cannot be started w/o controller has been enabled
		 */
		esperEngine = esperService.execute();  
		epstmt = esperEngine.getEPAdministrator().getStatement(stmtName);
		if (epstmt == null) {
			getLogger().error(String.format("STMT [%s] doesn't exist or consumer has not been started yet", stmtName)); //throws an error on addListener(...)
		}
	}

	@Override
	public void onTrigger(ProcessContext context, ProcessSessionFactory sessionFactory) throws ProcessException {
		ProcessSession newSession = sessionFactory.createSession();
		EsperListener successListener = new EsperListener(getLogger(), 
														  stmtName, 
														  newSession,
														  SUCCEEDED_REL);
		epstmt.addListener(successListener);
		
		context.yield(); //delay on start
	}
	
	private List<PropertyDescriptor> descriptors;
	
	private Set<Relationship> relationships;
	
	@Override
	protected void init(ProcessorInitializationContext context) {
		this.descriptors = Collections.unmodifiableList(getDescriptors());
		
		final Set<Relationship> relationships = new HashSet<Relationship>();
		relationships.add(SUCCEEDED_REL);
		this.relationships = Collections.unmodifiableSet(relationships);
	}
	
	@Override
	public Set<Relationship> getRelationships() {
		return this.relationships;
	}
	
	@Override
	protected List<PropertyDescriptor> getSupportedPropertyDescriptors() {
		return this.descriptors;
	}

	
}

