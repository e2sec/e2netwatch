/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
import com.espertech.esper.event.map.MapEventBean;

@Tags({"EsperProcessor"})
@CapabilityDescription("Processing events based on esper engine rules")
@SeeAlso({})
@ReadsAttributes({@ReadsAttribute(attribute="", description="")})
@WritesAttributes({@WritesAttribute(attribute="", description="")})
public class UnwantedProtocolsAlarmProcessor extends AbstractProcessor {

    public static final PropertyDescriptor DetectUnwantedProtocolsEplStatement = new PropertyDescriptor.Builder()
    		.name("UnwantedProtocolsEplStmt")
    		.displayName("UnwantedProtocolsEplStmt")
    		.description("EPL Statement")
    		.required(true)
    		.addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
    		.build();

    public static final PropertyDescriptor SelectAlarmsDetectedEplStatement = new PropertyDescriptor.Builder()
    		.name("AlarmsDetectedSelectEplStatement")
    		.displayName("AlarmsDetectedSelectEplStatement")
    		.description("EPL Statement")
    		.required(true)
    		.addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
    		.build();
    
    public static final PropertyDescriptor CreateInputEventSchema = new PropertyDescriptor.Builder()
    		.name("InputEventSchema")
    		.displayName("InputEventSchema")
    		.description("define schema with EPL for input event (as map)")
    		.required(true)
    		.addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
    		.build();
    
    public static final PropertyDescriptor CreateOutputEventSchema = new PropertyDescriptor.Builder()
	.name("OuputEventSchema")
	.displayName("OutputEventSchema")
	.description("define schema with EPL for output event (as map)")
	.required(true)
	.addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
	.build();
    
    public static final Relationship AlarmedEvent = new Relationship.Builder()
            .name("AlarmedEventt")
            .description("Alarmed Event found from provided Blacklist")
            .build();

    private List<PropertyDescriptor> descriptors;

    private Set<Relationship> relationships;

    @Override
    protected void init(final ProcessorInitializationContext context) {
        final List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
        descriptors.add(DetectUnwantedProtocolsEplStatement);
        descriptors.add(SelectAlarmsDetectedEplStatement);
        descriptors.add(CreateInputEventSchema);
        descriptors.add(CreateOutputEventSchema);
        this.descriptors = Collections.unmodifiableList(descriptors);

        final Set<Relationship> relationships = new HashSet<Relationship>();
        relationships.add(AlarmedEvent);
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
        
        //define esper main instances
        EPServiceProvider engine = EPServiceProviderManager.getDefaultProvider();
        EPRuntime runtime = engine.getEPRuntime();
        EPAdministrator admin = engine.getEPAdministrator();
        
        //creating schema on the fly from the nifi attributes
        admin.createEPL(context.getProperty(CreateInputEventSchema).getValue());
        admin.createEPL(context.getProperty(CreateOutputEventSchema).getValue());
        
        //processing incoming nifi events
        final AtomicReference<String> alertEvent = new AtomicReference<>();
        session.read(flowFile, new InputStreamCallback() {
			@Override
			public void process(InputStream inputStream) throws IOException {
				EPStatement blacklistProtocols = admin.createEPL(context.getProperty(DetectUnwantedProtocolsEplStatement).getValue());
				EPStatement selectAlarms = admin.createEPL(context.getProperty(SelectAlarmsDetectedEplStatement).getValue());
	        	try {
	        		String event = IOUtils.toString(inputStream); //implying just one event due to tests (read from file)
	        		runtime.sendEvent(event);
	        	} catch (Exception ex) {
	        		ex.printStackTrace();
	        		getLogger().error("ERROR processing incoming event");
	        	}
	        	selectAlarms.addListener( (newEvents, oldEvents) -> {
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
	        	});
			}
        });
      session.write(flowFile, (outStream) -> {
    	 getLogger().info("trying to write output...");
   		 outStream.write(alertEvent.get().getBytes()); 
      });
      session.transfer(flowFile, AlarmedEvent);
    }
}
