package nw104;

import java.util.HashMap;
import java.util.Map;

import org.apache.nifi.controller.ControllerService;
import org.apache.nifi.registry.VariableDescriptor;
import org.apache.nifi.registry.VariableRegistry;
import org.apache.nifi.reporting.InitializationException;
import org.apache.nifi.util.TestRunner;
import org.apache.nifi.util.TestRunners;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

import de.e2security.nifi.controller.esper.EsperEngineService;
import de.e2security.processors.e2esper.processor.EsperConsumer;
import de.e2security.processors.e2esper.utilities.CommonPropertyDescriptor;
import nw101.EplPatternProcessingTest.TestEvent;

public class TestExpressionLanguageValueForPropertyDescriptor {
	TestRunners runners = new TestRunners();
	ControllerService controller;
	TestRunner runner;
	
	@Before public void init() throws InitializationException {
		runner = runners.newTestRunner(new EsperConsumer());
		controller = new EsperEngineService();
		runner.addControllerService("EsperEngineService", controller);
		runner.enableControllerService(controller);
	}
	
	@After public void destroy() {
		runner.clearProperties();
		runner.clearProvenanceEvents();
		runner.clearTransferState();
		runner.shutdown();
	}
	
	final String event_schema = "@Name(TEST_schema) CREATE SCHEMA T_50005_0008_01_02 AS ("
			+ "tilde_event_uuid string, "
			+ "cep_delta long, "
			+ "host_hash string, "
			+ "target_user_name string, "
			+ "target_user_name_hash string, "
			+ "event_id int, "
			+ "hostname_domain string)";
	final String stmt = "@Name(TEST) SELECT '50005-0008-01' AS alert_uc_scenario, "
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
			+ "T_50005_0008_01_02(event_id=4720 OR event_id=4726)";
	
	VariableRegistry vr = new VariableRegistry() {
		@Override
		public Map<VariableDescriptor, String> getVariableMap() {
			final Map<VariableDescriptor,String> attributes = new HashMap<>();
			attributes.put(new VariableDescriptor("test.schema"), event_schema);
			attributes.put(new VariableDescriptor("test.stmt"), stmt);
			return attributes; 
		}
	};
	
	@Test public void testEsperProcessorWithExpressionLanguageEnabledButNotProvidedCanBeInitializedProperly() throws CloneNotSupportedException {
		runner.setProperty(CommonPropertyDescriptor.EVENT_SCHEMA, event_schema);
		runner.setProperty(CommonPropertyDescriptor.EPL_STATEMENT, stmt);
		runner.setProperty(CommonPropertyDescriptor.ESPER_ENGINE, "EsperEngineService");
		TestEvent event0 = new TestEvent().createDefaultTestEvent();
		runner.enqueue(new Gson().toJson(event0).getBytes());
		runner.run(1);
	}
	
	
	@Test public void testEsperProcessorWithExpressionLanguageProvidedCanBeInitializedProperly() throws CloneNotSupportedException {
		
		runner.setVariable("test.schema", vr.getVariableValue("test.schema"));
		runner.setVariable("test.stmt", vr.getVariableValue("test.stmt"));
		
		runner.setProperty(CommonPropertyDescriptor.EVENT_SCHEMA, "${test.schema}");
		runner.setProperty(CommonPropertyDescriptor.EPL_STATEMENT, "${test.stmt}");
		runner.setProperty(CommonPropertyDescriptor.ESPER_ENGINE, "EsperEngineService");
		TestEvent event0 = new TestEvent().createDefaultTestEvent();
		runner.enqueue(new Gson().toJson(event0).getBytes());
		runner.run(1);
	}
}
