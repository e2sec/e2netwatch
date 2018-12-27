package de.e2security.processors.e2esper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.nifi.processor.AbstractProcessor;
import org.apache.nifi.stream.io.ByteArrayInputStream;
import org.apache.nifi.util.MockFlowFile;
import org.junit.Test;

import de.e2security.processors.e2esper.utilities.ProcessorTestSupporter;
import junit.framework.Assert;

public class UnwantedProtocolProcessorTest extends ProcessorTestSupporter {
	
	@Override
	public AbstractProcessor initializeProcessor() {
		return new UnwantedProtocolProcessor();
	}
	
	@Test public void testOnTrigger() throws IOException {
		
		String input = "{\"source.port\":23,\"destination.port\":21,\"network.iana_number\":6}";
		InputStream inEvent = new ByteArrayInputStream(input.getBytes());
		
		runner.setProperty(CommonPropertyDescriptor.EVENT_SCHEMA, "create map schema ProtocolRegister as (source.port int,destination.port int,network.iana_number int)");
		runner.setProperty(CommonPropertyDescriptor.EPL_STATEMENT,"select * from ProtocolRegister");
		runner.setProperty(CommonPropertyDescriptor.INBOUND_EVENT_NAME, "ProtocolRegister");
		runner.setProperty(CommonPropertyDescriptor.ESPER_ENGINE,"EsperEngineService");
		runner.setProperty(UnwantedProtocolProcessor.UNWANTED_TCP_PORTS_LIST, "21,23,80");
		runner.setProperty(UnwantedProtocolProcessor.UNWANTED_UDP_PORTS_LIST, "81");
		
		runner.enqueue(inEvent);
		runner.run(1);
		runner.assertQueueEmpty(); //assert the event has been processed successfully
		
		//read result
		List<MockFlowFile> results = runner.getFlowFilesForRelationship(UnwantedProtocolProcessor.UNWANTED_TCP_CONNECTIONS);
		MockFlowFile result = results.get(0);
		String resultValue = new String(runner.getContentAsByteArray(result));
		String expectedMapFormat = "[source.port=23, destination.port=21, network.iana_number=6]";
		Assert.assertEquals(expectedMapFormat, resultValue);
	}

}
