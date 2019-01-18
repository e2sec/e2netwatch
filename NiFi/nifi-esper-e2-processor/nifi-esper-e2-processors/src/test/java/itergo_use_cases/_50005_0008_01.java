package itergo_use_cases;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.nifi.processor.AbstractProcessor;
import org.apache.nifi.stream.io.ByteArrayInputStream;
import org.apache.nifi.util.MockFlowFile;
import org.junit.Test;

import de.e2security.processors.e2esper.CommonEplProcessor;
import de.e2security.processors.e2esper.utilities.CommonPropertyDescriptor;
import de.e2security.processors.e2esper.utilities.ProcessorTestSupporter;

public class _50005_0008_01 extends ProcessorTestSupporter {
	
	@Override
	public AbstractProcessor initializeProcessor() {
		return new CommonEplProcessor();
	}
	
	@Test public void eventAsMapHasBeenTransformedToJsonFormat() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		String json  = "{\"cep_delta\":202876,\"event_id\":4720,\"target_user_name_hash\":\"427e10b4b02770a2103a4bf761de8e4c\",\"target_user_name\":\"anon004\",\"host_hash\":\"74177b67767601a7410f17ff18d55f33\",\"hostname_domain\":\"corp.ergo\",\"tilde_event_uuid\":\"UC0008-01-TC04-NOTRIGGER-2018-1228-121002-0047\"}";
		String json2 = "{\"cep_delta\":202876,\"event_id\":4726,\"target_user_name_hash\":\"427e10b4b02770a2103a4bf761de8e4c\",\"target_user_name\":\"anon004\",\"host_hash\":\"74177b67767601a7410f17ff18d55f33\",\"hostname_domain\":\"corp.ergo\",\"tilde_event_uuid\":\"UC0008-01-TC04-NOTRIGGER-2018-1228-121002-0047\"}";
		baos.write(json.getBytes());
		baos.write(json2.getBytes());
		InputStream inEvents = new ByteArrayInputStream(baos.toByteArray());
//		runner.setProperty(CommonPropertyDescriptor.EPL_STATEMENT, "@Name(ESPER_DEBUGGER) @Audit SELECT '50005-0008-01' AS alert_uc_scenario, 'ip_hash' AS enrichment_field,'50005_inv_ip2ci' AS enrichment_index, a.host_hash AS enrichment_key, a.tilde_event_uuid AS alert_reference1, b.tilde_event_uuid AS alert_reference2, a.cep_delta AS cep_delta, a.host_hash AS host_hash, a.target_user_name AS target_user_name, a.target_user_name_hash AS target_user_name_hash FROM PATTERN [ every-distinct(target_user_name_hash, 86401 sec) a=T_50005_0008_01_02(event_id=4720) -> timer:interval(5 seconds) -> b=T_50005_0008_01_02(event_id=4726 AND b.target_user_name_hash = a.target_user_name_hash AND b.hostname_domain = a.hostname_domain) WHERE timer:within(86400 sec) ]");
		runner.setProperty(CommonPropertyDescriptor.EPL_STATEMENT, "@Name(ESPER_DEBUGGER) @Audit SELECT '50005-0008-01' AS alert_uc_scenario FROM PATTERN [ every a=T_50005_0008_01_02(event_id=4720) -> b=T_50005_0008_01_02(event_id=4726) ]");
		runner.setProperty(CommonPropertyDescriptor.EVENT_SCHEMA, "create schema T_50005_0008_01_02 as (event_id int)");
		runner.setProperty(CommonPropertyDescriptor.ESPER_ENGINE,"EsperEngineService");
		runner.enqueue(json.getBytes());
		runner.enqueue(json2.getBytes());
		runner.run(2);
		List<MockFlowFile> file = runner.getFlowFilesForRelationship(CommonEplProcessor.SUCCEEDED_EVENT);
		if (file.size() > 0) {
			file.get(0).assertContentEquals(json);
		}
	}

}
