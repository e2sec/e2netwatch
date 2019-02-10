package de.e2security.processors.e2esper.processor;

import static de.e2security.processors.e2esper.utilities.CommonPropertyDescriptor.EPL_STATEMENT;
import static de.e2security.processors.e2esper.utilities.CommonPropertyDescriptor.ESPER_ENGINE;
import static de.e2security.processors.e2esper.utilities.CommonPropertyDescriptor.EVENT_SCHEMA;
import static de.e2security.processors.e2esper.utilities.CommonPropertyDescriptor.getDescriptors;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.io.IOUtils;
import org.apache.nifi.annotation.documentation.CapabilityDescription;
import org.apache.nifi.annotation.documentation.Tags;
import org.apache.nifi.annotation.lifecycle.OnScheduled;
import org.apache.nifi.annotation.lifecycle.OnStopped;
import org.apache.nifi.annotation.lifecycle.OnUnscheduled;
import org.apache.nifi.components.PropertyDescriptor;
import org.apache.nifi.flowfile.FlowFile;
import org.apache.nifi.processor.AbstractProcessor;
import org.apache.nifi.processor.ProcessContext;
import org.apache.nifi.processor.ProcessSession;
import org.apache.nifi.processor.ProcessorInitializationContext;
import org.apache.nifi.processor.Relationship;
import org.apache.nifi.processor.exception.ProcessException;

import com.espertech.esper.client.EPException;
import com.espertech.esper.client.EPServiceProvider;

import de.e2security.nifi.controller.esper.EsperService;
import de.e2security.processors.e2esper.utilities.CommonSchema;
import de.e2security.processors.e2esper.utilities.EventTransformer;
import de.e2security.processors.e2esper.utilities.SupportUtility;
import de.e2security.processors.e2esper.utilities.TransformerWithAttributes;

@Tags({"E2EsperProcessor"})
@CapabilityDescription("Sending incoming events to esper engine)")
public class EsperConsumer extends AbstractProcessor {

	private volatile EPServiceProvider esperEngine;
	final private AtomicReference<String> eventNameRef = new AtomicReference<>();

	@OnStopped public void stop(final ProcessContext context) {}
	
	// Processor lifecycle on stop: firstly @OnUnscheduled, after that only - @OnStopped.
	@OnUnscheduled public void unschedule(final ProcessContext context) { }

	@OnScheduled public void start(final ProcessContext context) {
		//assign EPServiceProvider from Controller Service 
		{
			final EsperService esperService = context.getProperty(ESPER_ENGINE).asControllerService(EsperService.class);
			esperEngine = esperService.execute(); //instantiated on controller's ENABLEMENT. execute() returns the shared instance back; 
		}
		//modify user defined statements
		final String eventSchema = context.getProperty(EVENT_SCHEMA).evaluateAttributeExpressions().getValue();
		final String modifiedEventSchema = SupportUtility.modifyUserDefinedSchema(eventSchema);
		final String eplStatement = context.getProperty(EPL_STATEMENT).evaluateAttributeExpressions().getValue();
		final String modifiedEPStatement = SupportUtility.modifyUserDefinedEPStatement(eplStatement);
		//set event name retrieved from event schema
		{
			eventNameRef.set(SupportUtility.retrieveClassNameFromSchemaEPS(eventSchema));
		}
		//set new statement within try-catch block as the stmt may be not correct
		try {
			esperEngine.getEPAdministrator().createEPL(modifiedEventSchema);
			getLogger().info(String.format("initialized schema [%s]", modifiedEventSchema));
			esperEngine.getEPAdministrator().createEPL(modifiedEPStatement);
			getLogger().info(String.format("initialized stmt [%s]", modifiedEPStatement));
		} catch (EPException epx) {
			throw new ProcessException(String.format("cannot register epl statement by esper engine: [%s]", 
					epx.getMessage()));
		}
	}

	@Override public void onPropertyModified(PropertyDescriptor descriptor, String oldValue, String newValue) {
		final AtomicReference<String> stmtNameRef = new AtomicReference<>();
		Optional.ofNullable(oldValue).ifPresent(old -> { //cause the method to be triggered also during initial property setup
			if (descriptor.getName().equals(CommonSchema.PropertyDescriptorNames.EplStatement.toString())
					|| descriptor.getName().equals(CommonSchema.PropertyDescriptorNames.EventSchema.toString())) {
				stmtNameRef.set(SupportUtility.retrieveStatementName(old));
				getLogger().info(String.format("[%s] property has been modified. OLD VALUE: [%s] - NEW VALUE [%s], ",
						descriptor.getDisplayName(), oldValue, newValue));
				SupportUtility.destroyStmtIfAvailable(Optional.ofNullable(
						esperEngine.getEPAdministrator().getStatement(stmtNameRef.get())), getLogger());
				getLogger().info(String.format("epl statement [%s] has been successfully removed from esper controller service", stmtNameRef.get()));
			}
		});
		super.onPropertyModified(descriptor, oldValue, newValue);
	}

	@Override public void onTrigger(ProcessContext context, ProcessSession session) throws ProcessException {
		final FlowFile flowFile = session.get();
		if (flowFile == null) { return ; }
		final AtomicReference<String> input = new AtomicReference<>();
		session.read(flowFile, (inputStream) -> {
			try {
				input.set(IOUtils.toString(inputStream, StandardCharsets.UTF_8)); 
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});
		//send event with flow file attribute properties
		final EventTransformer transformer = new TransformerWithAttributes(flowFile);
		try { 
			final Map<String,Object> eventAsMap = transformer.transform(input.get());
			esperEngine.getEPRuntime().sendEvent(eventAsMap, eventNameRef.get());
			getLogger().debug(String.format("sent event [%s]:[%s]", eventNameRef.get(), SupportUtility.transformEventMapToJson(eventAsMap)));
		} catch (IOException e) { e.printStackTrace(); }

		session.remove(flowFile);
	}

	private List<PropertyDescriptor> descriptors;

	private Set<Relationship> relationships;

	@Override
	protected void init(ProcessorInitializationContext context) {
		final Set<Relationship> relationships = new HashSet<Relationship>();
		this.descriptors = Collections.unmodifiableList(getDescriptors());
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
