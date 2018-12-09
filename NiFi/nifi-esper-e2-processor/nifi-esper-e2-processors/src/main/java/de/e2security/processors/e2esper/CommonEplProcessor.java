package de.e2security.processors.e2esper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.io.IOUtils;
import org.apache.nifi.annotation.behavior.ReadsAttribute;
import org.apache.nifi.annotation.behavior.ReadsAttributes;
import org.apache.nifi.annotation.behavior.WritesAttribute;
import org.apache.nifi.annotation.behavior.WritesAttributes;
import org.apache.nifi.annotation.documentation.CapabilityDescription;
import org.apache.nifi.annotation.documentation.SeeAlso;
import org.apache.nifi.annotation.documentation.Tags;
import org.apache.nifi.annotation.lifecycle.OnScheduled;
import org.apache.nifi.components.PropertyDescriptor;
import org.apache.nifi.flowfile.FlowFile;
import org.apache.nifi.processor.AbstractProcessor;
import org.apache.nifi.processor.ProcessContext;
import org.apache.nifi.processor.ProcessSession;
import org.apache.nifi.processor.ProcessorInitializationContext;
import org.apache.nifi.processor.Relationship;
import org.apache.nifi.processor.exception.ProcessException;
import org.apache.nifi.processor.io.InputStreamCallback;
import org.apache.nifi.processor.util.StandardValidators;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.event.map.MapEventBean;

import de.e2security.processors.e2esper.utilities.SupportUtility;

@Tags({"EsperProcessor"})
@CapabilityDescription("Processing events based on esper engine rules for statical analysis (e.g. w/o time frames/windows, rather with filtering/findings queries)")
@SeeAlso({})
@ReadsAttributes({@ReadsAttribute(attribute="", description="")})
@WritesAttributes({@WritesAttribute(attribute="", description="")})
public class CommonEplProcessor extends AbstractProcessor {
	
	public static final PropertyDescriptor EplStatement = new PropertyDescriptor.Builder()
	.name("EplStatement")
	.displayName("EplStatement")
	.description("epl statement")
	.required(true)
	.addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
	.build();
	
	public static final PropertyDescriptor NameOfInboundEvent = new PropertyDescriptor.Builder()
	.name("InboundEventName")
	.displayName("InboundEventName")
	.description("name of incoming event against which epl statement should be evaluated")
	.required(true)
	.addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
	.build();

	public static final PropertyDescriptor EventSchema = new PropertyDescriptor.Builder()
	.name("InputEventSchema")
	.displayName("InputEventSchema")
	.description("define schema with EPL as string. In case of complex event schema declaration divide multiple strings with '|'")
	.required(true)
	.addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
	.build();

	public static final Relationship SuccessEvent = new Relationship.Builder()
	.name("SuccessEvent")
	.description("SuccessEvent")
	.build();

	private List<PropertyDescriptor> descriptors;

	private Set<Relationship> relationships;

	@Override
	protected void init(final ProcessorInitializationContext context) {
		final List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
		descriptors.add(EventSchema);
		descriptors.add(EplStatement);
		descriptors.add(NameOfInboundEvent);
		this.descriptors = Collections.unmodifiableList(descriptors);

		final Set<Relationship> relationships = new HashSet<Relationship>();
		relationships.add(SuccessEvent);
		this.relationships = Collections.unmodifiableSet(relationships);
	}

	@Override
	public Set<Relationship> getRelationships() {
		return this.relationships;
	}

	@Override
	public final List<PropertyDescriptor> getSupportedPropertyDescriptors() {
		return descriptors;
	}

	@OnScheduled
	public void onScheduled(final ProcessContext context) {

	}

	@Override
	public void onTrigger(final ProcessContext context, final ProcessSession session) throws ProcessException {
		FlowFile flowFile = session.get();
		if ( flowFile == null ) { return;}
		//define esper's units
		Configuration config = new Configuration();
		config.getEngineDefaults().getLogging().setEnableExecutionDebug(true);
		config.getEngineDefaults().getLogging().setEnableTimerDebug(false);
		EPServiceProvider engine = EPServiceProviderManager.getDefaultProvider(config);
		EPRuntime runtime = engine.getEPRuntime();
		EPAdministrator admin = engine.getEPAdministrator();
		
		// parse each epl statement from array of strings defined in EplStatement
		SupportUtility.parseMultipleEventSchema(context.getProperty(EventSchema).getValue(), admin);
		EPStatement eplIn = admin.createEPL(context.getProperty(EplStatement).getValue());
		//processing incoming nifi events
		final AtomicReference<String> processedEvents = new AtomicReference<>();
		session.read(flowFile, new InputStreamCallback() {
			@Override
			public void process(InputStream inputStream) throws IOException {
				eplIn.addListener( (newEvents, oldEvents) -> {
					getLogger().info("Esper listener has detected a new incoming event...");
					try {
						for (EventBean event : newEvents) {
							if (event instanceof MapEventBean) {
								String catchedEventAsMapEntry = ((Map<?,?>) event.getUnderlying()).entrySet().toString();
								processedEvents.set(catchedEventAsMapEntry);
							}
						}
					} catch (Exception ex) {
						getLogger().error("ERROR UpdateListener cannot read underlying object...");
						ex.printStackTrace();
					}
				});
				try {
					String eventJson = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
					//parsing inputStream as JSON objects
					Map<String,Object> eventMap = SupportUtility.transformEventToMap(eventJson);
					runtime.sendEvent(eventMap, context.getProperty(NameOfInboundEvent).getValue());
				} catch (Exception ex) {
					ex.printStackTrace();
					getLogger().error("ERROR processing incoming event");
				}
			}
		});

		session.write(flowFile, (outStream) -> {
			getLogger().info("trying to write output...");
			outStream.write(processedEvents.get().getBytes()); 
		});
		session.transfer(flowFile, SuccessEvent);
	}
}
