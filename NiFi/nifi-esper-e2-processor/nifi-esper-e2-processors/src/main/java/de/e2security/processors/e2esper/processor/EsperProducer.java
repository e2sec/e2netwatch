package de.e2security.processors.e2esper.processor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.nifi.annotation.documentation.CapabilityDescription;
import org.apache.nifi.annotation.documentation.Tags;
import org.apache.nifi.annotation.lifecycle.OnConfigurationRestored;
import org.apache.nifi.annotation.lifecycle.OnScheduled;
import org.apache.nifi.annotation.lifecycle.OnStopped;
import org.apache.nifi.annotation.lifecycle.OnUnscheduled;
import org.apache.nifi.components.PropertyDescriptor;
import org.apache.nifi.expression.ExpressionLanguageScope;
import org.apache.nifi.processor.AbstractSessionFactoryProcessor;
import org.apache.nifi.processor.ProcessContext;
import org.apache.nifi.processor.ProcessSessionFactory;
import org.apache.nifi.processor.ProcessorInitializationContext;
import org.apache.nifi.processor.Relationship;
import org.apache.nifi.processor.exception.ProcessException;
import org.apache.nifi.processor.util.StandardValidators;

import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.annotation.Description;

import de.e2security.nifi.controller.esper.EsperService;
import de.e2security.processors.e2esper.listener.EsperListener;

@Tags({"E2EsperProcessor"})
@CapabilityDescription("Producing events from listener based on esper engine rules)")
@Description("It always recommended to stop EsperProducer first, before EsperConsumer will be stopped")
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
			.expressionLanguageSupported(ExpressionLanguageScope.VARIABLE_REGISTRY)
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
	
	private volatile EsperListener successListener;
	private final AtomicReference<EPStatement> stmtRef = new AtomicReference<>();
	
	@OnUnscheduled public void unschedule(final ProcessContext context) {}
	
	@OnScheduled public void schedule(final ProcessContext context) {
		/*
		 * instantiated on controller's ENABLEMENT. execute() returns the shared instance back;
		 * no NULL check is required -> the processor cannot be started w/o controller has been enabled
		 */
		final EsperService esperService = context.getProperty(ESPER_ENGINE).asControllerService(EsperService.class);
		final String stmtName = context.getProperty(EPSTMT_NAME).evaluateAttributeExpressions().getValue();
		final EPServiceProvider esperEngine = esperService.execute();
		/*
		 * if producer and consumer haven been started simultaneously (as pair in Process Group) or 
		 * producer has been started before consumer => wait for EPStatement initilization 
		 */
		while (stmtRef.compareAndSet(null, esperEngine.getEPAdministrator().getStatement(stmtName))) {
			getLogger().error(String.format("Searching for EPStatement called [%s]", stmtName));
		}
		
		getLogger().info(String.format("[%s] EPStatement has been found: [%s]", stmtName, stmtRef.get().getText()));
		
		//set new listener only if no any provided
		if (!stmtRef.get().getUpdateListeners().hasNext()) {
			successListener = new EsperListener(getLogger(), SUCCEEDED_REL);
			stmtRef.get().addListener(successListener);
			getLogger().info(String.format("successfully added update listener [%s]", successListener.getClass().getSimpleName()));
		}
	}
	
	@Override public void onPropertyModified(PropertyDescriptor descriptor, String oldValue, String newValue) {
		stmtRef.set(null);
		super.onPropertyModified(descriptor, oldValue, newValue);
	}

	@OnStopped public void stop(final ProcessContext context) {}
	
	@Override
	public void onTrigger(ProcessContext context, ProcessSessionFactory sessionFactory) throws ProcessException {
		successListener.setSession(sessionFactory);
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

