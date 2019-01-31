package de.e2security.netflow_flowaggregation.esper;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.junit.Test;

import com.espertech.esper.client.EPException;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.scopetest.SupportUpdateListener;
import com.espertech.esper.client.time.CurrentTimeEvent;
import com.espertech.esper.client.time.CurrentTimeSpanEvent;

import de.e2security.netflow_flowaggregation.esper.epl.TcpEplExpressions;
import de.e2security.netflow_flowaggregation.esper.utils.EplExpressionTestSupporter;
import de.e2security.netflow_flowaggregation.esper.utils.EsperTestSupporter;
import de.e2security.netflow_flowaggregation.exceptions.NetflowEventException;
import de.e2security.netflow_flowaggregation.model.protocols.NetflowEvent;
import de.e2security.netflow_flowaggregation.model.protocols.NetflowEventOrdered;
import de.e2security.netflow_flowaggregation.utils.TestUtil;

public class TestTcpEplExpressionsWithManualEventsInjection extends EsperTestSupporter {
	
	static NetflowEventsCorrectOrderTestListener listener = new NetflowEventsCorrectOrderTestListener(false); //static in order to use over the tests

	//test code during manual injection of finished connections
	@Test public void finishedTcpConnectionIsolatedTest() throws EPException, NetflowEventException {
		NetflowEvent event1 = new NetflowEvent();
		event1.setLast_switched("2018-11-03T12:50:12.999Z");
		event1.setTcp_flags(19);
		event1.setHost("host");
		event1.setProtocol(6);
		event1.setIpv4_src_addr("172.22.0.5");
		event1.setL4_dst_port(22484);
		event1.setFirst_switched("2018-11-03T12:50:12.999Z");
		event1.setIpv4_dst_addr("90.130.70.73");
		event1.setL4_src_port(34304);
		NetflowEvent event2 = new NetflowEvent();
		event2.setLast_switched("2018-11-03T12:50:12.999Z");
		event2.setTcp_flags(27);
		event2.setHost("host");
		event2.setProtocol(6);
		event2.setIpv4_src_addr("90.130.70.73");
		event2.setL4_dst_port(34304);
		event2.setFirst_switched("2018-11-03T12:50:13.999Z");
		event2.setIpv4_dst_addr("172.22.0.5");
		event2.setL4_src_port(22484);
		SupportUpdateListener supportListener = new SupportUpdateListener();
		int numberOfEvents = 2;
		List<NetflowEvent> eventsList = getHistoricalEvents(TestUtil.readSampleDataFile("netflow_ordered_finished.sample"), numberOfEvents);
		Pair<Long,Long> timer = getTimeFrameForCurrentTimer(eventsList);
		int window = 100;
		EPStatement filterStmt = admin.createEPL(TcpEplExpressions.eplFinishedFlows());
		EPStatement selectStmt = admin.createEPL(EplExpressionTestSupporter.selectTcpConnections());
		selectStmt.addListener(supportListener);
		runtime.sendEvent(new CurrentTimeEvent(timer.getKey()));
		/*
		 * since the epl statement stay unchanged we have to work with event type classes which are used in ep statements;
		 * e.g. NetflowEventOrdered instead of NetflowEvent
		 */
		runtime.sendEvent(event1.convertToOrderedType());
		runtime.sendEvent(event2.convertToOrderedType());
		runtime.sendEvent(new CurrentTimeSpanEvent(timer.getValue(), window));
		/*
		 * divide through 2 cause finished tcp connections are saved in TcpConnection instances contained of two events (in_/out_)
		 */
		Assert.assertEquals(numberOfEvents / 2, supportListener.getNewDataList().size());
	}
}
