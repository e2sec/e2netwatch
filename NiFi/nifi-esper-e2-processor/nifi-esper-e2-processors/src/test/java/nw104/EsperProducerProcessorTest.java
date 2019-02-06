package nw104;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.nifi.controller.ControllerService;
import org.apache.nifi.reporting.InitializationException;
import org.apache.nifi.util.MockFlowFile;
import org.apache.nifi.util.TestRunner;
import org.apache.nifi.util.TestRunners;
import org.junit.After;
import org.junit.Test;

import com.google.gson.Gson;

import de.e2security.nifi.controller.esper.EsperEngineService;
import de.e2security.processors.e2esper.processor.EsperConsumer;
import de.e2security.processors.e2esper.processor.EsperProducer;
import de.e2security.processors.e2esper.utilities.CommonPropertyDescriptor;
import nw101.EplPatternProcessingTest.TestEvent;

@SuppressWarnings("static-access")
public class EsperProducerProcessorTest {
	
	TestRunners runners = new TestRunners();
	ControllerService controller;
	TestRunner runnerConsumer;
	TestRunner runnerProducer;
	List<TestRunner> testRunners;

	@After public void destroy() throws InterruptedException {
		testRunners.forEach(runner -> {
			runner.setValidateExpressionUsage(false);
			runner.clearProperties();
			runner.clearProvenanceEvents();
			runner.clearTransferState();
			runner.shutdown();
		});
		((EsperEngineService) controller).shutdown();
	}
	
	Gson gson = new Gson();
	TestEvent event0 = new TestEvent().createDefaultTestEvent();
	
	@Test public void generaEsperProducerRunTest() throws CloneNotSupportedException {
			runnerConsumer = runners.newTestRunner(new EsperConsumer());
			runnerConsumer.setValidateExpressionUsage(false);
			runnerProducer = runners.newTestRunner(new EsperProducer());
			testRunners = new ArrayList<>();
			testRunners.add(runnerConsumer);
			testRunners.add(runnerProducer);
			controller = new EsperEngineService();
			testRunners.forEach(runner -> {
				try {
					runner.addControllerService("EsperEngineService", controller);
					runner.setValidateExpressionUsage(false);
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
		runnerConsumer.setProperty(CommonPropertyDescriptor.EVENT_SCHEMA, "@Name(TEST_schema) create schema T_50005_0008_01_02 as ("
				+ "tilde_event_uuid string, "
				+ "cep_delta long, "
				+ "host_hash string, "
				+ "target_user_name string, "
				+ "target_user_name_hash string, "
				+ "event_id int, "
				+ "hostname_domain string)");
		runnerConsumer.setProperty(CommonPropertyDescriptor.ESPER_ENGINE, "EsperEngineService");

		runnerProducer.setProperty(EsperProducer.ESPER_ENGINE, "EsperEngineService");
		runnerProducer.setProperty(EsperProducer.EPSTMT_NAME, "TEST");
		
		runnerProducer.enableControllerService(controller);
		
		TestEvent event1 = event0.clone();
		event1.setEvent_id(4720);
		event1.setTarget_user_name_hash("79957b8bf053a695e62603c1f81bb49");
		TestEvent event2 = event0.clone();
		event2.setEvent_id(4726);
		event2.setTarget_user_name_hash("79957b8bf053a695e62603c1f81bb50");
		runnerConsumer.enqueue(gson.toJson(event1).getBytes());
		runnerConsumer.run(1);
		
		//setting intiial listener -> first event has been lost
		runnerProducer.run(1);
		List<MockFlowFile> succ = runnerProducer.getFlowFilesForRelationship(EsperProducer.SUCCEEDED_REL);
		assertTrue(succ.isEmpty());
		
		runnerConsumer.enqueue(gson.toJson(event2).getBytes());
		runnerConsumer.run(1);
		
		runnerProducer.assertAllFlowFilesTransferred(EsperProducer.SUCCEEDED_REL);
		
		runnerConsumer.enqueue(gson.toJson(event2).getBytes());
		runnerConsumer.run(1);
		runnerProducer.assertAllFlowFilesTransferred(EsperProducer.SUCCEEDED_REL);	
	}

}
