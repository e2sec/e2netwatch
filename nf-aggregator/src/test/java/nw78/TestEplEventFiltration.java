package nw78;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.UnmatchedListener;
import com.espertech.esper.client.scopetest.SupportUpdateListener;

import de.e2security.netflow_flowaggregation.esper.utils.EsperTestSupporter;
import de.e2security.netflow_flowaggregation.model.protocols.NetflowEvent;
import de.e2security.netflow_flowaggregation.model.protocols.NetflowEventOrdered;
import de.e2security.netflow_flowaggregation.model.protocols.ProtocolRegister;

public class TestEplEventFiltration {
	
	private SupportUpdateListener supportListener = new SupportUpdateListener();
	private UnmatchedListener filteredListener;
	
	protected EPServiceProvider engine;
	protected EPAdministrator admin;
	protected EPRuntime runtime;
	
	@Before public void init() {
		Configuration config = new Configuration();
		Map<String,Object> mapEvent = new HashMap<>();
		mapEvent.put("source.port", int.class);
		mapEvent.put("destination.port", int.class);
		mapEvent.put("network.iana_number", int.class);
		Map<String,Object> alarmEvent = new HashMap<>();
		alarmEvent.put("source.port", int.class);
		alarmEvent.put("destination.port", int.class);
		alarmEvent.put("network.iana_number", int.class);
		// working Method 1 for creating schema on the fly
//		config.addEventType("ProtocolRegister", mapEvent);
//		config.addEventType("AlarmDetected", alarmEvent);
		engine = EPServiceProviderManager.getDefaultProvider(config);
		runtime = engine.getEPRuntime();
		admin = engine.getEPAdministrator();
		// working Method 2 for creating event schema on the fly
		admin.createEPL("create map schema ProtocolRegister as (source.port int,destination.port int,network.iana_number int) ");
		admin.createEPL("create map schema AlarmDetected as (source.port int,destination.port int,network.iana_number int)");
	}

	@After public void destroy() {
		engine.destroy();
	}
	
//	{"source.port":23,"destination.port":21,"network.iana_number":6}
	@Test public void theUnwantedConnectionsCanBeFilteredAndFetched() {
		EPStatement detection = admin.createEPL(EplStatements.detectUnwantedProtocols);
		EPStatement selection = admin.createEPL(EplStatements.selectAlarms);
		selection.addListener(supportListener);
		
//		List<NetflowEventOrdered> unmatched = new ArrayList<>();
//		runtime.setUnmatchedListener(filteredListener = new UnmatchedListener() {
//			@Override
//			public void update(EventBean event) {
//				unmatched.add((NetflowEventOrdered) event.getUnderlying());
//			}
//		});
		Map<String, Integer> genericEvent = new HashMap<String,Integer>();
		genericEvent.put("source.port", 23);
		genericEvent.put("destination.port", 21);
		genericEvent.put("network.iana_number", 6);
		runtime.sendEvent(genericEvent, "ProtocolRegister");
		int matched = supportListener.getNewDataList().size();
		Assert.assertEquals(1, matched);
	}
}
