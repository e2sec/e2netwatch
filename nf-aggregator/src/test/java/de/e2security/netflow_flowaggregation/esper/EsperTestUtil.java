package de.e2security.netflow_flowaggregation.esper;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.time.CurrentTimeEvent;

import de.e2security.netflow_flowaggregation.model.protocols.NetflowEvent;
import de.e2security.netflow_flowaggregation.model.protocols.NetflowEventOrdered;
import de.e2security.netflow_flowaggregation.utils.TestUtil;

public final class EsperTestUtil {

	//sending events to EPEngine (mocking up kafka consumer)
	public static void sendDataToEsper(List<String> lines, EPServiceProvider engine) {
		lines.forEach(line -> {
			NetflowEvent event = null;
			try { event = new NetflowEvent(line); } 
			catch (Exception e) { e.printStackTrace(); }
			engine.getEPRuntime().sendEvent(new CurrentTimeEvent(1541249347));
			engine.getEPRuntime().sendEvent(event);
		});
	}

	//specify the number of events to be tested, taking control over sending the data during tests
	public static List<NetflowEvent> getHistoricalEvents(List<String> lines, int quantity) {
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
	
	public static Pair<Long, Long> getTimeFrameForCurrentTimer(List<NetflowEvent> events) {
		long currentEventTime = TestUtil.getCurrentTimeEvent(events.get(0).getLast_switched()); 
		long lastEventTime = TestUtil.getCurrentTimeEvent(events.get(events.size() - 1).getLast_switched());
		long delta = lastEventTime - currentEventTime;
		Pair<Long, Long> pair = new ImmutablePair<>(currentEventTime, lastEventTime + delta);
		return pair;
	}
	
	public static Pair<Long, Long> getTimeFrameForCurrentTimer(ArrayDeque<NetflowEventOrdered> events) {
		long currentEventTime = TestUtil.getCurrentTimeEvent(events.getFirst().getLast_switched()); 
		long lastEventTime = TestUtil.getCurrentTimeEvent(events.getLast().getLast_switched());
		long delta = lastEventTime - currentEventTime;
		Pair<Long, Long> pair = new ImmutablePair<>(currentEventTime, lastEventTime + delta);
		return pair;
	}
	
}
