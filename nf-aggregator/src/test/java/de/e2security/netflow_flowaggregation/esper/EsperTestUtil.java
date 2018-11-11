package de.e2security.netflow_flowaggregation.esper;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.time.CurrentTimeEvent;

import de.e2security.netflow_flowaggregation.model.protocols.NetflowEvent;

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
	
}
