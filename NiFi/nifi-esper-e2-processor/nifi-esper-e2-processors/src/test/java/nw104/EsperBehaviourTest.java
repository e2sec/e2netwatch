package nw104;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.nifi.processor.Relationship;
import org.apache.nifi.util.MockFlowFile;
import org.apache.nifi.util.TestRunner;
import org.apache.nifi.util.TestRunners;
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
import de.e2security.processors.e2esper.processor.EsperProducer;
import de.e2security.processors.e2esper.utilities.CommonSchema;
import de.e2security.processors.e2esper.utilities.EventTransformer;
import de.e2security.processors.e2esper.utilities.TransformerWithAttributes;
import junit.framework.Assert;
import nw101.EplPatternProcessingTest.TestEvent;

public class EsperBehaviourTest {

	private volatile EPServiceProvider engine;;
	private TestEvent blueprint = new TestEvent().createDefaultTestEvent();
	private Gson gson = new Gson();
	private volatile TestRunners runners;
	private volatile TestRunner runner;
	private EsperProducer mockProcessor;;
	@Before public void init() {
		Configuration conf = new Configuration();
		conf.getEngineDefaults().getThreading().setInternalTimerEnabled(true);
		engine = EPServiceProviderManager.getDefaultProvider(conf);
		runner = runners.newTestRunner(mockProcessor = new EsperProducer()); //just to mockup session;
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
		TestEvent event2 = event1.clone();
		event2.setTarget_user_name_hash("79957b8bf053a695e62603c1f81bb48");
		EventTransformer transformer = new TransformerWithAttributes(new MockFlowFile(1));
		//define esper generic tuple for attributes
//		engine.getEPAdministrator().createEPL("create schema FlowFileAttributes()");
		//test schemas
		String progQuerySchema;
		{
			String userQuerySchema = "create schema T_50005_0008_01_02 as ("
					+ "tilde_event_uuid string, "
					+ "cep_delta long, "
					+ "host_hash string, "
					+ "target_user_name string, "
					+ "target_user_name_hash string, "
					+ "event_id int, "
					+ "hostname_domain string)";
			progQuerySchema = "create schema T_50005_0008_01_02 as ( flowFileAttributes Map, "
					+ "tilde_event_uuid string, "
					+ "cep_delta long, "
					+ "host_hash string, "
					+ "target_user_name string, "
					+ "target_user_name_hash string, "
					+ "event_id int, "
					+ "hostname_domain string)";
		}
		engine.getEPAdministrator().createEPL(progQuerySchema);
		//test statements
		String progQueryStmt;
		{
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
			progQueryStmt = "@Name(T_50005_0008_01_02_filter) SELECT flowFileAttributes, '50005-0008-01' AS alert_uc_scenario, "
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
			//get flow files attributes from the first incoming event
			progQueryStmt = "@Name(T_50005_0008_01_02_detections) SELECT a.flowFileAttributes as flowFileAttributes, '50005-0008-01' AS alert_uc_scenario, "
					+ "'ip_hash' AS enrichment_field,'50005_inv_ip2ci' AS enrichment_index, a.host_hash AS enrichment_key,"
					+ "a.tilde_event_uuid AS alert_reference1, b.tilde_event_uuid AS alert_reference2, "
					+ "a.cep_delta AS cep_delta, a.host_hash AS host_hash, a.target_user_name AS target_user_name, "
					+ "a.target_user_name_hash AS target_user_name_hash "
					+ "FROM PATTERN "
					+ "[ every-distinct(target_user_name_hash, 86401 sec) a=T_50005_0008_01_02(event_id=4720) "
					+ "-> b=T_50005_0008_01_02(event_id=4720 "
					+ "AND b.target_user_name_hash = a.target_user_name_hash AND b.hostname_domain = a.hostname_domain) "
					+ "WHERE timer:within(86400 sec) ]";

		}
		engine.getEPAdministrator().createEPL(progQueryStmt);
		EPStatement stmt = engine.getEPAdministrator().createEPL(progQueryStmt);
		SupportUpdateListener supportL;
		stmt.addListener(supportL = new SupportUpdateListener());
		final AtomicReference<Map> flowFileAttributes = new AtomicReference();
		MockFlowFile file = new MockFlowFile(1);
		EsperListener successListener = new EsperListener(new MockLogger(), mockProcessor.SUCCEEDED_REL);
		successListener.setSession(runner.getProcessSessionFactory());
		stmt.addListener(successListener);
		engine.getEPRuntime().sendEvent(transformer.transform(gson.toJson(event1)), "T_50005_0008_01_02");
		engine.getEPRuntime().sendEvent(transformer.transform(gson.toJson(event2)), "T_50005_0008_01_02");
		Assert.assertTrue(supportL.getNewDataList().size() > 0);
	}
	
			
}
