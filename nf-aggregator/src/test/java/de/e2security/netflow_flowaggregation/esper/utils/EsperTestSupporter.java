package de.e2security.netflow_flowaggregation.esper.utils;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.After;
import org.junit.Before;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;

import de.e2security.netflow_flowaggregation.model.protocols.NetflowEvent;
import de.e2security.netflow_flowaggregation.model.protocols.NetflowEventOrdered;
import de.e2security.netflow_flowaggregation.model.protocols.ProtocolRegister;
import de.e2security.netflow_flowaggregation.utils.TestUtil;

public abstract class EsperTestSupporter {
	/*
	 * this is main epl test class, thus all EventTypes should be defined in init();
	 */
	protected EPServiceProvider engine;
	protected EPAdministrator admin;
	protected EPRuntime runtime;
	
	@Before public void init() {
		Configuration config = new Configuration();
		config.addEventType(NetflowEvent.class);
		config.addEventType(NetflowEventOrdered.class);
		config.addEventType(ProtocolRegister.class);
		config.getEngineDefaults().getThreading().setInternalTimerEnabled(false);
		engine = EPServiceProviderManager.getDefaultProvider(config);
		runtime = engine.getEPRuntime();
		admin = engine.getEPAdministrator();
	}

	@After public void destroy() {
		engine.destroy();
	}
	
	//sending events to EPEngine (mocking up kafka consumer)
		protected static void sendDataToEsper(List<String> lines, EPServiceProvider engine) {
			lines.forEach(line -> {
				NetflowEvent event = null;
				try { event = new NetflowEvent(line); } 
				catch (Exception e) { e.printStackTrace(); }
				engine.getEPRuntime().sendEvent(event);
			});
		}

		//specify the number of events to be tested, taking control over sending the data during tests
		protected static List<NetflowEvent> getHistoricalEvents(List<String> lines, int quantity) {
			List<NetflowEvent> testEvents = new ArrayList<>();
			int counter = 1;
			for(String line : lines) {
				try { 
					NetflowEvent event = new NetflowEvent(line);
					testEvents.add(event);
				} catch (Exception ex) { ex.printStackTrace(); }
				if (counter >= quantity) 
					break;
				else 
					counter++;
			}
			return testEvents;
		}
		
		protected static Pair<Long, Long> getTimeFrameForCurrentTimer(List<NetflowEvent> events) {
			long currentEventTime = TestUtil.getCurrentTimeEvent(events.get(0).getLast_switched()); 
			long lastEventTime = TestUtil.getCurrentTimeEvent(events.get(events.size() - 1).getLast_switched());
			long delta = lastEventTime - currentEventTime;
			Pair<Long, Long> pair = new ImmutablePair<>(currentEventTime, lastEventTime + delta);
			return pair;
		}
		
		protected static Pair<Long, Long> getTimeFrameForCurrentTimer(ArrayDeque<NetflowEventOrdered> events) {
			long currentEventTime = TestUtil.getCurrentTimeEvent(events.getFirst().getLast_switched()); 
			long lastEventTime = TestUtil.getCurrentTimeEvent(events.getLast().getLast_switched());
			long delta = lastEventTime - currentEventTime;
			Pair<Long, Long> pair = new ImmutablePair<>(currentEventTime, lastEventTime + delta);
			return pair;
		}
		

}
