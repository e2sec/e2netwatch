package de.e2security.processors.e2esper;

import java.io.IOException;
import java.io.InputStream;

import org.apache.nifi.processor.AbstractProcessor;
import org.apache.nifi.stream.io.ByteArrayInputStream;
import org.apache.nifi.util.MockFlowFile;
import org.junit.Ignore;
import org.junit.Test;

import de.e2security.processors.e2esper.utilities.CommonPropertyDescriptor;
import de.e2security.processors.e2esper.utilities.ProcessorTestSupporter;

@SuppressWarnings("deprecation")
@Ignore
public class CommonEplProcessorTest extends ProcessorTestSupporter {
	
		@Override
		public AbstractProcessor initializeProcessor() {
			return new CommonEplProcessor();
		}
		
		@Test public void eventAsMapHasBeenTransformedToJsonFormat() throws IOException {
			String json = "{\"source.port\":23,\"destination.port\":21,\"network.iana_number\":6}";
			InputStream inEvent = new ByteArrayInputStream(json.getBytes());
			runner.setProperty(CommonPropertyDescriptor.EPL_STATEMENT, "@Name('ProtocolRegisterDebugger') @Audit select * from ProtocolRegister(network.iana_number=6)");
			runner.setProperty(CommonPropertyDescriptor.EVENT_SCHEMA, "create map schema ProtocolRegister as (source.port int,destination.port int,network.iana_number int)");
			runner.setProperty(CommonPropertyDescriptor.ESPER_ENGINE,"EsperEngineService");
			runner.enqueue(inEvent);
			runner.run(1);
			MockFlowFile file = runner.getFlowFilesForRelationship(CommonEplProcessor.SUCCEEDED_EVENT).get(0);
			file.assertContentEquals(json);
		}
		
		@Test public void testEventHasBeenProcessedAndTransferedToSucceededRelationship() throws IOException {
			InputStream inEvent = new ByteArrayInputStream("{\"source.port\":23,\"destination.port\":21,\"network.iana_number\":6}".getBytes());
			runner.setProperty(CommonPropertyDescriptor.EPL_STATEMENT, "@Name('ProtocolRegisterDebugger') @Audit select * from ProtocolRegister(network.iana_number=6)");
			runner.setProperty(CommonPropertyDescriptor.EVENT_SCHEMA, "create map schema ProtocolRegister as (source.port int,destination.port int,network.iana_number int)");
			runner.setProperty(CommonPropertyDescriptor.ESPER_ENGINE,"EsperEngineService");
			runner.enqueue(inEvent);
			runner.run(1);
			runner.assertQueueEmpty();
			runner.assertAllFlowFilesTransferred(CommonEplProcessor.SUCCEEDED_EVENT);
		}
		
		@Test public void testEventHasBeenUnmatchedAndTransferedToUnmatchedRelationship() throws IOException {
			InputStream inEvent = new ByteArrayInputStream("{\"source.port\":23,\"destination.port\":21,\"network.iana_number\":6}".getBytes());
			runner.setProperty(CommonPropertyDescriptor.EPL_STATEMENT, "@Name('ProtocolRegisterDebugger') @Audit select * from ProtocolRegister(network.iana_number=17)");
			runner.setProperty(CommonPropertyDescriptor.EVENT_SCHEMA, "create map schema ProtocolRegister as (source.port int,destination.port int,network.iana_number int)");
			runner.setProperty(CommonPropertyDescriptor.ESPER_ENGINE,"EsperEngineService");
			runner.enqueue(inEvent);
			runner.run(1);
			runner.assertQueueEmpty();
			runner.assertAllFlowFilesTransferred(CommonEplProcessor.UNMATCHED_EVENT);
		}
		
		@Test public void testComplexEventSchemaDefinition() throws IOException {
			InputStream inEvent = new ByteArrayInputStream("{\"netflow\":{\"source.port\":23, \"destination.port\":21,\"network.iana_number\":6}, \"version\":1}".getBytes());
			runner.setProperty(CommonPropertyDescriptor.EPL_STATEMENT, "select * from ProtocolRegister(netflow.network.iana_number=6)");
			runner.setProperty(CommonPropertyDescriptor.EVENT_SCHEMA, 
						"create map schema Netflow as (source.port int,destination.port int,network.iana_number int)"
					+ 	"|"
					+ 	"create map schema ProtocolRegister as (netflow Netflow, version int)"
					);
			runner.setProperty(CommonPropertyDescriptor.ESPER_ENGINE, "EsperEngineService");
			runner.enqueue(inEvent);
			runner.run(1);
			runner.assertQueueEmpty();
		}
}
