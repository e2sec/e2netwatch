package de.e2security.netflow_flowaggregation.esper;

import java.text.ParseException;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Queue;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.time.CurrentTimeEvent;
import com.espertech.esper.client.time.CurrentTimeSpanEvent;

import de.e2security.netflow_flowaggregation.netflow.NetflowEvent;
import de.e2security.netflow_flowaggregation.netflow.NetflowEventOrdered;
import de.e2security.netflow_flowaggregation.utils.TestUtil;


public class EplExpressionTest {

	EPServiceProvider engine;
	static NetflowEventsCorrectOrderTestListener listener = new NetflowEventsCorrectOrderTestListener(false); //static in order to use over the tests
	
	@Before public void init() {
		Configuration config = new Configuration();
		config.addEventType(NetflowEvent.class);
		config.addEventType(NetflowEventOrdered.class);
		config.getEngineDefaults().getThreading().setInternalTimerEnabled(false);
		engine = EPServiceProviderManager.getDefaultProvider(config);
	}
	
	@After public void destroy() {
		engine.destroy();
	}
	
	@Test public void eplFinishedFlowsTest() throws ParseException {
		List<NetflowEvent> events = EsperTestUtil.getHistoricalEvents(TestUtil.readSampleDataFile("nf_gen.tcp.sample"), 100);
		DateTimeFormatter formater = DateTimeFormatter.ISO_INSTANT;
		ZonedDateTime initial = events.get(0).getLast_switched();
		Date startTime = Date.from(Instant.from(formater.parse(initial.toString()))); //get initial time point for window
		/*
		 * setting listener on the second statement;
		 * however, through adding rstream into the first statement after keyword 'select' the same result can be achieved w/o the second one;
		 */
		EPStatement statement1 = engine.getEPAdministrator().createEPL(TcpEplExpressions.eplSortByLastSwitched());
		EPStatement statement2 = engine.getEPAdministrator().createEPL(TcpEplExpressionsTest.selectNetStreamOrdered());
		statement2.addListener(listener);
		engine.getEPRuntime().sendEvent(new CurrentTimeEvent(startTime.getTime())); // set the initial start window for the historical data (external timer)
		events.forEach(event -> {
			engine.getEPRuntime().sendEvent(event);
		});
		engine.getEPRuntime().sendEvent(new CurrentTimeSpanEvent(startTime.getTime() + 10*60*1000, 100)); //set advance time for 10 minutes while sliding window each 100 ms;
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
	
	@Test public void testFinishedTcpConnections() {
		Assert.assertEquals(100,listener.getNetflowsOrdered().size());
	}
	
}
