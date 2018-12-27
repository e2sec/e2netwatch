package de.e2security.processors.e2esper;

import java.io.IOException;
import java.io.InputStream;
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

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import com.espertech.esper.client.scopetest.SupportUpdateListener;
import com.espertech.esper.event.map.MapEventBean;

import de.e2security.nifi.controller.esper.EsperService;
import de.e2security.processors.e2esper.utilities.SupportUtility;
import static de.e2security.processors.e2esper.CommonPropertyDescriptor.*;
import static de.e2security.processors.e2esper.utilities.UnwantedProtocolProcessorHelper.*;

@Tags({"EsperProcessor"})
@CapabilityDescription("Processing events based on esper engine rules")
@SeeAlso({})
@ReadsAttributes({@ReadsAttribute(attribute="", description="")})
@WritesAttributes({@WritesAttribute(attribute="", description="")})
@SuppressWarnings("unused")
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
    
    AtomicReference<String> alertEvent = new AtomicReference<>();
    
    UpdateListener unwantedProtocolsListener = new UpdateListener() {
		@Override
		public void update(EventBean[] newEvents, EventBean[] oldEvents) {
				getLogger().info("Esper listener has detected a new event...");
				try {
					for (EventBean event : newEvents) {
						if (event instanceof MapEventBean) {
							String catchedEventAsMapEntry = ((Map<?,?>) event.getUnderlying()).entrySet().toString();
							alertEvent.set(catchedEventAsMapEntry);
						}
					}
				} catch (Exception ex) {
					getLogger().error("ERROR UpdateListener cannot read underlying object...");
					ex.printStackTrace();
				}
			}
	};
	
    @Override
    public void onTrigger(final ProcessContext context, final ProcessSession session) throws ProcessException {
        FlowFile flowFile = session.get();
        if ( flowFile == null ) { return;}

        //stmt preparations
        String tcpPorts = context.getProperty(UNWANTED_TCP_PORTS_LIST).getValue();
    	String udpPorts = context.getProperty(UNWANTED_UDP_PORTS_LIST).getValue();
        String conditionalExpr = concatenatePorts(tcpPorts, udpPorts);
        
        //select all since retrieving of particular fields is possible through SelectAlarmsDetectedEplStatement PropertyDescriptor;
        String detectUnwantedProtocolsEplStatement = 
        		  context.getProperty(EPL_STATEMENT).getValue()
        		+ "("
        		+ conditionalExpr
        		+ ")";
        
        //define esper main instances
		EsperService esperService = context.getProperty(ESPER_ENGINE).asControllerService(EsperService.class);
		EPServiceProvider esperEngine = esperService.execute();
        EPRuntime runtime = esperEngine.getEPRuntime();
        EPAdministrator admin = esperEngine.getEPAdministrator();
        
        //creating schema on the fly from the nifi attributes
        admin.createEPL(context.getProperty(EVENT_SCHEMA).getValue());
        
        //processing incoming nifi events
       
        session.read(flowFile, new InputStreamCallback() {
			@Override
			public void process(InputStream inputStream) throws IOException {
				EPStatement blacklistProtocols = admin.createEPL(detectUnwantedProtocolsEplStatement);
				SupportUpdateListener support = new SupportUpdateListener();
				blacklistProtocols.addListener(unwantedProtocolsListener);
	        	try {
	        		String eventJson = IOUtils.toString(inputStream); //implying just one event due to tests (read from file)
	        		Map<String,Object> eventMap = SupportUtility.transformEventToMap(eventJson);
	        		runtime.sendEvent(eventMap,context.getProperty(INBOUND_EVENT_NAME).getValue());
	        	} catch (Exception ex) {
	        		ex.printStackTrace();
	        		getLogger().error("ERROR processing incoming event");
	        	}
			}
        });
        
      session.write(flowFile, (outStream) -> {
    	 getLogger().info("trying to write output...");
   		 outStream.write(alertEvent.get().getBytes()); 
   		 getLogger().info(alertEvent.get());
      });
      session.transfer(flowFile, UNWANTED_TCP_CONNECTIONS);
    }
}
