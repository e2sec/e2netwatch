package de.e2security.netflow_flowaggregation.esper;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.scopetest.SupportUpdateListener;
import com.espertech.esper.client.time.CurrentTimeEvent;
import com.espertech.esper.client.time.CurrentTimeSpanEvent;

import de.e2security.netflow_flowaggregation.esper.utils.EplExpressionTestSupporter;
import de.e2security.netflow_flowaggregation.esper.utils.EsperTestSupporter;
import de.e2security.netflow_flowaggregation.exceptions.NetflowEventException;
import de.e2security.netflow_flowaggregation.model.protocols.NetflowEvent;
import de.e2security.netflow_flowaggregation.model.protocols.NetflowEventOrdered;
import de.e2security.netflow_flowaggregation.model.protocols.ProtocolRegister;
import de.e2security.netflow_flowaggregation.utils.TestUtil;
import junit.framework.Assert;

/*
 * mock up events injection (created by nf-generator, but 
 * specifically mocked up for test purposes)
 */
@SuppressWarnings({"deprecation","unused"})
public class TestUdpEplExpressionsWithManuelEventsInjection extends EsperTestSupporter {
	
	private static final Logger LOG = LoggerFactory.getLogger(TestUdpEplExpressionsWithManuelEventsInjection.class);

	UdpFinishedConnectionsListener listener = new UdpFinishedConnectionsListener();

	//Failed
	@Test public void unfinishedUdpConnectionsInitialStmtTest() {
		int eventsPassedThrough = loadUdpConnection("udp_connections_ordered_and_unfinished.sample", 4, UdpEplExpressions.eplFinishedUDPFlows());
		Assert.assertEquals(0, eventsPassedThrough);
	}

	//Passed
	@Test public void unfinishedUdpConnectionsAdjustedStmtTest() {
		int eventsPassedThrough = loadUdpConnection("udp_connections_ordered_and_unfinished.sample", 4, UdpEplExpressions.eplFinishedUDPFlows2());
		Assert.assertEquals(0, eventsPassedThrough);
	}

	//Failed
	@Test public void finishedUdpConnectionsAdjustedStmtTest() {
		int eventsPassedThrough = loadUdpConnection("udp_connections_ordered_and_finished.sample",4, UdpEplExpressions.eplFinishedUDPFlows2());
		Assert.assertEquals(2, eventsPassedThrough);
	}


	@Test public void finishedUdpConnectionsInitialStmtTest() {
		SupportUpdateListener supportListener = new SupportUpdateListener();
		UdpFinishedConnectionsListener listener = new UdpFinishedConnectionsListener(); //custom listener checks only the events comes through on the premise they are xreferenced and correlate within 120 seconds.
		EPStatement stmt0 = admin.createEPL(UdpEplExpressions.eplFinishedUDPFlows2());
		EPStatement stmt1 = admin.createEPL(EplExpressionTestSupporter.selectUdpConnections());
		stmt0.addListener(listener);
		stmt1.addListener(supportListener);
		List<NetflowEvent> events = getHistoricalEvents(TestUtil.readSampleDataFile("udp_connections_ordered_and_finished.sample"), 4);
		Pair<Long,Long> timer = getTimeFrameForCurrentTimer(events);
		runtime.sendEvent(new CurrentTimeEvent(timer.getLeft())); //by last_switched of first event: 12:32:05
		try {
			NetflowEventOrdered ordEvent1 = events.get(0).convertToOrderedType(); 
			NetflowEventOrdered ordEvent2 = events.get(1).convertToOrderedType();
			NetflowEventOrdered ordEvent3 = events.get(2).convertToOrderedType();
			NetflowEventOrdered ordEvent4 = events.get(3).convertToOrderedType();
			//send first and second events -> should be those which are finished
			runtime.sendEvent(ordEvent1); //first event's last_switched time: 12:32:05
			runtime.sendEvent(ordEvent2); //second event's last_switched time: 12:33:14
			//wait 120 seconds: timer:interval(120 sec) -> set offset
			runtime.sendEvent(new CurrentTimeEvent(timer.getLeft() + 120000)); //1541766725999 (the last three numbers reference to the zone -> should be ignored)
			//check that the comming events are not correlate with the previous pair
			runtime.sendEvent(ordEvent3); //last switched 12:35:47
			runtime.sendEvent(ordEvent4); //last switched 12:36:14
			runtime.sendEvent(new CurrentTimeEvent(timer.getLeft() + 240000));
			//adding events that have no any correlation with the previous batch of events
			
		} catch (NetflowEventException e1) { e1.printStackTrace(); }
		supportListener.getNewDataList().forEach(event -> {
			LOG.info(((ProtocolRegister) event[0].getUnderlying()).toString());
		});
		int eventsPassedThrough = supportListener.getNewDataList().size();
		Assert.assertEquals(listener.getFinishedUdpList().size(), eventsPassedThrough);
	}

	private int loadUdpConnection(String file,int numberOfEvents, String stmt) {
		SupportUpdateListener listener = new SupportUpdateListener();
		EPStatement stmt0 = admin.createEPL(stmt);
		EPStatement stmt1 = admin.createEPL(EplExpressionTestSupporter.selectUdpConnections());
		stmt1.addListener(listener);
		List<NetflowEvent> events = getHistoricalEvents(TestUtil.readSampleDataFile(file), numberOfEvents);
		Pair<Long,Long> timer = getTimeFrameForCurrentTimer(events);
		runtime.sendEvent(new CurrentTimeEvent(timer.getLeft()));
		events.forEach(event -> {
			try { runtime.sendEvent(event.convertToOrderedType()); } 
			catch (Exception e) {	e.printStackTrace(); }
		});
		runtime.sendEvent(new CurrentTimeSpanEvent(timer.getRight()));
		int eventsPassed = listener.getNewDataList().size();
		return eventsPassed;
	}
}
