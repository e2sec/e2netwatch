package nw83;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.rmi.UnexpectedException;
import java.util.ArrayDeque;
import java.util.Queue;

import org.junit.Test;

import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.scopetest.SupportUpdateListener;
import com.espertech.esper.client.time.CurrentTimeEvent;

import de.e2security.netflow_flowaggregation.esper.utils.EsperTestSupporter;
import de.e2security.netflow_flowaggregation.model.protocols.NetflowEvent;
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
	@Test public void testEsperBackwardEventProcessing() throws UnexpectedException {
		Queue<ProtocolRegister> resultList = new ArrayDeque<>();
		NetflowEvent testEvent1 = new NetflowEvent();
		testEvent1.setLast_switched("2018-12-22T00:00:00.999Z");
		NetflowEvent testEvent2 = new NetflowEvent();
		testEvent2.setLast_switched("2018-12-22T00:00:10.999Z");
		NetflowEvent testEvent3 = new NetflowEvent(); 
		testEvent3.setLast_switched("2018-12-22T00:00:20.999Z");
		
		EPStatement selectStmt = admin.createEPL("insert into ProtocolRegister select a.last_switched as in_last_switched, b.last_switched as out_last_switched "
				+ "from pattern [every a=NetflowEvent -> b=NetflowEvent where timer:within(60 sec)]");
		SupportUpdateListener supportListener = new SupportUpdateListener();
		selectStmt.addListener(supportListener);
		selectStmt.addListener( (newEvent, oldEvent) ->  {
			resultList.add((ProtocolRegister) newEvent[0].getUnderlying());
		});
		runtime.sendEvent(new CurrentTimeEvent(TestUtil.getCurrentTimeEvent(testEvent1.getLast_switched())));
		runtime.sendEvent(testEvent1);
		runtime.sendEvent(testEvent2);
		runtime.sendEvent(testEvent3);
		runtime.sendEvent(new CurrentTimeEvent(TestUtil.getCurrentTimeEvent(testEvent1.getLast_switched()) + 60000));
		int numberOfEvents = supportListener.getNewDataList().size();
		if ( numberOfEvents > 1) {
			//then expect that the following pairs has been created: a1-b2, b2-a3
			if (resultList.size() == numberOfEvents) {
				assertEquals(testEvent1.getLast_switched(),resultList.peek().getIn_last_switched());
				assertEquals(testEvent2.getLast_switched(),resultList.poll().getOut_last_switched());
				assertEquals(testEvent2.getLast_switched(),resultList.peek().getIn_last_switched());
				assertEquals(testEvent3.getLast_switched(),resultList.poll().getOut_last_switched());
				assertEquals("The conditional check has to be implemented, since we has defined, that both event pairs are coming out as result of pattern matching."
						+ "Can be achieved during additional class attribute and it's check by CustomKafkaProducer", 0, resultList.size());
			} else {
				throw new UnexpectedException("numberOfEvents doesn't equal resultList");
			}
		} else {
			Assert.assertEquals("Esper builds just one pair and ignore utilization of the event has been matched within pattern. "
					+ "Thus, fliping of events in case of afterwards sent ones (after the connection has been rejected or finished) should be implemented",1, numberOfEvents);
		}
	}
}
