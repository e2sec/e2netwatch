package de.e2security.netflow_flowaggregation.esper;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.scopetest.SupportUpdateListener;
import com.espertech.esper.client.time.CurrentTimeEvent;

import de.e2security.netflow_flowaggregation.esper.epl.UdpEplExpressions;
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
 * 
 * notes to udp connections:
 * 	- the order of events (c->d or d->e) plays no role -> 
 *  rather evaluation of epl reference checker in epl statement since a->b connections has been checked out +
 *  time:interval within they appear again
 */
@SuppressWarnings({"deprecation","unused"})
public class TestUdpEplExpressionsWithManualEventsInjection extends EsperTestSupporter {
	
	boolean stdout = false;
	
	private static final Logger LOG = LoggerFactory.getLogger(TestUdpEplExpressionsWithManualEventsInjection.class);
	int offset = 120000;
	protected EPServiceProvider engine;
	protected EPAdministrator admin;
	protected EPRuntime runtime;
	protected SupportUpdateListener supportListener;
	int finished;
	
	@Before public void init() {
		finished = 0;
		Configuration config = new Configuration();
		config.addEventType(NetflowEvent.class);
		config.addEventType(NetflowEventOrdered.class);
		config.addEventType(ProtocolRegister.class);
		config.getEngineDefaults().getThreading().setInternalTimerEnabled(false);
		engine = EPServiceProviderManager.getDefaultProvider(config);
		runtime = engine.getEPRuntime();
		admin = engine.getEPAdministrator();
		supportListener = new SupportUpdateListener();
		EPStatement stmt0 = admin.createEPL(UdpEplExpressions.eplFinishedUDPFlows());
		EPStatement stmt1 = admin.createEPL(EplExpressionTestSupporter.selectUdpConnections());
		stmt1.addListener(supportListener);
	}

	@After public void destroy() {
		engine.destroy();
	}
	
	/*
	 *Assumption: connection defined at first as unfinished should change its status to finished after further 2 minutes 
	 *since no any further protocols from the same event has been detected
	 * * E.g. A1->B1 -> farther connections between/from A and/or B within next 120 seconds. <- defined as not finished
	 * 		  A2->B2 -> farther evaluations... <- no any further protocols regarding A1 -> connection a1-b1 should defined as finished
	 */
	@Test public void connectionDefinedAsUnfinishedShouldChangeStatusSinceNoAnyFurtherProtocolsHasBeenDetected() {
		List<NetflowEvent> events = getHistoricalEvents(TestUtil.readSampleDataFile("udp_mixed.sample"), 10);
		Pair<Long,Long> timer = getTimeFrameForCurrentTimer(events);
		runtime.sendEvent(new CurrentTimeEvent(timer.getLeft()));
		try {
			//correlated
			NetflowEventOrdered event1 = events.get(0).convertToOrderedType();
			NetflowEventOrdered event2 = events.get(1).convertToOrderedType();
			NetflowEventOrdered event3 = events.get(2).convertToOrderedType();
			NetflowEventOrdered event4 = events.get(3).convertToOrderedType();
			//yet one correlated pair
			NetflowEventOrdered event9 = events.get(8).convertToOrderedType();
			NetflowEventOrdered event10 = events.get(9).convertToOrderedType();
			runtime.sendEvent(event1);
			runtime.sendEvent(event2);
			runtime.sendEvent(new CurrentTimeEvent(timer.getLeft() + 60000));
			runtime.sendEvent(event3);
			runtime.sendEvent(new CurrentTimeEvent(timer.getLeft() + offset));
			//offset set -> new event A will be evaluated
			runtime.sendEvent(event9);
			runtime.sendEvent(event10);
			runtime.sendEvent(new CurrentTimeEvent(timer.getLeft() + 2*offset));
			finished++; //define as finished pair of event 9 and 10 
			finished++; //as well as events 1 and 2
		} catch (NetflowEventException ex) { ex.printStackTrace(); }
			Assert.assertEquals(finished, supportListener.getNewDataList().size());
	}
	/*
	 * test that no connection will be detected since no any correlated events will be sent to
	 */
	@Test public void noConnectionDetectedTest() {
		List<NetflowEvent> events = getHistoricalEvents(TestUtil.readSampleDataFile("udp_mixed.sample"), 16);
		Pair<Long,Long> timer = getTimeFrameForCurrentTimer(events);
		runtime.sendEvent(new CurrentTimeEvent(timer.getLeft())); //initial start
		try {
			NetflowEventOrdered ordEvent13 = events.get(12).convertToOrderedType();
			NetflowEventOrdered ordEvent14 = events.get(13).convertToOrderedType();
			NetflowEventOrdered ordEvent15 = events.get(14).convertToOrderedType();
			NetflowEventOrdered ordEvent16 = events.get(15).convertToOrderedType();
			//all events are uncorrelated
			runtime.sendEvent(ordEvent13);
			runtime.sendEvent(ordEvent14);
			runtime.sendEvent(ordEvent15);
			runtime.sendEvent(ordEvent16);
			runtime.sendEvent(new CurrentTimeEvent(timer.getRight() + offset));
			//no events are to be awaited
		} catch (NetflowEventException ex) {}
		Assert.assertEquals(finished, supportListener.getNewDataList().size());
	}

	/*
	 * since packets are sent one direction within timer:interval connection cannot defined as finished;
	 * e.g. A->B->timer:interval(B->A)
	 */
	@Test public void unfinishedOneDirectionConnectionTest() {
		List<NetflowEvent> events = getHistoricalEvents(TestUtil.readSampleDataFile("udp_unfinished_one_direction_connection_test.sample"), 8);
		Pair<Long,Long> timer = getTimeFrameForCurrentTimer(events);
		runtime.sendEvent(new CurrentTimeEvent(timer.getLeft())); //initial start
		try {
			//mixed flow unfinished one direction: see epl udp statement for legend (a,b,c,d)
			NetflowEventOrdered ordEvent1 = events.get(0).convertToOrderedType(); //a
			NetflowEventOrdered ordEvent2 = events.get(1).convertToOrderedType(); //
			NetflowEventOrdered ordEvent3 = events.get(2).convertToOrderedType(); //b
			NetflowEventOrdered ordEvent4 = events.get(3).convertToOrderedType(); //
			NetflowEventOrdered ordEvent5 = events.get(4).convertToOrderedType(); //
			NetflowEventOrdered ordEvent6 = events.get(5).convertToOrderedType(); //d
			NetflowEventOrdered ordEvent7 = events.get(6).convertToOrderedType(); //d
			NetflowEventOrdered ordEvent8 = events.get(7).convertToOrderedType(); //d
			
			runtime.sendEvent(ordEvent1);
			runtime.sendEvent(ordEvent2);
			runtime.sendEvent(ordEvent3);
			runtime.sendEvent(ordEvent4);
			runtime.sendEvent(ordEvent5);
			runtime.sendEvent(ordEvent6);
			runtime.sendEvent(ordEvent7);
			runtime.sendEvent(ordEvent8);
			runtime.sendEvent(new CurrentTimeEvent(timer.getKey() + 119000)); //unfinished connection test
			//no finished connections are to be awaited

		} catch (NetflowEventException ex) { ex.printStackTrace(); }
		Assert.assertEquals(finished,supportListener.getNewDataList().size());
	}
	
	@Test public void mixedUdpConnectionsTest() {
		
		List<NetflowEvent> events = getHistoricalEvents(TestUtil.readSampleDataFile("udp_mixed.sample"), 17);
		Pair<Long,Long> timer = getTimeFrameForCurrentTimer(events);
		runtime.sendEvent(new CurrentTimeEvent(timer.getLeft())); //initial start
		try {
			//correlated
			NetflowEventOrdered ordEvent1 = events.get(0).convertToOrderedType();  
			NetflowEventOrdered ordEvent2 = events.get(1).convertToOrderedType();
			NetflowEventOrdered ordEvent3 = events.get(2).convertToOrderedType();
			NetflowEventOrdered ordEvent4 = events.get(3).convertToOrderedType();
			NetflowEventOrdered ordEvent5 = events.get(4).convertToOrderedType();
			NetflowEventOrdered ordEvent6 = events.get(5).convertToOrderedType();
			NetflowEventOrdered ordEvent7 = events.get(6).convertToOrderedType();
			NetflowEventOrdered ordEvent8 = events.get(7).convertToOrderedType();
			NetflowEventOrdered ordEvent9 = events.get(8).convertToOrderedType();
			NetflowEventOrdered ordEvent10 = events.get(9).convertToOrderedType();
			
			NetflowEventOrdered ordEvent11 = events.get(10).convertToOrderedType();
			NetflowEventOrdered ordEvent12 = events.get(11).convertToOrderedType();
			
			//not correlated
			NetflowEventOrdered ordEvent13 = events.get(12).convertToOrderedType();
			NetflowEventOrdered ordEvent14 = events.get(13).convertToOrderedType();
			NetflowEventOrdered ordEvent15 = events.get(14).convertToOrderedType();
			NetflowEventOrdered ordEvent16 = events.get(15).convertToOrderedType();
			NetflowEventOrdered ordEvent17 = events.get(16).convertToOrderedType();

			//correlated
			runtime.sendEvent(ordEvent1); 
			runtime.sendEvent(ordEvent2); 
			//timer:interval(120 sec) -> set offset
			runtime.sendEvent(new CurrentTimeEvent(timer.getLeft() + offset)); // first finished
			finished++;
			//correlated
			runtime.sendEvent(ordEvent3);
			runtime.sendEvent(ordEvent4); 
			runtime.sendEvent(new CurrentTimeEvent(timer.getLeft() + offset*2)); //second finished
			finished++;
			//correlated
			runtime.sendEvent(ordEvent5);
			runtime.sendEvent(ordEvent6);
			runtime.sendEvent(ordEvent7);
			runtime.sendEvent(ordEvent8);
			runtime.sendEvent(new CurrentTimeEvent(timer.getKey() + offset*3)); //third finished
			finished++;
			//the same correlated
			runtime.sendEvent(ordEvent9);
			runtime.sendEvent(ordEvent10);
			runtime.sendEvent(new CurrentTimeEvent(timer.getKey() + offset*4)); //fourth finished
			finished++;
			//send one other pair with uncorrelated event meantime
			runtime.sendEvent(ordEvent11); //a
			runtime.sendEvent(ordEvent12); //b
			runtime.sendEvent(ordEvent13); //not correlated
			runtime.sendEvent(ordEvent14); //a as c
			runtime.sendEvent(new CurrentTimeEvent(timer.getKey() + offset*5)); //fifth finished
			finished++;
			//send four uncorrelated events
			runtime.sendEvent(ordEvent14);
			runtime.sendEvent(ordEvent15);
			runtime.sendEvent(ordEvent16);
			runtime.sendEvent(ordEvent17);
			runtime.sendEvent(new CurrentTimeEvent(timer.getKey() + offset*6));
			//no finished connections are to be awaited
		} catch (NetflowEventException e1) { e1.printStackTrace(); }
		supportListener.getNewDataList().forEach(event -> {
			if (stdout) LOG.info(((ProtocolRegister) event[0].getUnderlying()).toString());
		});
		int eventsPassedThrough = supportListener.getNewDataList().size();
		Assert.assertEquals(finished++, eventsPassedThrough);
	}
	
}
