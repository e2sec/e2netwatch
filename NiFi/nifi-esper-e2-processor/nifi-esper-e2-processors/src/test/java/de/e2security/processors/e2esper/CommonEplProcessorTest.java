package de.e2security.processors.e2esper;

import java.io.IOException;
import java.io.InputStream;

import org.apache.nifi.stream.io.ByteArrayInputStream;
import org.apache.nifi.util.TestRunner;
import org.apache.nifi.util.TestRunners;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CommonEplProcessorTest {
		
		private TestRunner runner;
		
		@Before public void init() {
			runner = TestRunners.newTestRunner(new CommonEplProcessor());
		}
		
		@After public void destroy() {
			runner.clearProperties();
			runner.clearProvenanceEvents();
			runner.clearTransferState();
		}
		
		@Test public void testOnTrigger() throws IOException {
			TestRunner runner = TestRunners.newTestRunner(new CommonEplProcessor());
			InputStream inEvent = new ByteArrayInputStream("{\"source.port\":23,\"destination.port\":21,\"network.iana_number\":6}".getBytes());
			runner.setProperty(CommonEplProcessor.EplStatement, "@Name('ProtocolRegisterDebugger') @Audit select * from ProtocolRegister(network.iana_number=6)");
			runner.setProperty(CommonEplProcessor.EventSchema, "create map schema ProtocolRegister as (source.port int,destination.port int,network.iana_number int)");
			runner.setProperty(CommonEplProcessor.NameOfInboundEvent, "ProtocolRegister");
			runner.enqueue(inEvent);
			runner.run(1);
			runner.assertQueueEmpty();
		}


		@Test public void testComplexEventSchemaDefinition() throws IOException {
			
			InputStream inEvent = new ByteArrayInputStream("{\"netflow\":{\"source.port\":23, \"destination.port\":21,\"network.iana_number\":6}, \"version\":1}".getBytes());
			runner.setProperty(CommonEplProcessor.EplStatement, "select * from ProtocolRegister(netflow.network.iana_number=6)");
			runner.setProperty(CommonEplProcessor.EventSchema, 
						"create map schema Netflow as (source.port int,destination.port int,network.iana_number int)"
					+ 	"|"
					+ 	"create map schema ProtocolRegister as (netflow Netflow, version int)"
					);
			runner.setProperty(CommonEplProcessor.NameOfInboundEvent, "ProtocolRegister");
			runner.enqueue(inEvent);
			runner.run(1);
			runner.assertQueueEmpty();
		}

}
