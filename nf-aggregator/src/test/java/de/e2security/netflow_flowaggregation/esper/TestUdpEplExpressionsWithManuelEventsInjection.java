package de.e2security.netflow_flowaggregation.esper;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.scopetest.SupportUpdateListener;
import com.espertech.esper.client.time.CurrentTimeEvent;
import com.espertech.esper.client.time.CurrentTimeSpanEvent;

import de.e2security.netflow_flowaggregation.esper.utils.EsperTestSupporter;
import de.e2security.netflow_flowaggregation.model.protocols.NetflowEvent;
import de.e2security.netflow_flowaggregation.utils.TestUtil;
import junit.framework.Assert;

/*
 * mock up events injection (created by nf-generator, but 
 * specifically mocked up for test purposes)
 */
public class TestUdpEplExpressionsWithManuelEventsInjection extends EsperTestSupporter {
	
	UdpFinishedConnectionsListener listener = new UdpFinishedConnectionsListener();
	
	@Test public void finishedUdpConnectionsTest() {
		SupportUpdateListener listener = new SupportUpdateListener();
		EPStatement stmt = admin.createEPL(UdpEplExpressions.eplFinishedUDPFlows());
		stmt.addListener(listener);
		List<NetflowEvent> events = getHistoricalEvents(
						TestUtil.readSampleDataFile("udp_connections_ordered_and_finished.sample"), 2);
		Pair<Long,Long> timer = getTimeFrameForCurrentTimer(events);
		runtime.sendEvent(new CurrentTimeEvent(timer.getLeft()));
		events.forEach(event -> {
			try { runtime.sendEvent(event.convertToOrderedType()); } 
			catch (Exception e) {	e.printStackTrace(); }
		});
		runtime.sendEvent(new CurrentTimeSpanEvent(timer.getRight()));
		Assert.assertEquals(2, listener.getNewDataList().size());
	}
}
