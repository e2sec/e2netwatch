package de.e2security.netflow_flowaggregation.esper;

import java.util.List;
import java.util.Queue;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.scopetest.EPAssertionUtil;
import com.espertech.esper.client.scopetest.SupportUpdateListener;
import com.espertech.esper.client.time.CurrentTimeEvent;

import de.e2security.netflow_flowaggregation.esper.utils.EplExpressionTestSupporter;
import de.e2security.netflow_flowaggregation.esper.utils.EsperTestSupporter;
import de.e2security.netflow_flowaggregation.model.protocols.NetflowEvent;
import de.e2security.netflow_flowaggregation.model.protocols.NetflowEventOrdered;
import de.e2security.netflow_flowaggregation.utils.TestUtil;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestUdpEplExpressions extends EsperTestSupporter {
	
	int numberOfTestEvents = 100;
	NetflowEventsCorrectOrderTestListener listener = new NetflowEventsCorrectOrderTestListener(true);
	
	/*
	 * dummy test since the sort statement has been already tested in tcpTests
	 * just to ensure no data are corrupted in dataset 
	 */
	@Test public void A_sortUdpNetflowEventsTest() { 
		List<NetflowEvent> events = getHistoricalEvents(TestUtil.readSampleDataFile("nf_gen.udp.sample"), numberOfTestEvents);
		SupportUpdateListener supportListener = new SupportUpdateListener();
		EPStatement stmt0 = admin.createEPL(EplExpressionTestSupporter.selectNetStreamOrdered());
		EPStatement stmt1 = admin.createEPL(NetflowEventEplExpressions.eplSortByLastSwitched());
		stmt0.addListener(listener);
		Pair<Long,Long> timer = getTimeFrameForCurrentTimer(events);
		engine.getEPRuntime().sendEvent(new CurrentTimeEvent(timer.getLeft()));
		events.forEach(event -> {
				engine.getEPRuntime().sendEvent(event);
		});
		engine.getEPRuntime().sendEvent(new CurrentTimeEvent(timer.getRight()));
		Queue<NetflowEventOrdered> orderedList = listener.getNetflowsOrdered();
		Assert.assertEquals(events.size(), orderedList.size());
	}
	
	@Test public void eplFinishedUDPFlowsTest() {
		EPAssertionUtil esperAssert = new EPAssertionUtil();
		Queue<NetflowEventOrdered> orderedEvents = listener.getNetflowsOrdered();
		SupportUpdateListener supportListener = new SupportUpdateListener();
		EPStatement stmt0 = admin.createEPL(UdpEplExpressions.eplFinishedUDPFlows());
		EPStatement stmt1 = admin.createEPL(EplExpressionTestSupporter.selectUdpConnections());
		stmt1.addListener(new UdpFinishedConnectionsListener());
	}
	
	
}
