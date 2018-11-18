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
 * mock up events injection (initially created by nf-generator, but manually adjusted for particular test purposes).
 * In order to not overload class with json strings, all datasets have been saved in its own files;
 * 
 * the problem of udp statement tests is mainly because of 'timer:interval()' used in UdpEplExpressions.eplFinishedUDPFlows() statement,
 * which makes it complicated to use the same approach as by TcpEplExpression tests, namely CurrentTimeSpanEvent, which statements utilize timer:within() method instead;
 * timer:interval() implies the time the engine waits for incoming events;
 * 
 * in such cases timestamp of event doesn't play a big role, rather the time event has been pushed in.
 * for this purpose runtime.sendEvent(new CurrentTimeEvent()) has been utilized to mock up time sliding;
 */
@SuppressWarnings({"deprecation","unused"})
public class TestUdpEplExpressionsWithManualEventsInjection extends EsperTestSupporter {
	
	private static final Logger LOG = LoggerFactory.getLogger(TestUdpEplExpressionsWithManualEventsInjection.class);

	@Test public void mixedUdpConnectionsTest() {
		UdpFinishedConnectionsListener listener = new UdpFinishedConnectionsListener();
		SupportUpdateListener supportListener = new SupportUpdateListener();
		EPStatement stmt0 = admin.createEPL(UdpEplExpressions.eplFinishedUDPFlows2());
		EPStatement stmt1 = admin.createEPL(EplExpressionTestSupporter.selectUdpConnections());
		stmt0.addListener(listener);
		stmt1.addListener(supportListener);
		List<NetflowEvent> events = getHistoricalEvents(TestUtil.readSampleDataFile("udp_connections_ordered_mixed_fin_unfin.sample"), 8);
		Pair<Long,Long> timer = getTimeFrameForCurrentTimer(events);
		runtime.sendEvent(new CurrentTimeEvent(timer.getLeft())); //by last_switched of first event: 12:32:05
		try {
			//correlated
			NetflowEventOrdered ordEvent1 = events.get(0).convertToOrderedType();  
			NetflowEventOrdered ordEvent2 = events.get(1).convertToOrderedType();
			NetflowEventOrdered ordEvent3 = events.get(2).convertToOrderedType();
			NetflowEventOrdered ordEvent4 = events.get(3).convertToOrderedType();
			NetflowEventOrdered ordEvent5 = events.get(4).convertToOrderedType();
			
			//not correlated
			NetflowEventOrdered ordEvent6 = events.get(5).convertToOrderedType();
			NetflowEventOrdered ordEvent7 = events.get(6).convertToOrderedType();
			NetflowEventOrdered ordEvent8 = events.get(7).convertToOrderedType();
			
			runtime.sendEvent(ordEvent1); 
			runtime.sendEvent(ordEvent2); 
			//wait 120 seconds: timer:interval(120 sec) -> set offset
			runtime.sendEvent(new CurrentTimeEvent(timer.getLeft() + 120000)); 
			runtime.sendEvent(ordEvent3);
			runtime.sendEvent(ordEvent4); 
			runtime.sendEvent(ordEvent5);
			runtime.sendEvent(ordEvent6);
			//set offset +120 seconds 
			runtime.sendEvent(new CurrentTimeEvent(timer.getKey() + 240000));
			runtime.sendEvent(ordEvent7);
			runtime.sendEvent(ordEvent8);
			//set offset +360 seconds
			runtime.sendEvent(new CurrentTimeEvent(timer.getKey() + 360000));
		} catch (NetflowEventException e1) { e1.printStackTrace(); }
		supportListener.getNewDataList().forEach(event -> {
			LOG.info(((ProtocolRegister) event[0].getUnderlying()).toString());
		});
		int eventsPassedThrough = supportListener.getNewDataList().size();
		Assert.assertEquals(listener.getFinishedUdpList().size(), eventsPassedThrough);
	}

	@Test public void unfinishedUdpConnectionsAdjustedStmtTest() {
		SupportUpdateListener supportListener = new SupportUpdateListener();
		EPStatement stmt0 = admin.createEPL(UdpEplExpressions.eplFinishedUDPFlows2());
		EPStatement stmt1 = admin.createEPL(EplExpressionTestSupporter.selectUdpConnections());
		stmt1.addListener(supportListener);
		List<NetflowEvent> events = getHistoricalEvents(TestUtil.readSampleDataFile("udp_connections_ordered_and_unfinished.sample"), 4);
		Pair<Long,Long> timer = getTimeFrameForCurrentTimer(events);
		runtime.sendEvent(new CurrentTimeEvent(timer.getLeft())); //by last_switched of first event: 12:32:05
		try {
			NetflowEventOrdered ordEvent1 = events.get(0).convertToOrderedType(); 
			NetflowEventOrdered ordEvent2 = events.get(1).convertToOrderedType();
			NetflowEventOrdered ordEvent3 = events.get(2).convertToOrderedType();
			NetflowEventOrdered ordEvent4 = events.get(3).convertToOrderedType();
			runtime.sendEvent(ordEvent1); //first event's last_switched time: 12:32:05
			runtime.sendEvent(ordEvent2); //second event's last_switched time: 12:33:14
			//set offset to 111 seconds (the next last_switched_time of the upcoming event)
			runtime.sendEvent(new CurrentTimeEvent(timer.getLeft() + 111000));
			runtime.sendEvent(ordEvent3); //last switched 12:35:05 <- less than 2 minutes since last event
			runtime.sendEvent(ordEvent4); //last switched 12:35:10 <- less than 2 minutes since last event
			runtime.sendEvent(new CurrentTimeEvent(timer.getLeft() + 111000 + 9000)); // 120 sec
		} catch (NetflowEventException e1) { e1.printStackTrace(); }
		supportListener.getNewDataList().forEach(event -> {
			LOG.info(((ProtocolRegister) event[0].getUnderlying()).toString());
		});
		int eventsPassedThrough = supportListener.getNewDataList().size();
		Assert.assertEquals(0,eventsPassedThrough);
	}

	@Test public void finishedUdpConnectionsAdjustedStmtTest() {
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
		} catch (NetflowEventException e1) { e1.printStackTrace(); }
		supportListener.getNewDataList().forEach(event -> {
			LOG.info(((ProtocolRegister) event[0].getUnderlying()).toString());
		});
		int eventsPassedThrough = supportListener.getNewDataList().size();
		Assert.assertEquals(listener.getFinishedUdpList().size(), eventsPassedThrough);
	}
	
}
