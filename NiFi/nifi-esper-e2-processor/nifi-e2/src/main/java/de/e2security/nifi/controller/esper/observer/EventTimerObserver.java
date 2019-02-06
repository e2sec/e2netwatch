package de.e2security.nifi.controller.esper.observer;

import java.util.List;
import java.util.Map;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.event.map.MapEventBean;
import com.espertech.esper.filterspec.MatchedEventMap;
import com.espertech.esper.pattern.observer.EventObserver;
import com.espertech.esper.pattern.observer.EventObserverVisitor;
import com.espertech.esper.pattern.observer.ObserverEventEvaluator;

public class EventTimerObserver implements EventObserver {

	MatchedEventMap events;
	ObserverEventEvaluator evaluator;
	int timeDiscrepancy;
	String timeField;
	
	public EventTimerObserver(MatchedEventMap events, ObserverEventEvaluator evaluator, List<Object> parameters) {
		this.events = events;
		this.evaluator = evaluator;
		timeDiscrepancy = (int) parameters.get(0);
		timeField = (String) parameters.get(1);
	}

	@Override
	public void startObserve() {
		int size = events.getMatchingEvents().length;
		if (size != 2) 
			throw new RuntimeException("complex event pattern used; timer:internal can be set only for pattern with two events");
		int timestampDelta = 0;
		for (Object obj : events.getMatchingEvents()) {
			if (obj instanceof MapEventBean) {
				Map<?,?> eventAsMap = (Map<?,?>) ((EventBean) obj).getUnderlying();
				Number eventTimestamp = null;
				try { 
					eventTimestamp = (Number) eventAsMap.get(timeField);
					timestampDelta = eventTimestamp.intValue() - timestampDelta;
				} catch (ClassCastException cce) { 
					cce.printStackTrace();
				}
			}
		}
		timestampDelta = Math.abs(timestampDelta);
		if (timestampDelta >= timeDiscrepancy) {
				evaluator.observerEvaluateTrue(events, true);
			} else {
				evaluator.observerEvaluateFalse(false);
		}

	}

	@Override
	public void stopObserve() {
	}

	@Override
	public void accept(EventObserverVisitor visitor) {
	}

	@Override
	public MatchedEventMap getBeginState() {
		return events;
	}

}
