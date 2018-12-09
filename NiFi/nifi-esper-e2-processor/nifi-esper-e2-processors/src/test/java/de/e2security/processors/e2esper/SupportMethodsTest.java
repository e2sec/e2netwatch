package de.e2security.processors.e2esper;

import org.junit.Test;

import junit.framework.Assert;

public class SupportMethodsTest {

	private UnwantedProtocolsAlarmProcessor processor = new UnwantedProtocolsAlarmProcessor();
	
	@Test public void testPortsConcatenation() {
		String actual = processor.concatenator("tcp", "21,23,80");
		//(network.iana_number[=6 and  ( source.port=21or destination.port=21 or source.port=23or destination.port=23 or source.port=80or destination.port=80 or ])
		String expected = "(network.iana_number=6 and ("
				+ "source.port=21 or destination.port=21"
				+ " or source.port=23 or destination.port=23"
				+ " or source.port=80 or destination.port=80))";
		Assert.assertEquals(expected, actual);;
	}

	@Test public void testUdpAndTcpConcatenation() {
		String actual = processor.concatenatePorts("21,23, 80", "88, 90");
		String awaited = "(network.iana_number=6 and ("
				+ "source.port=21 or destination.port=21"
				+ " or source.port=23 or destination.port=23"
				+ " or source.port=80 or destination.port=80))"
				+ " or (network.iana_nuber=17 and ("
				+ "source.port=88 or destination.port=88"
				+ " or source.port=90 or destination.port=90))";
	}
	
	@Test public void testMulitpleEventSchemaDividedByPipeline() {
		String testSchemaDeclaration = "create map schema Netflow as (source.port int,destination.port int,network.iana_number int)"
				+ 	"|"
				+ 	"create map schema ProtocolRegister as (netflow Netflow, version int)";
		String[] eventSchemas = testSchemaDeclaration.split("\\|");
		String expected = "create map schema Netflow as (source.port int,destination.port int,network.iana_number int)";
		String expected2 = "create map schema ProtocolRegister as (netflow Netflow, version int)";
		Assert.assertEquals(expected, eventSchemas[0]);
		Assert.assertEquals(expected2, eventSchemas[1]);
	}
}
