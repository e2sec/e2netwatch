package de.e2security.processors.e2esper;

import java.io.IOException;
import java.io.InputStream;

import org.apache.nifi.controller.ControllerService;
import org.apache.nifi.reporting.InitializationException;
import org.apache.nifi.stream.io.ByteArrayInputStream;
import org.apache.nifi.util.TestRunner;
import org.apache.nifi.util.TestRunners;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.e2security.nifi.controller.esper.EsperEngineService;

public class CommonEplProcessorTest {
		
		private TestRunner runner;
		private ControllerService controller;
		
		@Before public void init() {
			controller = new EsperEngineService();
			runner = TestRunners.newTestRunner(new CommonEplProcessor());
			//adding controller 
			try {
				runner.addControllerService("EsperEngineService", controller);
			} catch (InitializationException e) {
				e.printStackTrace();
			}
			runner.enableControllerService(controller);
		}
		
		@After public void destroy() throws InterruptedException {
			runner.clearProperties();
			runner.clearProvenanceEvents();
			runner.clearTransferState();
			((EsperEngineService) controller).shutdown();
			runner.disableControllerService(controller);
			runner.shutdown();
		}
		
		@Test public void testOnTrigger() throws IOException {
			InputStream inEvent = new ByteArrayInputStream("{\"source.port\":23,\"destination.port\":21,\"network.iana_number\":6}".getBytes());
			runner.setProperty(CommonEplProcessor.EPL_STATEMENT, "@Name('ProtocolRegisterDebugger') @Audit select * from ProtocolRegister(network.iana_number=6)");
			runner.setProperty(CommonEplProcessor.EVENT_SCHEMA, "create map schema ProtocolRegister as (source.port int,destination.port int,network.iana_number int)");
			runner.setProperty(CommonEplProcessor.INBOUND_EVENT_NAME, "ProtocolRegister");
			runner.setProperty(CommonEplProcessor.ESPER_ENGINE,"EsperEngineService");
			runner.enqueue(inEvent);
			runner.run(1);
			runner.assertQueueEmpty();
		}


		@Test public void testComplexEventSchemaDefinition() throws IOException {
			InputStream inEvent = new ByteArrayInputStream("{\"netflow\":{\"source.port\":23, \"destination.port\":21,\"network.iana_number\":6}, \"version\":1}".getBytes());
			runner.setProperty(CommonEplProcessor.EPL_STATEMENT, "select * from ProtocolRegister(netflow.network.iana_number=6)");
			runner.setProperty(CommonEplProcessor.EVENT_SCHEMA, 
						"create map schema Netflow as (source.port int,destination.port int,network.iana_number int)"
					+ 	"|"
					+ 	"create map schema ProtocolRegister as (netflow Netflow, version int)"
					);
			runner.setProperty(CommonEplProcessor.INBOUND_EVENT_NAME, "ProtocolRegister");
			runner.setProperty(CommonEplProcessor.ESPER_ENGINE, "EsperEngineService");
			runner.enqueue(inEvent);
			runner.run(1);
			runner.assertQueueEmpty();
		}

}
