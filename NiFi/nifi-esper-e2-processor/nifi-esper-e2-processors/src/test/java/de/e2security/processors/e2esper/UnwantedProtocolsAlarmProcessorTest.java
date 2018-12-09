package de.e2security.processors.e2esper;

import java.io.IOException;
import java.io.InputStream;

import org.apache.nifi.stream.io.ByteArrayInputStream;
import org.apache.nifi.util.TestRunner;
import org.apache.nifi.util.TestRunners;
import org.junit.Test;

public class UnwantedProtocolsAlarmProcessorTest {

	@Test public void testOnTrigger() throws IOException {
		
		InputStream inEvent = new ByteArrayInputStream("{\"source.port\":23,\"destination.port\":21,\"network.iana_number\":6}".getBytes());
		
		TestRunner runner = TestRunners.newTestRunner(new UnwantedProtocolsAlarmProcessor());
		runner.setProperty(UnwantedProtocolsAlarmProcessor.CreateInputEventSchema, "create map schema ProtocolRegister as (source.port int,destination.port int,network.iana_number int)");
		runner.setProperty(UnwantedProtocolsAlarmProcessor.CreateOutputEventSchema, "create map schema UnwantedProtocol as (source.port int,destination.port int,network.iana_number int)");
		runner.setProperty(UnwantedProtocolsAlarmProcessor.FromEvent, "ProtocolRegister");
		runner.setProperty(UnwantedProtocolsAlarmProcessor.SelectAlarmsDetectedEplStatement, "select * from UnwantedProtocol");
		runner.setProperty(UnwantedProtocolsAlarmProcessor.ToEvent, "UnwantedProtocol");
		runner.setProperty(UnwantedProtocolsAlarmProcessor.UnwantedTcpPortsProperty, "21,23,80");
		runner.setProperty(UnwantedProtocolsAlarmProcessor.UnwantedUdpPortsProperty, "81");
		
		runner.enqueue(inEvent);
		
		runner.run(1);
		
		runner.assertQueueEmpty(); //assert the event has been processed successfully
		
		runner.shutdown();
	}
}
