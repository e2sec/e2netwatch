package nw83;

import static org.junit.Assert.assertEquals;

import java.rmi.UnexpectedException;
import java.util.ArrayDeque;
import java.util.Queue;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.scopetest.SupportUpdateListener;
import com.espertech.esper.client.time.CurrentTimeEvent;

import de.e2security.netflow_flowaggregation.esper.utils.EsperTestSupporter;
import de.e2security.netflow_flowaggregation.exceptions.NetflowEventException;
import de.e2security.netflow_flowaggregation.model.protocols.NetflowEvent;
import de.e2security.netflow_flowaggregation.model.protocols.NetflowEventOrdered;
import de.e2security.netflow_flowaggregation.model.protocols.ProtocolRegister;
import de.e2security.netflow_flowaggregation.utils.TestUtil;
import junit.framework.Assert;

public class WrongOrderConnectionTest extends EsperTestSupporter{
	
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
	@Test public void testEsperPatternBehaviourWithouEveryKeywordDonnoResult() throws NetflowEventException {
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
		EPStatement selectStmt = admin.createEPL("@Name('EsperDebugger') @Audit insert into ProtocolRegister select a.last_switched as in_last_switched, b.last_switched as out_last_switched "
				+ "from pattern [a=NetflowEventOrdered -> b=NetflowEventOrdered(first_switched_as_long < a.first_switched_as_long) where timer:within(60 sec)]");
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
}
