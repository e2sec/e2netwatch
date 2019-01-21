package de.e2security.processors.e2esper;

import static de.e2security.processors.e2esper.utilities.CommonPropertyDescriptor.EPL_STATEMENT;
import static de.e2security.processors.e2esper.utilities.CommonPropertyDescriptor.ESPER_ENGINE;
import static de.e2security.processors.e2esper.utilities.CommonPropertyDescriptor.EVENT_SCHEMA;
import static de.e2security.processors.e2esper.utilities.UnwantedProtocolProcessorHelper.concatenatePorts;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

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
import org.apache.nifi.processor.io.InputStreamCallback;
import org.apache.nifi.processor.util.StandardValidators;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPStatement;

import de.e2security.nifi.controller.esper.EsperService;
import de.e2security.processors.e2esper.utilities.CommonPropertyDescriptor;
import de.e2security.processors.e2esper.utilities.SucceededEventListener;
import de.e2security.processors.e2esper.utilities.SupportUtility;
import de.e2security.processors.e2esper.utilities.UnmatchedEventListener;

@Tags({"EsperProcessor"})
@CapabilityDescription("Processing events based on esper engine rules")
@SeeAlso({})
@ReadsAttributes({@ReadsAttribute(attribute="", description="")})
@WritesAttributes({@WritesAttribute(attribute="", description="")})
@SuppressWarnings("unused")
@Deprecated
public class UnwantedProtocolProcessor extends AbstractProcessor {

    public static final PropertyDescriptor UNWANTED_TCP_PORTS_LIST = new PropertyDescriptor.Builder()
    		.name("UnwantedTcpPorts")
    		.displayName("UnwantedTcpPorts")
    		.description("List of unwanted tcp ports as comma-separated string")
    		.required(true)
    		.addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
    		.build();

    public static final PropertyDescriptor UNWANTED_UDP_PORTS_LIST = new PropertyDescriptor.Builder()
    		.name("UnwantedUdpPorts")
    		.displayName("UnwantedUdpPorts")
    		.description("List of unwanted udp ports as comma-separated string")
    		.required(true)
    		.addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
    		.build();

    public static final Relationship UNWANTED_UDP_CONNECTIONS = new Relationship.Builder()
            .name("unwanted udp connections")
            .description("event among unwanted udp connections")
            .build();

    public static final Relationship UNWANTED_TCP_CONNECTIONS = new Relationship.Builder()
            .name("unwanted tcp connections")
            .description("event among unwanted tcp connections")
            .build();
    
    public static final Relationship THROUGHPUT = new Relationship.Builder()
    		.name("throughput of unfiltered connections")
    		.description("throughput of unfiltered connections")
    		.build();
    
    private List<PropertyDescriptor> descriptors;

    private Set<Relationship> relationships;

    @Override
    protected void init(final ProcessorInitializationContext context) {
        final List<PropertyDescriptor> descriptors = CommonPropertyDescriptor.getDescriptors();
        descriptors.add(UNWANTED_TCP_PORTS_LIST);
        descriptors.add(UNWANTED_UDP_PORTS_LIST);
        this.descriptors = Collections.unmodifiableList(descriptors);

        final Set<Relationship> relationships = new HashSet<Relationship>();
        relationships.add(UNWANTED_UDP_CONNECTIONS);
        relationships.add(UNWANTED_TCP_CONNECTIONS);
        relationships.add(THROUGHPUT);
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
        final AtomicReference<Optional<Pair<String,Relationship>>> finalResult = new AtomicReference<>();
        UnmatchedEventListener unmatchedEventListener = new UnmatchedEventListener(getLogger(),THROUGHPUT,finalResult);
        //use one of them on demand depending on the input protocol
        Supplier<SucceededEventListener> succeededTcpEventListener = () -> new SucceededEventListener(getLogger(),UNWANTED_TCP_CONNECTIONS,finalResult);
        Supplier<SucceededEventListener> succeededUdpEventListener = () -> new SucceededEventListener(getLogger(),UNWANTED_UDP_CONNECTIONS,finalResult);
        
        //stmt preparations
        String tcpPorts = context.getProperty(UNWANTED_TCP_PORTS_LIST).getValue();
    	String udpPorts = context.getProperty(UNWANTED_UDP_PORTS_LIST).getValue();
        String conditionalExpr = concatenatePorts(tcpPorts, udpPorts);
        
        //select all since retrieving of particular fields is possible through SelectAlarmsDetectedEplStatement PropertyDescriptor;
        String detectUnwantedProtocolsEplStatement =  context.getProperty(EPL_STATEMENT).getValue()
        		+ "("
        		+   conditionalExpr
        		+ ")";
        
        //define esper main instances
		EsperService esperService = context.getProperty(ESPER_ENGINE).asControllerService(EsperService.class);
		EPServiceProvider esperEngine = esperService.execute();
        EPRuntime runtime = esperEngine.getEPRuntime();
        EPAdministrator admin = esperEngine.getEPAdministrator();
        
        runtime.setUnmatchedListener(unmatchedEventListener);
        
        //creating schema on the fly from the nifi attributes
        admin.createEPL(context.getProperty(EVENT_SCHEMA).getValue());
        
        //processing incoming nifi events
       
        session.read(flowFile, new InputStreamCallback() {
			@Override
			public void process(InputStream inputStream) throws IOException {
				EPStatement blacklistProtocols = admin.createEPL(detectUnwantedProtocolsEplStatement);
				String eventJson = IOUtils.toString(inputStream);
	        	try {
	        		Map<String,Object> eventMap = SupportUtility.transformEventToMap(eventJson,Optional.empty());
	        		Optional<Object> protocol = Optional.ofNullable(eventMap.get("network.iana_number"));
	        		int proto = protocol.map(obj -> (int) obj).orElse(0);
	        		switch (proto) {
					case 6:
						blacklistProtocols.addListener(succeededTcpEventListener.get());
						break;
					case 17:
						blacklistProtocols.addListener(succeededUdpEventListener.get());
						break;
	        		default:
	        			break;
	        		}
	        		runtime.sendEvent(eventMap, SupportUtility.retrieveClassNameFromSchemaEPS(context.getProperty(EPL_STATEMENT).getValue()));
	        	} catch (Exception ex) {
	        		ex.printStackTrace();
	        	}
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
