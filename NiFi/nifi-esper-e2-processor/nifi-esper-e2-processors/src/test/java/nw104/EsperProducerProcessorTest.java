package nw104;

import java.util.ArrayList;
import java.util.List;

import org.apache.nifi.controller.ControllerService;
import org.apache.nifi.reporting.InitializationException;
import org.apache.nifi.util.MockFlowFile;
import org.apache.nifi.util.TestRunner;
import org.apache.nifi.util.TestRunners;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

import de.e2security.nifi.controller.esper.EsperEngineService;
import de.e2security.processors.e2esper.CommonEplProcessor;
import de.e2security.processors.e2esper.CommonPropertyDescriptor;
import de.e2security.processors.e2esper.processor.EsperConsumer;
import de.e2security.processors.e2esper.processor.EsperProducer;
import nw101.EplPatternProcessingTest.TestEvent;

public class EsperProducerProcessorTest {
	
	TestRunners runners = new TestRunners();
	ControllerService controller;
	TestRunner runnerConsumer;
	TestRunner runnerProducer;
	List<TestRunner> testRunners;

	@After public void destroy() throws InterruptedException {
		testRunners.forEach(runner -> {
			runner.clearProperties();
			runner.clearProvenanceEvents();
			runner.clearTransferState();
			((EsperEngineService) controller).shutdown();
			runner.disableControllerService(controller);
			runner.shutdown();
		});
	}
	
	Gson gson = new Gson();
	TestEvent event0 = new TestEvent().createDefaultTestEvent();
	
	@Test public void generaEsperProducerRunlTest() throws CloneNotSupportedException {
		
			runnerConsumer = runners.newTestRunner(new EsperConsumer());
			runnerProducer = runners.newTestRunner(new EsperProducer());
			testRunners = new ArrayList<>();
			testRunners.add(runnerConsumer);
			testRunners.add(runnerProducer);
			controller = new EsperEngineService();
			testRunners.forEach(runner -> {
				try {
					runner.addControllerService("EsperEngineService", controller);
				} catch (InitializationException e) {
					e.printStackTrace();
				}
			});

		runnerConsumer.enableControllerService(controller);
		runnerConsumer.setProperty(CommonPropertyDescriptor.EPL_STATEMENT, "@Name(TEST) SELECT '50005-0008-01' AS alert_uc_scenario, "
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
				+ "T_50005_0008_01_02(event_id=4720 or event_id=4726)");
		runnerConsumer.setProperty(CommonPropertyDescriptor.EVENT_SCHEMA, "create schema T_50005_0008_01_02 as ("
				+ "tilde_event_uuid string, "
				+ "cep_delta long, "
				+ "host_hash string, "
				+ "target_user_name string, "
				+ "target_user_name_hash string, "
				+ "event_id int, "
				+ "hostname_domain string)");
		runnerConsumer.setProperty(CommonPropertyDescriptor.INBOUND_EVENT_NAME, "T_50005_0008_01_02");
		runnerConsumer.setProperty(CommonPropertyDescriptor.ESPER_ENGINE, "EsperEngineService");

		runnerProducer.setProperty(EsperProducer.ESPER_ENGINE, "EsperEngineService");
		runnerProducer.setProperty(EsperProducer.EPSTMT_NAME, "TEST");
		
		runnerProducer.enableControllerService(controller);
		
		TestEvent event1 = event0.clone();
		event1.setEvent_id(4720);
		event1.setTarget_user_name_hash("79957b8bf053a695e62603c1f81bb49");
		TestEvent event2 = event0.clone();
		event2.setEvent_id(4726);
		event2.setTarget_user_name_hash("79957b8bf053a695e62603c1f81bb49");
		
		runnerConsumer.enqueue(gson.toJson(event1).getBytes());
		
		runnerConsumer.run(1);
		runnerProducer.run(1);
		
		runnerConsumer.enqueue(gson.toJson(event2).getBytes());
		runnerConsumer.run(1);
		
		MockFlowFile succ = runnerProducer.getFlowFilesForRelationship(EsperProducer.SUCCEEDED_REL).get(0);
		runnerProducer.assertAllFlowFilesContainAttribute("json");
	}

}
