package nw104;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.scopetest.SupportUpdateListener;
import com.google.gson.Gson;

import de.e2security.processors.e2esper.utilities.SupportUtility;
import junit.framework.Assert;
import net.minidev.json.JSONObject;
import nw101.EplPatternProcessingTest.TestEvent;

public class EsperBehaviourTest {
	private volatile EPServiceProvider engine;;
	private TestEvent blueprint = new TestEvent().createDefaultTestEvent();
	private Gson gson = new Gson();
	@Before public void init() {
		Configuration conf = new Configuration();
		conf.getEngineDefaults().getThreading().setInternalTimerEnabled(true);
		engine = EPServiceProviderManager.getDefaultProvider(conf);
	}
	
	@After public void after() {
		engine.destroy();
	}
	
	/*
	 * testing how to define complex (self contained objects) classes in Esper (in order to use for Attributes transfer within processor defined events) 
	 */
	
	@Ignore
	@Test public void definitionOfEsperClassesTest() throws CloneNotSupportedException, IOException {
		//prepare complex event:
		TestEvent event1 = blueprint.clone();
		event1.setEvent_id(4720);
		Map<String,String> attributes = new HashMap<String,String>(){{
			put("filepath", "testpath");
			put("uuid","testuuid");
		}};
		Map<String,Object> wrapper = new HashMap<String,Object>(){{
			put("TEST_EVENT", SupportUtility.transformEventToMap(gson.toJson(event1), Optional.empty()));
			put("TEST_ATTR", attributes);
		}};
		JSONObject event = new JSONObject(wrapper);
		//define esper generic tuple for attributes
		engine.getEPAdministrator().createEPL("create schema TEST_ATTR()");
		String userQuerySchema = "create schema T_50005_0008_01_02 as ("
				+ "tilde_event_uuid string, "
				+ "cep_delta long, "
				+ "host_hash string, "
				+ "target_user_name string, "
				+ "target_user_name_hash string, "
				+ "event_id int, "
				+ "hostname_domain string)";
		//TODO: programmatical modification: aim: to transfer TEST_ATTR() in userQuerySchema
		String progQuerySchema = "create schema T_50005_0008_01_02 as ( attr TEST_ATTR, "
				+ "tilde_event_uuid string, "
				+ "cep_delta long, "
				+ "host_hash string, "
				+ "target_user_name string, "
				+ "target_user_name_hash string, "
				+ "event_id int, "
				+ "hostname_domain string)";
		engine.getEPAdministrator().createEPL(progQuerySchema);
		String userQueryStmt = "@Name(T_50005_0008_01_02_filter) SELECT '50005-0008-01' AS alert_uc_scenario, "
				+ "'ip_hash' AS enrichment_field,"
				+ "'50005_inv_ip2ci' AS enrichment_index, "
				+ "host_hash AS enrichment_key, "
				+ "tilde_event_uuid AS alert_reference1, "
				+ "tilde_event_uuid AS alert_reference2, "
				+ "cep_delta AS cep_delta, "
				+ "host_hash AS host_hash, "
				+ "target_user_name AS target_user_name, "
				+ "target_user_name_hash AS target_user_name_hash "
				+ "FROM "
				+ "T_50005_0008_01_02(event_id=4720 or event_id=4726)";
		String progQueryStmt = "@Name(T_50005_0008_01_02_filter) SELECT '50005-0008-01' AS alert_uc_scenario, "
				+ "'ip_hash' AS enrichment_field,"
				+ "'50005_inv_ip2ci' AS enrichment_index, "
				+ "host_hash AS enrichment_key, "
				+ "tilde_event_uuid AS alert_reference1, "
				+ "tilde_event_uuid AS alert_reference2, "
				+ "cep_delta AS cep_delta, "
				+ "host_hash AS host_hash, "
				+ "target_user_name AS target_user_name, "
				+ "target_user_name_hash AS target_user_name_hash "
				+ "FROM "
				+ "T_50005_0008_01_02(event_id=4720 or event_id=4726)";
		EPStatement stmt = engine.getEPAdministrator().createEPL(progQueryStmt);
		SupportUpdateListener supportL;
		stmt.addListener(supportL = new SupportUpdateListener());
		engine.getEPRuntime().sendEvent(event.toString());
		Assert.assertTrue(supportL.getNewDataList().size() > 0);
	}
}
