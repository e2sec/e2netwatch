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
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
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

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;

@Tags({"EsperProcessor"})
@CapabilityDescription("Processing events based on esper engine rules")
@SeeAlso({})
@ReadsAttributes({@ReadsAttribute(attribute="", description="")})
@WritesAttributes({@WritesAttribute(attribute="", description="")})
public class UnwantedProtocolsAlarmProcessor extends AbstractProcessor {

    public static final PropertyDescriptor BlacklistFilter = new PropertyDescriptor
            .Builder().name("BlacklistFilter")
            .displayName("BlacklistFilter")
            .description("Conditional (WHERE)-poststatement: E.g. protocol=6 and source.port=25 or destination.port=80")
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
        descriptors.add(BlacklistFilter);
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
    	Properties protocolRegisterSchema = new Properties();
    	protocolRegisterSchema.put("description","string");
    	protocolRegisterSchema.put("host.ip","string");
    	protocolRegisterSchema.put("source.ip","string");
    	protocolRegisterSchema.put("source.port","int");
    	protocolRegisterSchema.put("destination.ip","string");
    	protocolRegisterSchema.put("destination.port","int");
    	protocolRegisterSchema.put("network.iana_number","int");
    	protocolRegisterSchema.put("netflow.inbound.sequence_number","int");
    	protocolRegisterSchema.put("netflow.inbound.records","int");
    	protocolRegisterSchema.put("netflow.outbound.sequence_number","int");
    	protocolRegisterSchema.put("netflow.outbound.records","int");
    	protocolRegisterSchema.put("network.inbound.bytes","int");
    	protocolRegisterSchema.put("network.outbound.bytes","int");
    	protocolRegisterSchema.put("network.inbound.packets","int");
    	protocolRegisterSchema.put("network.outbound.packets","int");
    	protocolRegisterSchema.put("network.inbound.tcp_flags","int");
    	protocolRegisterSchema.put("network.outbound.tcp_flags","int");
    	protocolRegisterSchema.put("netflow.inbound.first_switched","string");
    	protocolRegisterSchema.put("netflow.outbound.first_switched","string");
    	protocolRegisterSchema.put("netflow.inbound.last_switched","string");
    	protocolRegisterSchema.put("netflow.outbound.last_switched","string");
    	Properties alarmedConnectionSchema = new Properties();
    	alarmedConnectionSchema.put("source.port", "int");
    	alarmedConnectionSchema.put("destination.port", "int");
        FlowFile flowFile = session.get();
        if ( flowFile == null ) { return;}
        //setting esper engine settings
        Configuration config = new Configuration();
        config.addEventType("ProtocolRegister", protocolRegisterSchema);
        config.addEventType("AlarmedConnection", alarmedConnectionSchema);
        EPServiceProvider engine = EPServiceProviderManager.getDefaultProvider(config);
        EPRuntime runtime = engine.getEPRuntime();
        EPAdministrator admin = engine.getEPAdministrator();
        //processing incoming nifi events
        final AtomicReference<Pair<Integer,Integer>> alertEvent = new AtomicReference<>();
        session.read(flowFile, new InputStreamCallback() {
			@Override
			public void process(InputStream inputStream) throws IOException {
				EPStatement blacklistProtocols = admin.createEPL(
						"insert rstream into AlarmedConnection "
								+ "select source\\.port, destination\\.port "
								+ "from ProtocolRegister("
								+ 	context.getProperty(BlacklistFilter).getValue()
								+ ")"
						);
				EPStatement selectAlarms = admin.createEPL("select * from AlarmedConnection");
	        	try {
	        		String event = IOUtils.toString(inputStream); //implying just one event due to tests (read from file)
	        		runtime.sendEvent(event);
	        	} catch (Exception ex) {
	        		ex.printStackTrace();
	        		getLogger().error("ERROR processing incoming event");
	        	}
	        	selectAlarms.addListener( (newEvents, oldEvents) -> {
	        		getLogger().info("within listener...");
	        		try {
	        			int src = (int) Integer.valueOf(newEvents[0].get("source.port").toString());
	        			getLogger().info("SRC: " + src);
	        			int dst = (int) Integer.valueOf(newEvents[0].get("destination.port").toString());
	        			getLogger().info("DST: " + dst);
	        			Pair<Integer,Integer> src_dst = new ImmutablePair<>(src,dst);
	        			getLogger().info("alerting the following pair: " + src_dst.getLeft() + "-" + src_dst.getRight());
	        			alertEvent.set(src_dst);
	        		} catch (Exception ex) {
	        			getLogger().error("ERROR parsing events came into BlacklistProtocol listener");
	        			ex.printStackTrace();
	        		}
	        	});
			}
        });
      session.write(flowFile, (outStream) -> {
    	 getLogger().info("trying to write output...");
   		 outStream.write(("" + alertEvent.get().getLeft() + alertEvent.get().getRight()).getBytes()); //serializing
      });
      session.transfer(flowFile, AlarmedEvent);
    }
}
