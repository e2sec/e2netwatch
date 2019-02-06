package de.e2security.processors.e2esper.utilities;

import static de.e2security.processors.e2esper.utilities.UnwantedProtocolProcessorHelper.concatenatePorts;
import static de.e2security.processors.e2esper.utilities.UnwantedProtocolProcessorHelper.concatenator;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import junit.framework.Assert;

public class SupportMethodsTest {
	
	@Test public void retriveClassNameTest() {
		String eventName = SupportUtility.retrieveClassNameFromSchemaEPS("create schema T_50005_0008_01_02 as (\"\n" + 
				"				+ \"tilde_event_uuid string, \"\n" + 
				"				+ \"cep_delta long, \"\n" + 
				"				+ \"host_hash string, \"\n" + 
				"				+ \"target_user_name string, \"\n" + 
				"				+ \"target_user_name_hash string, \"\n" + 
				"				+ \"event_id int, \"\n" + 
				"				+ \"hostname_domain string)");
		Assert.assertEquals("T_50005_0008_01_02", eventName);
	}

	@Test public void testPortsConcatenation() {
		String actual = concatenator("tcp", "21,23,80");
		//(network.iana_number[=6 and  ( source.port=21or destination.port=21 or source.port=23or destination.port=23 or source.port=80or destination.port=80 or ])
		String expected = "(network.iana_number=6 and ("
				+ "source.port=21 or destination.port=21"
				+ " or source.port=23 or destination.port=23"
				+ " or source.port=80 or destination.port=80))";
		Assert.assertEquals(expected, actual);;
	}

	@Test public void testUdpAndTcpConcatenation() {
		String actual = concatenatePorts("21,23, 80", "88, 90");
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
