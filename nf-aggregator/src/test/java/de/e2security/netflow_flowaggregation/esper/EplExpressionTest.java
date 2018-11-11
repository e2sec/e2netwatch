package de.e2security.netflow_flowaggregation.esper;

import java.text.ParseException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Queue;

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

//TODO: specify the sequence of tests 
public class EplExpressionTest {

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
	
	@Test public void eplFinishedFlowsTest() throws ParseException {
		/*
		 * while extending the number of lines to be read from sample data -> windowSpanTime should be also adjusted
		 */
		int numberOfEvents = 100;
		List<NetflowEvent> events = EsperTestUtil.getHistoricalEvents(TestUtil.readSampleDataFile("nf_gen.tcp.sample"), numberOfEvents);
		long currentEventTime = TestUtil.getCurrentTimeEvent(events.get(0).getLast_switched()); 
		long lastEvenTime = TestUtil.getCurrentTimeEvent(events.get(numberOfEvents - 1).getLast_switched());
		long delta = lastEvenTime - currentEventTime;
		int window = 100;
		/*
		 * setting listener on the second statement;
		 * however, through adding rstream into the first statement after keyword 'select' the same result can be achieved w/o the second one;
		 */
		EPStatement statement1 = admin.createEPL(TcpEplExpressions.eplSortByLastSwitched());
		EPStatement statement2 = admin.createEPL(TcpEplExpressionsTest.selectNetStreamOrdered());
		statement2.addListener(listener);
		engine.getEPRuntime().sendEvent(new CurrentTimeEvent(currentEventTime)); // set initial start window for the historical data (external timer)
		events.forEach(runtime::sendEvent);
		engine.getEPRuntime().sendEvent(new CurrentTimeSpanEvent(currentEventTime + delta * window , window)); //set advance time in ms while sliding window each x ms;
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
	
	//test code during manual injection of finished connections
	@Test public void testFinishedTcpConnectionIsolated() {
	}
	
	//test code using data generated during eplFinishedFlowsTest()
	@Test public void finishedTcpConnectionsTest() {
		SupportUpdateListener supportListener = new SupportUpdateListener();
		NetflowEventsFinishedTcpConnectionsListener localListener = new NetflowEventsFinishedTcpConnectionsListener(true);
		Queue<NetflowEventOrdered> netflowsOrdered = listener.getNetflowsOrdered();
		ZonedDateTime start = netflowsOrdered.peek().getLast_switched();
		long currentEventTime = TestUtil.getCurrentTimeEvent(start);
		EPStatement detectFinished = admin.createEPL(TcpEplExpressions.eplFinishedFlows());
		EPStatement selectFinished = admin.createEPL(TcpEplExpressionsTest.selectFinishedTcpConnections());
		selectFinished.addListener(localListener);
		selectFinished.addListener(supportListener);
		runtime.sendEvent(new CurrentTimeEvent(currentEventTime));
		netflowsOrdered.forEach(runtime::sendEvent);
		runtime.sendEvent(new CurrentTimeSpanEvent(currentEventTime + 10*60*1000,100));
		/*
		 * during assertion:
		 * 	compare the result of native Esper's EPstatement with manual boolean checker in NetflowEventsFinishedTcpConnectionsListener
		 */
		Assert.assertEquals(supportListener.getNewDataList().size(), localListener.getFinishedConns().size());
	}
	
}
