package de.e2security.netflow_flowaggregation.esper;

import java.time.ZonedDateTime;
import java.util.ArrayDeque;
import java.util.Queue;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

import de.e2security.netflow_flowaggregation.netflow.NetflowEventOrdered;

public class NetflowEventsCorrectOrderTestListener implements UpdateListener {
	
	private boolean stdout = false;
	
	NetflowEventsCorrectOrderTestListener(boolean stdout) {
		this.stdout = stdout;
	}

	private Queue<ZonedDateTime> dates = new ArrayDeque<>();
	private Queue<NetflowEventOrdered> netflowsOrdered = new ArrayDeque<>();
	
	@Override
	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		NetflowEventOrdered event = (NetflowEventOrdered) newEvents[0].getUnderlying();
		ZonedDateTime last_switched = (ZonedDateTime) event.getLast_switched();
		if (stdout) System.out.println(last_switched.toString());
		dates.add(last_switched);
		netflowsOrdered.add(event);
	}
	
	public Queue<ZonedDateTime> getDates() {
		return dates;
	}
	
	public Queue<NetflowEventOrdered> getNetflowsOrdered() {
		return netflowsOrdered;
	}

}
