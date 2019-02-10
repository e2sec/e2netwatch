package nw111;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.nifi.util.MockFlowFile;
import org.junit.Test;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPException;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import nw101.EplPatternProcessingTest.TestEvent;

public class EsperEngineTest {

	private Gson gson = new Gson();
	
	private EPServiceProvider engine = new EPServiceProviderManager().getDefaultProvider();
	private EPAdministrator admin = engine.getEPAdministrator();
	private EPRuntime runtime = engine.getEPRuntime();

	@Test public void testingPreparedStatementsAsWorkAroundForABetterFlowFileAttributeTransfer() throws EPException, IOException {
		admin.createEPL("create schema NiFiEsperEvent()");
		admin.createEPL("create schema NiFiUserEvent as (foo string, bar string) inherits NiFiEsperEvent");
		admin.createEPL("create schema FlowFileAttributes() inherits NiFiEsperEvent");
		TestEvent event0 = new TestEvent().createDefaultTestEvent();
		final Map<String,Object> eventAsMap = new HashMap<>();
		{
			final Map<String,String> ffAttributesAsMap = new MockFlowFile(1).getAttributes(); 
			final ObjectMapper mapper = new ObjectMapper();
			eventAsMap.put("NiFiUserEvent", mapper.readValue(gson.toJson(event0), new TypeReference<Map<String,Object>>(){}));
			eventAsMap.put("FlowFileAttributes", ffAttributesAsMap);
		}
		//modify http://esper.espertech.com/release-7.1.0/esper-reference/html/epl_clauses.html#epl-containedeventselect-select
		EPStatement stmt = admin.createEPL("select * from NiFiEsperEvent"); 
		stmt.addListener((newEvents, oldEvents) -> {
			Map<?,?> map = (Map) newEvents[0].getUnderlying();
			System.out.println(map.toString());
		});
		runtime.sendEvent(eventAsMap, "NiFiEsperEvent");
	}
	
	@Test public void transformArrayFromJsonToMapResultTest() throws JsonParseException, JsonMappingException, IOException {
		final Map<String,Object> eventAsMap = new HashMap<>();
		final ObjectMapper mapper = new ObjectMapper();
		String eventAsJson = "{\"event_id\":\"bar\", \"zzz_inventory\":[\"hostname.ci_name=foo\",\"hostname.vland_desc=eden\"]}";
		eventAsMap.putAll(mapper.readValue(eventAsJson, new TypeReference<Map<String,Object>>(){}));
		eventAsMap.entrySet().forEach(entry -> {
			System.out.println(entry.getValue().getClass());
		});
		admin.createEPL("create map schema ARREVENT as (foo string, zzz_inventory String[])");
	}
	
}
