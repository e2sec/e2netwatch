package nw104;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.nifi.util.MockFlowFile;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import com.espertech.esper.client.scopetest.SupportUpdateListener;
import com.espertech.esper.event.map.MapEventBean;
import com.google.gson.Gson;

import de.e2security.processors.e2esper.listener.EsperListener;
import de.e2security.processors.e2esper.utilities.CommonSchema;
import de.e2security.processors.e2esper.utilities.EventTransformer;
import de.e2security.processors.e2esper.utilities.SupportUtility;
import de.e2security.processors.e2esper.utilities.TransformerWithMetrics;
import junit.framework.Assert;
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

	@Test public void definitionOfEsperClassesTest() throws CloneNotSupportedException, IOException {
		//prepare complex event:
		TestEvent event1 = blueprint.clone();
		event1.setEvent_id(4720);
		EventTransformer transformer = new TransformerWithMetrics(new MockFlowFile(1));
		//define esper generic tuple for attributes
//		engine.getEPAdministrator().createEPL("create schema FlowFileAttributes()");
		String userQuerySchema = "create schema T_50005_0008_01_02 as ("
				+ "tilde_event_uuid string, "
				+ "cep_delta long, "
				+ "host_hash string, "
				+ "target_user_name string, "
				+ "target_user_name_hash string, "
				+ "event_id int, "
				+ "hostname_domain string)";
		String progQuerySchema = "create schema T_50005_0008_01_02 as ( flowFileAttributes Map, "
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
		String progQueryStmt = "@Name(T_50005_0008_01_02_filter) SELECT flowFileAttributes, '50005-0008-01' AS alert_uc_scenario, "
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
		engine.getEPAdministrator().createEPL(progQueryStmt);
		EPStatement stmt = engine.getEPAdministrator().createEPL(progQueryStmt);
		SupportUpdateListener supportL;
		stmt.addListener(supportL = new SupportUpdateListener());
		stmt.addListener(new UpdateListener() {
			@Override
			public void update(EventBean[] newEvents, EventBean[] oldEvents) {
				final EventBean event = newEvents[0];
				if (event instanceof MapEventBean) {
					final Map<?,?> eventAsMap = (Map<?,?>) event.getUnderlying();
					{
						Optional<Map<String,String>> attrEvent = Optional.ofNullable((Map<String,String>) eventAsMap.get(CommonSchema.EVENT.flowFileAttributes.toString()));
						attrEvent.ifPresent((map) -> System.out.println(map));
					}
				}
			}
		});
		engine.getEPRuntime().sendEvent(transformer.transform(gson.toJson(event1)), "T_50005_0008_01_02");
		Assert.assertTrue(supportL.getNewDataList().size() > 0);
	}
}
