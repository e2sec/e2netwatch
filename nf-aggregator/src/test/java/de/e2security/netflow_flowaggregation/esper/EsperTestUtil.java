package de.e2security.netflow_flowaggregation.esper;

import java.util.List;

import com.espertech.esper.client.EPServiceProvider;

import de.e2security.netflow_flowaggregation.netflow.NetflowEvent;

public final class EsperTestUtil {
	
	//sending events to EPEngine (mocking up kafka consumer)
	public static void sendDataToEsper(List<String> lines, EPServiceProvider engine) {
		lines.forEach(line -> {
			NetflowEvent event = null;
			try { event = new NetflowEvent(line); } 
			catch (Exception e) { e.printStackTrace(); }
			engine.getEPRuntime().sendEvent(event);
		});
	}
	
}
