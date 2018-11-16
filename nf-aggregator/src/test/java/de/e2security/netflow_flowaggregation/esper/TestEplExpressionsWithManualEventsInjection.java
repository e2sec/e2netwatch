package de.e2security.netflow_flowaggregation.esper;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
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
import com.espertech.esper.client.scopetest.SupportUpdateListener;
import com.espertech.esper.client.time.CurrentTimeEvent;
import com.espertech.esper.client.time.CurrentTimeSpanEvent;

import de.e2security.netflow_flowaggregation.model.protocols.NetflowEvent;
import de.e2security.netflow_flowaggregation.model.protocols.NetflowEventOrdered;
import de.e2security.netflow_flowaggregation.model.protocols.TcpConnection;
import de.e2security.netflow_flowaggregation.utils.TestUtil;

public class TestEplExpressionsWithManualEventsInjection {
	
	EPServiceProvider engine;
	EPAdministrator admin;
	EPRuntime runtime;

	@Before public void init() {
		Configuration config = new Configuration();
		config.addEventType(NetflowEvent.class);
		config.addEventType(NetflowEventOrdered.class);
		config.addEventType(TcpConnection.class);
		config.getEngineDefaults().getThreading().setInternalTimerEnabled(false);
		engine = EPServiceProviderManager.getDefaultProvider(config);
		runtime = engine.getEPRuntime();
		admin = engine.getEPAdministrator();
	}

	@After public void destroy() {
		engine.destroy();
	}
	
	//test code during manual injection of finished connections
	@Test public void finishedTcpConnectionIsolatedTest() {
		SupportUpdateListener listener = new SupportUpdateListener();
		int numberOfEvents = 2;
		List<NetflowEvent> eventsList = EsperTestUtil.getHistoricalEvents(TestUtil.readSampleDataFile("netflow_ordered_finished.sample"), numberOfEvents);
		Pair<Long,Long> timer = EsperTestUtil.getTimeFrameForCurrentTimer(eventsList);
		int window = 100;
		EPStatement filterStmt = admin.createEPL(TcpEplExpressions.eplFinishedFlows());
		EPStatement selectStmt = admin.createEPL(TcpEplExpressionsTest.selectTcpConnections());
		selectStmt.addListener(listener);
		runtime.sendEvent(new CurrentTimeEvent(timer.getKey()));
		/*
		 * since the epl statement stay unchanged we have to work with event type classes which are used in ep statements;
		 * e.g. NetflowEventOrdered instead of NetflowEvent
		 */
		eventsList.forEach(event -> {
			try {
				NetflowEventOrdered orderedEvent = event.convertToOrderedType();
				runtime.sendEvent(orderedEvent);
			} catch (Exception e) {	e.printStackTrace(); }
		});
		runtime.sendEvent(new CurrentTimeSpanEvent(timer.getValue(), window));
		/*
		 * divide through 2 cause finished tcp connections are saved in TcpConnection instances contained of two events (in_/out_)
		 */
		Assert.assertEquals(numberOfEvents / 2, listener.getNewDataList().size());
	}
}
