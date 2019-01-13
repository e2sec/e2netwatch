package nw102;

import java.time.Instant;
import java.util.List;

import org.apache.nifi.processor.AbstractProcessor;
import org.apache.nifi.util.MockFlowFile;
import org.junit.Test;

import com.google.gson.Gson;

import de.e2security.processors.e2esper.CommonEplProcessor;
import de.e2security.processors.e2esper.utilities.CommonPropertyDescriptor;
import de.e2security.processors.e2esper.utilities.ProcessorTestSupporter;
import nw101.EplPatternProcessingTest.TestEvent;

public class TestCustomTimerPatternObserver extends ProcessorTestSupporter {

	@Override
	public AbstractProcessor initializeProcessor() {
		return new CommonEplProcessor();
	}

	@Test public void testTimerInternalPatternObserver() throws CloneNotSupportedException {
		runner.setProperty(CommonPropertyDescriptor.EPL_STATEMENT, "SELECT '50005-0008-01' AS alert_uc_scenario, "
				+ "'ip_hash' AS enrichment_field,"
				+ "'50005_inv_ip2ci' AS enrichment_index, "
				+ "a.host_hash AS enrichment_key, "
				+ "a.tilde_event_uuid AS alert_reference1, "
				+ "b.tilde_event_uuid AS alert_reference2, "
				+ "a.cep_delta AS cep_delta, "
				+ "a.host_hash AS host_hash, "
				+ "a.target_user_name AS target_user_name, "
				+ "a.target_user_name_hash AS target_user_name_hash "
				+ "FROM PATTERN [ "
				+ "every-distinct(target_user_name_hash, 86401 sec) "
				+ "a=T_50005_0008_01_02(event_id=4720) -> "
				+ "b=T_50005_0008_01_02("
				+ "event_id=4726 "
				+ "AND "
				+ "b.target_user_name_hash = a.target_user_name_hash "
				+ "AND "
				+ "b.hostname_domain = a.hostname_domain"
				+ ") -> timer:event(5, \"epoch\") " 
				+ "WHERE "
				+ "timer:within(86400 sec) "
				+ "]");
		runner.setProperty(CommonPropertyDescriptor.EVENT_SCHEMA, "create schema T_50005_0008_01_02 as ("
				+ "tilde_event_uuid string, "
				+ "cep_delta long, "
				+ "host_hash string, "
				+ "target_user_name string, "
				+ "target_user_name_hash string, "
				+ "event_id int, "
				+ "hostname_domain string)");
		runner.setProperty(CommonPropertyDescriptor.INBOUND_EVENT_NAME, "T_50005_0008_01_02");
		runner.setProperty(CommonPropertyDescriptor.ESPER_ENGINE, "EsperEngineService");
		
		Gson gson = new Gson();
		TestEvent event0 = new TestEvent().createDefaultTestEvent();
		
		TestEvent event1 = event0.clone();
		event1.setEvent_id(4720);
		event1.setEpoch(Instant.now().toEpochMilli() - 5);
		TestEvent event2 = event0.clone();
		event2.setEvent_id(4726);
		event2.setEpoch(Instant.now().toEpochMilli());
		runner.enqueue(gson.toJson(event1).getBytes());
		runner.enqueue(gson.toJson(event2).getBytes());
		
		runner.run(2);
		List<MockFlowFile> unmatched = runner.getFlowFilesForRelationship(CommonEplProcessor.SUCCEEDED_EVENT);
		unmatched.get(0).assertContentEquals("{\"alert_reference1\":\"UC0001-03-TC01-TRIGGER-2019-0103-112501-0007\",\"alert_uc_scenario\":\"50005-0008-01\",\"alert_reference2\":\"UC0001-03-TC01-TRIGGER-2019-0103-112501-0007\",\"cep_delta\":1800,\"target_user_name_hash\":\"79957b8bf053a695e62603c1f81bb48\",\"enrichment_index\":\"50005_inv_ip2ci\",\"enrichment_key\":\"256785f9d61e3b6abcaa9168574e9f09\",\"target_user_name\":\"e######adm\",\"enrichment_field\":\"ip_hash\",\"host_hash\":\"256785f9d61e3b6abcaa9168574e9f09\"}");
	}
}
