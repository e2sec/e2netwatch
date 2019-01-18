package nw104;

import org.apache.nifi.processor.AbstractProcessor;
import org.apache.nifi.reporting.InitializationException;
import org.apache.nifi.util.TestRunner;
import org.junit.Test;

import com.google.gson.Gson;

import de.e2security.nifi.controller.esper.EsperEngineService;
import de.e2security.processors.e2esper.processor.EsperConsumer;
import de.e2security.processors.e2esper.processor.EsperProducer;
import de.e2security.processors.e2esper.utilities.CommonPropertyDescriptor;
import de.e2security.processors.e2esper.utilities.ProcessorTestSupporter;
import nw101.EplPatternProcessingTest.TestEvent;

@SuppressWarnings("static-access")
public class EsperConsumerProcessorTest extends ProcessorTestSupporter {

	@Override
	public AbstractProcessor initializeProcessor() {
		return new EsperConsumer();
	}
	
	Gson gson = new Gson();
	TestEvent event0 = new TestEvent().createDefaultTestEvent();
	
	@Test public void generaEsperConsumerRunlTest() throws CloneNotSupportedException, InitializationException {
		
		runner.setProperty(CommonPropertyDescriptor.EPL_STATEMENT, "@Name(TEST) SELECT '50005-0008-01' AS alert_uc_scenario, "
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
		runner.setProperty(CommonPropertyDescriptor.EVENT_SCHEMA, "create schema T_50005_0008_01_02 as ("
				+ "tilde_event_uuid string, "
				+ "cep_delta long, "
				+ "host_hash string, "
				+ "target_user_name string, "
				+ "target_user_name_hash string, "
				+ "event_id int, "
				+ "hostname_domain string)");
		runner.setProperty(CommonPropertyDescriptor.ESPER_ENGINE, "EsperEngineService");
		
		TestEvent event1 = event0.clone();
		event1.setEvent_id(4720);
		event1.setTarget_user_name_hash("79957b8bf053a695e62603c1f81bb49");
		TestEvent event2 = event0.clone();
		event2.setEvent_id(4726);
		event2.setTarget_user_name_hash("79957b8bf053a695e62603c1f81bb49");
		
		//set current time due to external timer
		/* TODO: implement also other types of events; 
		 * implementation in test is difficult. EsperConsumer awaits JSON format
		 * For Test Purpose EsperEngineController ExternalTimer should be enabled;
		 */
//		runner.enqueue(new CurrentTimeEvent(System.currentTimeMillis()).toString().getBytes());
//		runner.run(1);

		runner.enqueue(gson.toJson(event1).getBytes());
		runner.run(1); 
		
		//initialize monitor
		TestRunner runnerMonitor = runners.newTestRunner(new EsperProducer());
		runnerMonitor.addControllerService("EsperEngineService", controller);
		runnerMonitor.setProperty(controller, EsperEngineService.ENABLE_STATEMENT_METRIC, "true");
		runnerMonitor.setProperty(EsperProducer.ESPER_ENGINE, "EsperEngineService");
		runnerMonitor.setProperty(EsperProducer.EPSTMT_NAME, "StmtMetric");
		runnerMonitor.enableControllerService(controller);
		runnerMonitor.run(1);
		
		runner.enqueue(gson.toJson(event2).getBytes());
		runner.run(1);
		
		runnerMonitor.run();
//		MockFlowFile metricOutput = runnerMonitor.getFlowFilesForRelationship(EsperProducer.SUCCEEDED_REL).get(0);
		
	}
	
}
