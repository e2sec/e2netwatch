package nw101;

import java.util.List;

import org.apache.nifi.processor.AbstractProcessor;
import org.apache.nifi.util.MockFlowFile;
import org.junit.Ignore;
import org.junit.Test;

import com.google.gson.Gson;

import de.e2security.processors.e2esper.CommonEplProcessor;
import de.e2security.processors.e2esper.utilities.CommonPropertyDescriptor;
import de.e2security.processors.e2esper.utilities.ProcessorTestSupporter;

/*
 * testing the case neither Unmatched nor Succeeded events has been triggered
 */
@SuppressWarnings("unused")
@Ignore
public class EplPatternProcessingTest extends ProcessorTestSupporter {

	Gson gson = new Gson();
	TestEvent event0 = new TestEvent().createDefaultTestEvent();

	@Override
	public AbstractProcessor initializeProcessor() {
		return new CommonEplProcessor();
	}
	
	private void setProcessorProperties() {
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
				+ "a=T_50005_0008_01_02(event_id=4720) -> timer:interval(5 seconds) -> "
				+ "b=T_50005_0008_01_02("
				+ "event_id=4726 "
				+ "AND "
				+ "b.target_user_name_hash = a.target_user_name_hash "
				+ "AND "
				+ "b.hostname_domain = a.hostname_domain"
				+ ") "
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
		runner.setProperty(CommonPropertyDescriptor.ESPER_ENGINE, "EsperEngineService");
	}


	//testing unmatched events
	@Test public void testUnmatchedEventBeenTriggered() throws CloneNotSupportedException {
		setProcessorProperties();
		
		//unmatched...
		TestEvent event1 = event0.clone();
		event1.setEvent_id(4720);
		event1.setTarget_user_name_hash("79957b8bf053a695e62603c1f81bb48");
		TestEvent event2 = event0.clone();
		event2.setEvent_id(4726);
		event2.setTarget_user_name_hash("79957b8bf053a695e62603c1f81bb48");

		runner.enqueue(gson.toJson(event1).getBytes());
		runner.enqueue(gson.toJson(event2).getBytes());
		
		runner.run(2);
		List<MockFlowFile> unmatched = runner.getFlowFilesForRelationship(CommonEplProcessor.UNMATCHED_EVENT);
		unmatched.get(0).assertContentEquals("{\"tilde_event_uuid\":\"UC0001-03-TC01-TRIGGER-2019-0103-112501-0007\","
										+ "\"cep_delta\":1800,"
										+ "\"host_hash\":\"256785f9d61e3b6abcaa9168574e9f09\","
										+ "\"target_user_name\":\"e######adm\","
										+ "\"hostname_domain\":\"5f9d61e3b6abcaa9168574e9f09\","
										+ "\"target_user_name_hash\":\"79957b8bf053a695e62603c1f81bb48\","
										+ "\"event_id\":4726,"
										+ "\"epoch\":0}");
	}
	
	//testing succeeded events
	@Test public void testSucceededEventBeenTriggered() throws CloneNotSupportedException {
		setProcessorProperties();
		
		//succeeded...
		TestEvent event3 = event0.clone();
		event3.setEvent_id(4720);
		event3.setTarget_user_name_hash("79957b8bf053a695e62603c1f81bb49");
		TestEvent event4 = event0.clone();
		event4.setEvent_id(4726);
		event4.setTarget_user_name_hash("79957b8bf053a695e62603c1f81bb49");
		
		/*
		 * setting run schedule due to timer:interval pattern observer. 
		 * Unfortunately, it is very unreliable and doesn't match with time interval set on in observer. 
		 * The bug has been addressed in NW-102
		 */
		runner.setRunSchedule(10000l); 

		runner.enqueue(gson.toJson(event3).getBytes());
		runner.enqueue(gson.toJson(event4).getBytes());
		
		runner.run(2);
		
		List<MockFlowFile> succeeded = runner.getFlowFilesForRelationship(CommonEplProcessor.SUCCEEDED_EVENT);
		runner.assertQueueEmpty();
	}

	public static class TestEvent implements Cloneable {
		private String tilde_event_uuid;
		private long cep_delta;
		private String host_hash;
		private String target_user_name;
		private String hostname_domain;
		private String target_user_name_hash;
		private int event_id;
		private long epoch;

		public TestEvent() { }

		public TestEvent clone() throws CloneNotSupportedException {
			return (TestEvent) super.clone(); 
		}

		public TestEvent createDefaultTestEvent() {
			setTilde_event_uuid("UC0001-03-TC01-TRIGGER-2019-0103-112501-0007");
			setCep_delta(1800);
			setHost_hash("256785f9d61e3b6abcaa9168574e9f09");
			setTarget_user_name("e######adm");
			setHostname_domain("5f9d61e3b6abcaa9168574e9f09");
			setTarget_user_name_hash("79957b8bf053a695e62603c1f81bb48");
			setEvent_id(4720);
			setEpoch(0L);
			return this;
		}

		
		public long getEpoch() {
			return epoch;
		}

		public void setEpoch(long epoch) {
			this.epoch = epoch;
		}

		public String getTilde_event_uuid() {
			return tilde_event_uuid;
		}
		public void setTilde_event_uuid(String tilde_event_uuid) {
			this.tilde_event_uuid = tilde_event_uuid;
		}
		public long getCep_delta() {
			return cep_delta;
		}
		public void setCep_delta(long cep_delta) {
			this.cep_delta = cep_delta;
		}
		public String getHost_hash() {
			return host_hash;
		}
		public void setHost_hash(String host_hash) {
			this.host_hash = host_hash;
		}
		public String getTarget_user_name() {
			return target_user_name;
		}
		public void setTarget_user_name(String target_user_name) {
			this.target_user_name = target_user_name;
		}
		public String getHostname_domain() {
			return hostname_domain;
		}
		public void setHostname_domain(String hostname_domain) {
			this.hostname_domain = hostname_domain;
		}
		public String getTarget_user_name_hash() {
			return target_user_name_hash;
		}
		public void setTarget_user_name_hash(String target_user_name_hash) {
			this.target_user_name_hash = target_user_name_hash;
		}
		public int getEvent_id() {
			return event_id;
		}
		public void setEvent_id(int event_id) {
			this.event_id = event_id;
		}
	}
}
