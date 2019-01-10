package de.e2security.processors.e2esper.processor;

import static de.e2security.processors.e2esper.CommonPropertyDescriptor.EPL_STATEMENT;
import static de.e2security.processors.e2esper.CommonPropertyDescriptor.ESPER_ENGINE;
import static de.e2security.processors.e2esper.CommonPropertyDescriptor.EVENT_SCHEMA;
import static de.e2security.processors.e2esper.CommonPropertyDescriptor.INBOUND_EVENT_NAME;
import static de.e2security.processors.e2esper.CommonPropertyDescriptor.getDescriptors;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
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

public class EsperConsumer extends AbstractProcessor {
	
	public static final Relationship IGNORE = new Relationship.Builder()
			.name("null relationship")
			.description("terminate this relationship")
			.build();
	
	private EPServiceProvider esperEngine;
	
	@OnStopped public void stop(final ProcessContext context) {}
	
	@OnScheduled public void start(final ProcessContext context) {
		final EsperService esperService = context.getProperty(ESPER_ENGINE).asControllerService(EsperService.class);
		esperEngine = esperService.execute(); //instantiated on controller's ENABLEMENT. execute() returns the shared instance back; 
		esperEngine.getEPAdministrator().createEPL(context.getProperty(EVENT_SCHEMA).getValue());
		esperEngine.getEPAdministrator().createEPL(context.getProperty(EPL_STATEMENT).getValue());
	}

	@Override public void onTrigger(ProcessContext context, ProcessSession session) throws ProcessException {
		FlowFile flowFile = session.get();
		if (flowFile == null) { return ; }
		final String eventName = context.getProperty(INBOUND_EVENT_NAME).getValue();
		
		String[] input = new String[1];
		session.read(flowFile, (inputStream) -> {
			try {
				input[0] = IOUtils.toString(inputStream, StandardCharsets.UTF_8); 
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});
		try {
			Map<String, Object> eventMap = SupportUtility.transformEventToMap(input[0]);
			esperEngine.getEPRuntime().sendEvent(eventMap, eventName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		session.remove(flowFile);
	}
	
	private List<PropertyDescriptor> descriptors;
	
	private Set<Relationship> relationships;
	
	@Override
	protected void init(ProcessorInitializationContext context) {
		this.descriptors = Collections.unmodifiableList(getDescriptors());
		
		final Set<Relationship> relationships = new HashSet<Relationship>();
		relationships.add(IGNORE);
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
