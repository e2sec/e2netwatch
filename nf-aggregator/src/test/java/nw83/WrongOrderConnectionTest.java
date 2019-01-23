package nw83;

import static org.junit.Assert.assertEquals;

import java.rmi.UnexpectedException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.espertech.esper.client.EPException;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.scopetest.SupportUpdateListener;
import com.espertech.esper.client.time.CurrentTimeEvent;

import de.e2security.netflow_flowaggregation.esper.NetflowEventEplExpressions;
import de.e2security.netflow_flowaggregation.esper.TcpEplExpressions;
import de.e2security.netflow_flowaggregation.esper.utils.EplExpressionTestSupporter;
import de.e2security.netflow_flowaggregation.esper.utils.EsperTestSupporter;
import de.e2security.netflow_flowaggregation.exceptions.NetflowEventException;
import de.e2security.netflow_flowaggregation.model.protocols.NetflowEvent;
import de.e2security.netflow_flowaggregation.model.protocols.NetflowEventOrdered;
import de.e2security.netflow_flowaggregation.model.protocols.ProtocolRegister;
import de.e2security.netflow_flowaggregation.utils.TestUtil;
import junit.framework.Assert;

public class WrongOrderConnectionTest extends EsperTestSupporter {
	
	@Test public void testSortByLastSwitchedEplStatementTimeAndOrderStmtOnSameLastSwitchedWithFirstFirstSwitchedOrder() throws NetflowEventException {
		NetflowEvent testEvent1 = new NetflowEvent();
		testEvent1.setLast_switched("2018-12-22T00:00:00.999Z");
		testEvent1.setFirst_switched("2018-12-21T23:59:00.999Z");
		NetflowEvent testEvent2 = new NetflowEvent();
		testEvent2.setLast_switched("2018-12-22T00:00:00.999Z");
		testEvent2.setFirst_switched("2018-12-21T23:59:10.999Z");
		EPStatement stmt = admin.createEPL("insert rstream into NetflowEventOrdered"
				+ " select rstream receivedTimeStamp"
				+ ",host"
				+ ",ipv4_src_addr"
				+ ",ipv4_dst_addr"
				+ ",l4_src_port"
				+ ",l4_dst_port"
				+ ",tcp_flags"
				+ ",protocol"
				+ ",version"
				+ ",flow_seq_num"
				+ ",flow_records"
				+ ",in_bytes"
				+ ",in_pkts"
				+ ",first_switched"
				+ ",last_switched"
				+ " from NetflowEvent#time(60 sec) order by last_switched, first_switched");
		AtomicReference<NetflowEventOrdered> neoReference = new AtomicReference<>();
		stmt.addListener((newEvents, oldEvents) ->  {
			NetflowEventOrdered neo = (NetflowEventOrdered) newEvents[0].getUnderlying();
			neoReference.compareAndSet(null, neo);
		});
		runtime.sendEvent(new CurrentTimeEvent(TestUtil.getCurrentTimeEvent("2018-12-22T00:00:00.999Z"))); //+ 60 seconds to lastSwitched
		runtime.sendEvent(testEvent2);
		runtime.sendEvent(testEvent1);
		runtime.sendEvent(new CurrentTimeEvent(TestUtil.getCurrentTimeEvent("2018-12-22T00:01:20.999Z")));
		Assert.assertEquals(testEvent1.convertToOrderedType().toString(), neoReference.get().toString());
	}
	
	@Test public void testSortByLastSwitchedEplStatementTimeAndOrderStmtOnSameLastSwitchedWithoutFirstSwitchedOrderShouldFail() throws NetflowEventException {
		NetflowEvent testEvent1 = new NetflowEvent();
		testEvent1.setLast_switched("2018-12-22T00:00:00.999Z");
		testEvent1.setFirst_switched("2018-12-21T23:59:00.999Z");
		NetflowEvent testEvent2 = new NetflowEvent();
		testEvent2.setLast_switched("2018-12-22T00:00:00.999Z");
		testEvent2.setFirst_switched("2018-12-21T23:59:10.999Z");
		EPStatement stmt = admin.createEPL("insert rstream into NetflowEventOrdered"
				+ " select rstream receivedTimeStamp"
				+ ",host"
				+ ",ipv4_src_addr"
				+ ",ipv4_dst_addr"
				+ ",l4_src_port"
				+ ",l4_dst_port"
				+ ",tcp_flags"
				+ ",protocol"
				+ ",version"
				+ ",flow_seq_num"
				+ ",flow_records"
				+ ",in_bytes"
				+ ",in_pkts"
				+ ",first_switched"
				+ ",last_switched"
				+ " from NetflowEvent#time(60 sec) order by last_switched");
		AtomicReference<NetflowEventOrdered> neoReference = new AtomicReference<>();
		stmt.addListener((newEvents, oldEvents) ->  {
			NetflowEventOrdered neo = (NetflowEventOrdered) newEvents[0].getUnderlying();
			neoReference.compareAndSet(null, neo);
		});
		runtime.sendEvent(new CurrentTimeEvent(TestUtil.getCurrentTimeEvent("2018-12-22T00:00:00.999Z"))); //+ 60 seconds to lastSwitched
		runtime.sendEvent(testEvent2);
		runtime.sendEvent(testEvent1);
		runtime.sendEvent(new CurrentTimeEvent(TestUtil.getCurrentTimeEvent("2018-12-22T00:01:20.999Z")));
		Assert.assertNotSame(testEvent1.convertToOrderedType().toString(), neoReference.get().toString());
	}
	
	@Test public void testSortByLastSwitchedEplStatementTimeAndOrderStmt() throws NetflowEventException {
		NetflowEvent testEvent1 = new NetflowEvent();
		testEvent1.setLast_switched("2018-12-22T00:00:00.999Z");
		testEvent1.setFirst_switched("2018-12-21T23:59:00.999Z");
		NetflowEvent testEvent2 = new NetflowEvent();
		testEvent2.setLast_switched("2018-12-22T00:00:20.999Z");
		testEvent2.setFirst_switched("2018-12-21T23:59:10.999Z");
		EPStatement stmt = admin.createEPL("insert rstream into NetflowEventOrdered"
				+ " select rstream receivedTimeStamp"
				+ ",host"
				+ ",ipv4_src_addr"
				+ ",ipv4_dst_addr"
				+ ",l4_src_port"
				+ ",l4_dst_port"
				+ ",tcp_flags"
				+ ",protocol"
				+ ",version"
				+ ",flow_seq_num"
				+ ",flow_records"
				+ ",in_bytes"
				+ ",in_pkts"
				+ ",first_switched"
				+ ",last_switched"
				+ " from NetflowEvent#time(60 sec) order by last_switched");
		AtomicReference<NetflowEventOrdered> neoReference = new AtomicReference<>();
		stmt.addListener((newEvents, oldEvents) ->  {
			NetflowEventOrdered neo = (NetflowEventOrdered) newEvents[0].getUnderlying();
			neoReference.compareAndSet(null, neo);
		});
		runtime.sendEvent(new CurrentTimeEvent(TestUtil.getCurrentTimeEvent("2018-12-22T00:00:00.999Z"))); //+ 60 seconds to lastSwitched
		runtime.sendEvent(testEvent2);
		runtime.sendEvent(testEvent1);
		runtime.sendEvent(new CurrentTimeEvent(TestUtil.getCurrentTimeEvent("2018-12-22T00:01:20.999Z")));
		Assert.assertEquals(testEvent1.convertToOrderedType().toString(), neoReference.get().toString());
	}
	
	@Test public void testSortByLastSwitchedEplStatement() throws NetflowEventException {
		NetflowEvent testEvent1 = new NetflowEvent();
		testEvent1.setLast_switched("2018-12-22T00:00:00.999Z");
		testEvent1.setFirst_switched("2018-12-21T23:59:00.999Z");
		NetflowEvent testEvent2 = new NetflowEvent();
		testEvent2.setLast_switched("2018-12-22T00:00:20.999Z");
		testEvent2.setFirst_switched("2018-12-21T23:59:10.999Z");
		EPStatement stmt = admin.createEPL(NetflowEventEplExpressions.eplSortByLastSwitched());
		AtomicReference<NetflowEventOrdered> neoReference = new AtomicReference<>();
		stmt.addListener((newEvents, oldEvents) ->  {
			NetflowEventOrdered neo = (NetflowEventOrdered) newEvents[0].getUnderlying();
			neoReference.compareAndSet(null, neo);
		});
		runtime.sendEvent(new CurrentTimeEvent(TestUtil.getCurrentTimeEvent("2018-12-22T00:00:00.999Z"))); //+ 60 seconds to lastSwitched
		runtime.sendEvent(testEvent2);
		runtime.sendEvent(testEvent1);
		runtime.sendEvent(new CurrentTimeEvent(TestUtil.getCurrentTimeEvent("2018-12-22T00:01:20.999Z")));
		Assert.assertEquals(testEvent1.convertToOrderedType().toString(), neoReference.get().toString());
	}
	
	@Test public void testSortByLastSwitchedEplStatementOnSameLastSwitchedValue() throws EPException, NetflowEventException {
		NetflowEvent testEvent1 = new NetflowEvent();
		testEvent1.setLast_switched("2018-12-22T00:00:00.999Z");
		testEvent1.setFirst_switched("2018-12-21T23:59:00.999Z");
		testEvent1.setProtocol(6);
		testEvent1.setTcp_flags(1);
		testEvent1.setHost("ident");
		testEvent1.setIpv4_dst_addr("hostB");
		testEvent1.setL4_src_port(9191);
		testEvent1.setIpv4_src_addr("hostA");
		testEvent1.setL4_dst_port(5353);
		NetflowEvent testEvent2 = new NetflowEvent();
		testEvent2.setLast_switched("2018-12-22T00:00:00.999Z");
		testEvent2.setFirst_switched("2018-12-21T23:59:10.999Z");
		testEvent2.setProtocol(6);
		testEvent2.setTcp_flags(1);
		testEvent2.setHost("ident");
		testEvent2.setIpv4_dst_addr("hostA");
		testEvent2.setIpv4_src_addr("hostB");
		testEvent2.setL4_src_port(5353);
		testEvent2.setL4_dst_port(9191);
		EPStatement ordered = admin.createEPL(NetflowEventEplExpressions.eplSortByLastSwitched());
		EPStatement finished = admin.createEPL(TcpEplExpressions.eplFinishedFlows());
		SupportUpdateListener supportListener = new SupportUpdateListener();
		finished.addListener(supportListener);
		
		ordered.addListener((newEvents,oldEvents) ->  {
			for (EventBean event : newEvents) {
				NetflowEventOrdered neo = (NetflowEventOrdered) event.getUnderlying();
				runtime.sendEvent(neo);
			}
		});
		
		runtime.sendEvent(new CurrentTimeEvent(TestUtil.getCurrentTimeEvent("2018-12-22T00:00:00.999Z")));
		runtime.sendEvent(testEvent1);
		runtime.sendEvent(testEvent2);
		runtime.sendEvent(new CurrentTimeEvent(TestUtil.getCurrentTimeEvent("2018-12-22T00:01:00.999Z")));

		runtime.sendEvent(new CurrentTimeEvent(TestUtil.getCurrentTimeEvent("2018-12-22T00:01:10.999Z")));
		runtime.sendEvent(new CurrentTimeEvent(TestUtil.getCurrentTimeEvent("2018-12-22T00:01:40.999Z")));

		Assert.assertTrue(!supportListener.getNewDataList().isEmpty());
		
		/*		Test fails in case:
		 * 		testEvent2.getLast_switched() == testEvent1.getLast_switched();
		 * 		and 		runtime.sendEvent(testEvent2);
							runtime.sendEvent(testEvent1);
				Reason: rightOrderChecker() in TcpEplExpression, cause in the case compared timestamps are equal, 
				esper sort the events by the time as they came into the engine
				//TODO: write custom Pattern Observer
		 */
	}
	
	
	/*
	 * reason of wrong order connection is that either client or server sends subsequent events event though the connection is finished or rejected;
	 * in this case we have a pair of events, which detected of esper (cep engine) as the right one for pattern matching. However the in-out order has been impared.  
	 *  |  |  |  (within 60 sec)
	 * a1 b2 a3  (where a1-b2 pair has been already detected by esper pattern);
	 * Purpose of testEsperBackwardEventProcessing() is to detect the drawbacks of such a case:
	 * e.g. whether only b2-a3 pair or also a1-b2 pair will be processed by esper engine 
	 */
	
	private static final Logger LOG = LoggerFactory.getLogger(WrongOrderConnectionTest.class);

	private NetflowEventOrdered neo1 = null;
	private NetflowEventOrdered neo2 = null;
	private NetflowEventOrdered neo3 = null;
	
	//since @Before has been already used in generic super class EsperTestSupporter
	private void createAndSendGenericEvents() throws NetflowEventException {
		NetflowEvent testEvent1 = new NetflowEvent();
		testEvent1.setLast_switched("2018-12-22T00:00:00.999Z");
		testEvent1.setFirst_switched("2018-12-21T23:59:00.999Z");
		NetflowEvent testEvent2 = new NetflowEvent();
		testEvent2.setLast_switched("2018-12-22T00:00:10.999Z");
		testEvent2.setFirst_switched("2018-12-21T23:59:10.999Z");
		NetflowEvent testEvent3 = new NetflowEvent(); 
		testEvent3.setLast_switched("2018-12-22T00:00:20.999Z"); 
		testEvent3.setFirst_switched("2018-12-21T23:59:00.999Z");
		//convert to ordered class to imply a most real case and eliminate a possible confuse
		neo1 = testEvent1.convertToOrderedType();
		neo2 = testEvent2.convertToOrderedType();
		neo3 = testEvent3.convertToOrderedType();
		runtime.sendEvent(new CurrentTimeEvent(TestUtil.getCurrentTimeEvent(neo1.getLast_switched())));
		runtime.sendEvent(neo1);
		runtime.sendEvent(neo2);
		runtime.sendEvent(neo3);
		testEvent1.setLast_switched("2018-12-22T00:00:00.999Z");
		testEvent1.setFirst_switched("2018-12-21T23:59:00.999Z");
		testEvent1.setProtocol(6);

	}
	
	//assuming the following pairs will be created: a1-b2, b2-a3 that results in the problem with wrong connection order
	@Test public void testEsperBackwardEventProcessingExpectingEsperIncludesPatternMatchForTheSameEventMoreThanOnce() throws UnexpectedException, NetflowEventException {
		Queue<ProtocolRegister> resultList = new ArrayDeque<>();
		EPStatement selectStmt = admin.createEPL("insert into ProtocolRegister select a.last_switched as in_last_switched, b.last_switched as out_last_switched "
				+ "from pattern [every a=NetflowEventOrdered -> b=NetflowEventOrdered where timer:within(60 sec)]");
		SupportUpdateListener supportListener = new SupportUpdateListener();
		selectStmt.addListener(supportListener);
		selectStmt.addListener( (newEvent, oldEvent) ->  {
			resultList.add((ProtocolRegister) newEvent[0].getUnderlying());
		});
		
		createAndSendGenericEvents();
		runtime.sendEvent(new CurrentTimeEvent(TestUtil.getCurrentTimeEvent(neo1.getLast_switched()) + 60000));

		assertEquals(resultList.size(),2);
		assertEquals(neo1.getLast_switched(),resultList.peek().getIn_last_switched());
		assertEquals(neo2.getLast_switched(),resultList.poll().getOut_last_switched());
		assertEquals(neo2.getLast_switched(),resultList.peek().getIn_last_switched());
		assertEquals(neo3.getLast_switched(),resultList.poll().getOut_last_switched());
		
	}
	
	// Esper BEHAVIOURAL test. Assertion based on the correct result of the test self and not on the expectation;
	// assuming the 'every'-keyword elimination doesn't result in the expected behaviour (even though should be tested)
	@Test public void esperPatternBehaviourWithoutEveryKeywordDonotBringExpectedResult() throws NetflowEventException {
		Queue<ProtocolRegister> resultList = new ArrayDeque<>();
		EPStatement selectStmt = admin.createEPL("insert into ProtocolRegister select a.last_switched as in_last_switched, b.last_switched as out_last_switched "
				+ "from pattern [a=NetflowEventOrdered -> b=NetflowEventOrdered where timer:within(60 sec)]");
		selectStmt.addListener( (newEvent, oldEvent) ->  {
			resultList.add((ProtocolRegister) newEvent[0].getUnderlying());
		});
		createAndSendGenericEvents();
		
		NetflowEvent testEvent4 = new NetflowEvent(); //(first_switched_as_long < a.first_switched_as_long)
		testEvent4.setLast_switched("2018-12-22T00:00:30.999Z");
		testEvent4.setFirst_switched("2018-12-21T23:59:20.999Z");
		NetflowEventOrdered neo4 = testEvent4.convertToOrderedType();
		runtime.sendEvent(neo4);
		runtime.sendEvent(new CurrentTimeEvent(TestUtil.getCurrentTimeEvent(neo1.getLast_switched()) + 60000));
		Assert.assertTrue(resultList.size() != 2); //adding the fourth even event could be resulted in two pairs regarding implemented epl pattern w/o 'every' keyword.
	}
	
	@Test public void testEsperPatternConditionalOnFirstSwitched() throws NetflowEventException {
		Queue<ProtocolRegister> resultList = new ArrayDeque<>();
		EPStatement selectStmt = admin.createEPL("insert into ProtocolRegister select a.last_switched as in_last_switched, b.last_switched as out_last_switched "
				+ "from pattern [every a=NetflowEventOrdered -> b=NetflowEventOrdered(first_switched_as_long > a.first_switched_as_long) where timer:within(60 sec)]");
		selectStmt.addListener( (newEvent, oldEvent) ->  {
			resultList.add((ProtocolRegister) newEvent[0].getUnderlying());
		});
		createAndSendGenericEvents();
		runtime.sendEvent(new CurrentTimeEvent(TestUtil.getCurrentTimeEvent(neo1.getLast_switched()) + 60000));
		assertEquals(1, resultList.size());
		//asserting only a1-b2 pair will be built
		assertEquals(neo1.getLast_switched(),resultList.peek().getIn_last_switched());
		assertEquals(neo2.getLast_switched(),resultList.poll().getOut_last_switched());
	}
	
	/*   1	   2	 3	   4	 5	   6	 7	   8	 9
	 *   |     |     |     |     |     |     |     |     |  -> correlated events (a1,b1), (c1,d1);  
	 * a1(1) b1(1) c1(1) b1(2) a1(2) b1(3) a1(3) c1(2) d1(1)
	 * 
	 */
	@Test public void testEsperPatternConditionalOnFirstSwitchedWithinFinishedTcpPattern() throws NetflowEventException {
		Queue<ProtocolRegister> resultList = new ArrayDeque<>();
		EPStatement selectStmt = admin.createEPL("insert into ProtocolRegister select a.last_switched as in_last_switched, b.last_switched as out_last_switched "
				+ "from pattern ["
				+ 		"every a=NetflowEventOrdered(protocol=6 and (tcp_flags&1)=1) "
				+ 		"-> b=NetflowEventOrdered(protocol=6 and (tcp_flags&1)=1 and first_switched_as_long > a.first_switched_as_long " 
				+ 		"and l4_dst_port = a.l4_src_port and l4_src_port = a.l4_dst_port" 
				+ 		") where timer:within(60 sec)"
				+ "]");
		selectStmt.addListener( (newEvent, oldEvent) ->  {
			resultList.add((ProtocolRegister) newEvent[0].getUnderlying());
		});
		NetflowEvent testEvent1 = new NetflowEvent();
		testEvent1.setLast_switched("2018-12-22T00:00:00.999Z");		
		testEvent1.setFirst_switched("2018-12-21T23:59:00.999Z");
		testEvent1.setProtocol(6);
		testEvent1.setTcp_flags(1);
		testEvent1.setL4_src_port(80);
		testEvent1.setL4_dst_port(5353);
		NetflowEvent testEvent2 = new NetflowEvent();
		testEvent2.setLast_switched("2018-12-22T00:00:10.999Z");
		testEvent2.setFirst_switched("2018-12-21T23:59:10.999Z");
		testEvent2.setProtocol(6);
		testEvent2.setTcp_flags(1);
		testEvent2.setL4_src_port(5353);
		testEvent2.setL4_dst_port(80);
		NetflowEvent testEvent3 = new NetflowEvent(); 
		testEvent3.setLast_switched("2018-12-22T00:00:11.999Z"); 
		testEvent3.setFirst_switched("2018-12-21T23:59:11.999Z");
		testEvent3.setProtocol(6);
		testEvent3.setTcp_flags(1);
		testEvent3.setL4_src_port(5453);
		testEvent3.setL4_dst_port(8180);
		NetflowEvent testEvent4 = new NetflowEvent(); //(first_switched_as_long < a.first_switched_as_long)
		testEvent4.setLast_switched("2018-12-22T00:00:30.999Z");
		testEvent4.setFirst_switched("2018-12-21T23:59:10.999Z");
		testEvent4.setProtocol(6);
		testEvent4.setTcp_flags(0);
		testEvent4.setL4_src_port(5455);
		testEvent4.setL4_dst_port(8180);
		NetflowEvent testEvent5 = new NetflowEvent();
		testEvent5.setLast_switched("2018-12-22T00:00:35.999Z");
		testEvent5.setFirst_switched("2018-12-21T23:59:00.999Z");
		testEvent5.setProtocol(6);
		testEvent5.setTcp_flags(1);
		testEvent5.setL4_src_port(4567);
		testEvent5.setL4_dst_port(9443);
		NetflowEvent testEvent6 = new NetflowEvent();
		testEvent6.setLast_switched("2018-12-22T00:00:36.999Z");
		testEvent6.setFirst_switched("2018-12-21T23:59:10.999Z");
		testEvent6.setProtocol(6);
		testEvent6.setTcp_flags(1);
		testEvent6.setL4_src_port(9443);
		testEvent6.setL4_dst_port(4567);
		NetflowEvent testEvent7 = new NetflowEvent();
		testEvent7.setLast_switched("2018-12-22T00:00:38.999Z");		
		testEvent7.setFirst_switched("2018-12-21T23:59:00.999Z");
		testEvent7.setProtocol(6);
		testEvent7.setTcp_flags(0);
		testEvent7.setL4_src_port(9991);
		testEvent7.setL4_dst_port(5353);
		NetflowEvent testEvent8 = new NetflowEvent(); 
		testEvent8.setLast_switched("2018-12-22T00:00:40.999Z"); 
		testEvent8.setFirst_switched("2018-12-21T23:59:11.999Z");
		testEvent8.setProtocol(17);
		testEvent8.setTcp_flags(0);
		testEvent8.setL4_src_port(9443);
		testEvent8.setL4_dst_port(5353);
		NetflowEvent testEvent9 = new NetflowEvent(); 
		testEvent9.setLast_switched("2018-12-22T00:00:41.999Z"); 
		testEvent9.setFirst_switched("2018-12-21T23:59:20.999Z");
		testEvent9.setProtocol(6);
		testEvent9.setTcp_flags(1);
		testEvent9.setL4_src_port(8180);
		testEvent9.setL4_dst_port(5453);
		NetflowEventOrdered neo1 = testEvent1.convertToOrderedType();
		NetflowEventOrdered neo2 = testEvent2.convertToOrderedType();
		NetflowEventOrdered neo3 = testEvent3.convertToOrderedType();
		NetflowEventOrdered neo4 = testEvent4.convertToOrderedType();
		NetflowEventOrdered neo5 = testEvent5.convertToOrderedType();
		NetflowEventOrdered neo6 = testEvent6.convertToOrderedType();
		NetflowEventOrdered neo7 = testEvent7.convertToOrderedType();
		NetflowEventOrdered neo8 = testEvent8.convertToOrderedType();
		NetflowEventOrdered neo9 = testEvent9.convertToOrderedType();
		runtime.sendEvent(new CurrentTimeEvent(TestUtil.getCurrentTimeEvent(neo1.getLast_switched())));
		runtime.sendEvent(neo1);
		runtime.sendEvent(neo2);
		runtime.sendEvent(neo3);
		runtime.sendEvent(neo4);
		runtime.sendEvent(neo5);
		runtime.sendEvent(neo6);
		runtime.sendEvent(neo7);
		runtime.sendEvent(neo8);
		runtime.sendEvent(neo9);
		runtime.sendEvent(new CurrentTimeEvent(TestUtil.getCurrentTimeEvent(neo1.getLast_switched()) + 60000));
		assertEquals(3,resultList.size());
		assertEquals(neo1.getLast_switched(),resultList.peek().getIn_last_switched());
		assertEquals(neo2.getLast_switched(),resultList.poll().getOut_last_switched());
		assertEquals(neo5.getLast_switched(),resultList.peek().getIn_last_switched());
		assertEquals(neo6.getLast_switched(),resultList.poll().getOut_last_switched());
		assertEquals(neo3.getLast_switched(),resultList.peek().getIn_last_switched());
		assertEquals(neo9.getLast_switched(),resultList.poll().getOut_last_switched());
	}
}
