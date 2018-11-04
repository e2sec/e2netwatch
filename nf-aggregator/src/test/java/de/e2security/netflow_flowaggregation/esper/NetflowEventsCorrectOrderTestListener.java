package de.e2security.netflow_flowaggregation.esper;

import java.time.ZonedDateTime;
import java.util.ArrayDeque;
import java.util.Queue;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

public class NetflowEventsCorrectOrderTestListener implements UpdateListener {
	
	private boolean stdout = false;
	
	NetflowEventsCorrectOrderTestListener(boolean stdout) {
		this.stdout = stdout;
	}

	private Queue<ZonedDateTime> dates = new ArrayDeque<>();
	
	@Override
	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		ZonedDateTime last_switched = (ZonedDateTime) newEvents[0].get("last_switched");
		if (stdout) System.out.println(last_switched.toString());
		dates.add(last_switched);
	}
	
	public Queue<ZonedDateTime> getDates() {
		return dates;
	}

}
