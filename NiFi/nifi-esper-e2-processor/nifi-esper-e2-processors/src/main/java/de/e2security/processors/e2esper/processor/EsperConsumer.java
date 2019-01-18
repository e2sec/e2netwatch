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
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.io.IOUtils;
import org.apache.nifi.annotation.documentation.CapabilityDescription;
import org.apache.nifi.annotation.documentation.Tags;
import org.apache.nifi.annotation.lifecycle.OnScheduled;
import org.apache.nifi.annotation.lifecycle.OnStopped;
import org.apache.nifi.components.PropertyDescriptor;
import org.apache.nifi.flowfile.FlowFile;
import org.apache.nifi.processor.AbstractProcessor;
import org.apache.nifi.processor.ProcessContext;
import org.apache.nifi.processor.ProcessSession;
import org.apache.nifi.processor.ProcessorInitializationContext;
import org.apache.nifi.processor.Relationship;
import org.apache.nifi.processor.exception.ProcessException;

import com.espertech.esper.client.EPServiceProvider;

import de.e2security.nifi.controller.esper.EsperService;
import de.e2security.processors.e2esper.utilities.SupportUtility;

@Tags({"E2EsperProcessor"})
@CapabilityDescription("Sending incoming events to esper engine)")
public class EsperConsumer extends AbstractProcessor {
	
	private volatile EPServiceProvider esperEngine;
	
	@OnStopped public void stop(final ProcessContext context) {}
	
	final private AtomicReference<String> eventName = new AtomicReference<String>();
	
	@OnScheduled public void start(final ProcessContext context) {
		final EsperService esperService = context.getProperty(ESPER_ENGINE).asControllerService(EsperService.class);
		esperEngine = esperService.execute(); //instantiated on controller's ENABLEMENT. execute() returns the shared instance back; 
		esperEngine.getEPAdministrator().createEPL(context.getProperty(EVENT_SCHEMA).getValue());
		esperEngine.getEPAdministrator().createEPL(context.getProperty(EPL_STATEMENT).getValue());
		eventName.compareAndSet(null, SupportUtility.retrieveClassNameFromSchemaEPS(context.getProperty(EVENT_SCHEMA).getValue()));
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
		try { esperEngine.getEPRuntime().sendEvent(SupportUtility.transformEventToMap(input.get()), eventName.get());
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
