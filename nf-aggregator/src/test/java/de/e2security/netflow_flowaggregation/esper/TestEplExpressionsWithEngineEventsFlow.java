package de.e2security.netflow_flowaggregation.esper;

import java.text.ParseException;
import java.time.ZonedDateTime;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

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

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestEplExpressionsWithEngineEventsFlow {
	
	/*
	 * in this test class we're trying to test the natural flow of events within esper engine regarding the ep statements order;
	 * for this reason the listener has been defined globally and marked as static;
	 * the listener fetch data from the first ep statement (eplSortByLastSwitched) and saves them in the correct order (list of ordered events);
	 * all further tests/statements are working with ordered event instances;
	 * regarding the sequence of farther tests regarding the actual statements is not required. 
	 * 
	 * for testing the injection of data manually in order to test the epl statements independently please use EplExpressionTestWithManualEventsInjection class
	 */

	EPServiceProvider engine;
	EPAdministrator admin;
	EPRuntime runtime;
	static NetflowEventsCorrectOrderTestListener listener = new NetflowEventsCorrectOrderTestListener(false); //static in order to use over the tests

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

	/*
	 * should be implementes as first test -> cause marked with A; 
	 * the following order is not important
	 */
	@Test public void A_eplSortByLastSwitchedTest() throws ParseException {
		/*
		 * while extending the number of lines to be read from sample data -> windowSpanTime should be also adjusted
		 */
		int numberOfEvents = 100;
		List<NetflowEvent> events = EsperTestUtil.getHistoricalEvents(TestUtil.readSampleDataFile("nf_gen.tcp.sample"), numberOfEvents);
		Pair<Long,Long> timer = EsperTestUtil.getTimeFrameForCurrentTimer(events);
		int window = 100;
		/*
		 * setting listener on the second statement;
		 * however, through adding rstream into the first statement after keyword 'select' the same result can be achieved w/o the second one;
		 */
		EPStatement statement1 = admin.createEPL(TcpEplExpressions.eplSortByLastSwitched());
		EPStatement statement2 = admin.createEPL(TcpEplExpressionsTest.selectNetStreamOrdered());
		statement2.addListener(listener);
		engine.getEPRuntime().sendEvent(new CurrentTimeEvent(timer.getKey())); // set initial start window for the historical data (external timer)
		events.forEach(runtime::sendEvent);
		engine.getEPRuntime().sendEvent(new CurrentTimeSpanEvent(timer.getValue(), window)); //set advance time in ms while sliding window each x ms;
		Queue<ZonedDateTime> dates = listener.getDates();
		int correctOrder = 0;
		while (dates.size() >= 2) {
			ZonedDateTime lessRecent = dates.poll();
			ZonedDateTime moreRecent = dates.peek(); 
			if (lessRecent.compareTo(moreRecent) == 0 || lessRecent.compareTo(moreRecent) < 0) { //this < that = negative, this > that = positive, 0 = neutral
				correctOrder++;
			}
		}
		Assert.assertEquals(events.size() - 1, correctOrder);
	}

	//test code using data generated during eplSortByLastSwitched() <- as chain of test methods
	@Test public void finishedTcpConnectionsTest() {
		int window = 100;
		SupportUpdateListener supportListener = new SupportUpdateListener();
		NetflowEventsFinishedTcpConnectionsListener localListener = new NetflowEventsFinishedTcpConnectionsListener(false);
		ArrayDeque<NetflowEventOrdered> netflowsOrdered = (ArrayDeque<NetflowEventOrdered>) listener.getNetflowsOrdered();
		Pair<Long,Long> timer = EsperTestUtil.getTimeFrameForCurrentTimer(netflowsOrdered);
		EPStatement detectFinished = admin.createEPL(TcpEplExpressions.eplFinishedFlows());
		EPStatement selectFinished = admin.createEPL(TcpEplExpressionsTest.selectFinishedTcpConnections());
		selectFinished.addListener(localListener);
		selectFinished.addListener(supportListener);
		runtime.sendEvent(new CurrentTimeEvent(timer.getKey()));
		netflowsOrdered.forEach(runtime::sendEvent);
		runtime.sendEvent(new CurrentTimeSpanEvent(timer.getValue(), window));
		/*
		 * during assertion:
		 * 	compare the result of native Esper's EPstatement with manual boolean checker in NetflowEventsFinishedTcpConnectionsListener
		 */
		Assert.assertEquals(supportListener.getNewDataList().size(), localListener.getFinishedConns().size());
	}

	@Test public void rejectedTcpConnectionsWithInFlagsSynAndAckAndOutFlagsRstTest() {
		String rejectedPattern1 = "[every a=NetflowEventOrdered(protocol=6 and (tcp_flags&2)=2 and (tcp_flags&16)=0) -> "
				+ " b=NetflowEventOrdered(protocol=6 and (tcp_flags&4)=4 and host=a.host ";

		Assert.assertFalse(true);
	}

	@Test public void rejectedTcpConnectionsWithInFlagsRstAndOutFlagsSynAndAckTest() {
		String rejectedPattern2 = "[every a=NetflowEventOrdered(protocol=6 and (tcp_flags&4)=4) ->"
				+ " b=NetflowEventOrdered(protocol=6 and (tcp_flags&2)=2 and (tcp_flags&16)=0 and host=a.host ";
		Assert.assertFalse(true);
	}
}
