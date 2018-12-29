package de.e2security.processors.e2esper;

import static de.e2security.processors.e2esper.CommonPropertyDescriptor.EPL_STATEMENT;
import static de.e2security.processors.e2esper.CommonPropertyDescriptor.ESPER_ENGINE;
import static de.e2security.processors.e2esper.CommonPropertyDescriptor.EVENT_SCHEMA;
import static de.e2security.processors.e2esper.CommonPropertyDescriptor.INBOUND_EVENT_NAME;
import static de.e2security.processors.e2esper.CommonPropertyDescriptor.getDescriptors;
import static de.e2security.processors.e2esper.utilities.EsperProcessorLogger.failure;
import static de.e2security.processors.e2esper.utilities.EsperProcessorLogger.success;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.Pair;
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

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPException;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPStatement;

import de.e2security.nifi.controller.esper.EsperService;
import de.e2security.processors.e2esper.utilities.SucceededEventListener;
import de.e2security.processors.e2esper.utilities.SupportUtility;
import de.e2security.processors.e2esper.utilities.UnmatchedEventListener;

@Tags({"EsperProcessor"})
@CapabilityDescription("Processing events based on esper engine rules)")
@SeeAlso({})
@ReadsAttributes({@ReadsAttribute(attribute="", description="")})
@WritesAttributes({@WritesAttribute(attribute="", description="")})
public class CommonEplProcessor extends AbstractProcessor {
	
	public static final Relationship SUCCEEDED_EVENT = new Relationship.Builder()
			.name("succeeded event")
			.description("esper event matched epl statement")
			.build();

	public static final Relationship UNMATCHED_EVENT = new Relationship.Builder()
			.name("unmatched event")
			.description("esper event unmatched epl statement")
			.build();
	
	private List<PropertyDescriptor> descriptors;

	private Set<Relationship> relationships;

	@Override
	protected void init(final ProcessorInitializationContext context) {
		this.descriptors = Collections.unmodifiableList(getDescriptors());
		
		final Set<Relationship> relationships = new HashSet<Relationship>();
		relationships.add(SUCCEEDED_EVENT);
		relationships.add(UNMATCHED_EVENT);
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
	public void onScheduled(final ProcessContext context) {	}
	
	@Override
	public void onTrigger(final ProcessContext context, final ProcessSession session) throws ProcessException {
		FlowFile flowFile = session.get(); 
		if ( flowFile == null ) { return;} 
		final AtomicReference<Optional<Pair<String,Relationship>>> finalResult = new AtomicReference<>();
		//load esper engine from the controller
		EsperService esperService = context.getProperty(ESPER_ENGINE).asControllerService(EsperService.class);
		EPServiceProvider esperEngine = esperService.execute();
		EPRuntime runtime = esperEngine.getEPRuntime();
		EPAdministrator admin = esperEngine.getEPAdministrator();
		
		// parse each epl statement from array of strings defined in EplStatement
		SupportUtility.parseMultipleEventSchema(context.getProperty(EVENT_SCHEMA).getValue(),admin,getLogger());
		final String _EPL_STATEMENT = context.getProperty(EPL_STATEMENT).getValue();
		EPStatement eplIn = admin.createEPL(_EPL_STATEMENT); //do not try to catch error; this is a runtime critical one
		getLogger().debug(success("IMPLEMENTED EPL STMT", _EPL_STATEMENT));
		//processing incoming nifi events
		SucceededEventListener sel = new SucceededEventListener(getLogger(),SUCCEEDED_EVENT,finalResult);
		eplIn.addListener(sel);
		UnmatchedEventListener fel = new UnmatchedEventListener(getLogger(),UNMATCHED_EVENT,finalResult);
		runtime.setUnmatchedListener(fel);
		final String _INBOUND_EVENT_NAME = context.getProperty(INBOUND_EVENT_NAME).getValue(); 
		session.read(flowFile, (inputStream) -> {
			String eventJson = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
			try {
				//parsing inputStream as JSON objects
				Map<String,Object> eventMap = SupportUtility.transformEventToMap(eventJson);
				runtime.sendEvent(eventMap, _INBOUND_EVENT_NAME);
				getLogger().debug(success("PROCESSED EVENT AS MAP", eventMap.entrySet().toString()));
			} catch (EPException epx) {
				getLogger().error(epx.getMessage());
				epx.printStackTrace();
				getLogger().debug(failure("TRYING TO CREATE MAP FROM INCOMING EVENT",eventJson));
			}
		});
		
		Optional<Pair<String,Relationship>> resultOptional = finalResult.get();
		if (resultOptional.isPresent()) {
			session.write(flowFile, (outStream) -> {
				outStream.write(resultOptional.get().getLeft().getBytes());
			});
			session.transfer(flowFile,resultOptional.get().getRight());
		} else {
			getLogger().error("NEITHER SUCCEEDED NOR UNMATCHED EVENT PROCESSED TO FLOW FILE");
			session.transfer(flowFile,null);
		}
		
		session.commit();
	}
}
