package de.e2security.processors.e2esper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.nifi.stream.io.ByteArrayInputStream;
import org.apache.nifi.util.MockFlowFile;
import org.apache.nifi.util.TestRunner;
import org.apache.nifi.util.TestRunners;
import org.junit.Test;

import junit.framework.Assert;

public class UnwantedProtocolsAlarmProcessorTest {

	@Test public void testOnTrigger() throws IOException {
		
		String input = "{\"source.port\":23,\"destination.port\":21,\"network.iana_number\":6}";
		InputStream inEvent = new ByteArrayInputStream(input.getBytes());
		
		TestRunner runner = TestRunners.newTestRunner(new UnwantedProtocolsAlarmProcessor());
		runner.setProperty(UnwantedProtocolsAlarmProcessor.InputEventSchema, "create map schema ProtocolRegister as (source.port int,destination.port int,network.iana_number int)");
		runner.setProperty(UnwantedProtocolsAlarmProcessor.EplStatement,"select * from ProtocolRegister");
		runner.setProperty(UnwantedProtocolsAlarmProcessor.EventName, "ProtocolRegister");
		runner.setProperty(UnwantedProtocolsAlarmProcessor.UnwantedTcpPorts, "21,23,80");
		runner.setProperty(UnwantedProtocolsAlarmProcessor.UnwantedUdpPorts, "81");
		
		runner.enqueue(inEvent);
		
		runner.run(1);
		
		runner.assertQueueEmpty(); //assert the event has been processed successfully
		
		//read result
		List<MockFlowFile> results = runner.getFlowFilesForRelationship(UnwantedProtocolsAlarmProcessor.AlarmedEvent);
		MockFlowFile result = results.get(0);
		String resultValue = new String(runner.getContentAsByteArray(result));
		String expectedMapFormat = "[source.port=23, destination.port=21, network.iana_number=6]";
		Assert.assertEquals(expectedMapFormat, resultValue);
		runner.shutdown();
	}
}
