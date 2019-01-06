package de.e2security.nifi.controller.esper.observer;

import java.util.List;

import com.espertech.esper.epl.expression.core.ExprNode;
import com.espertech.esper.epl.expression.core.ExprValidationContext;
import com.espertech.esper.filterspec.MatchedEventMap;
import com.espertech.esper.pattern.EvalStateNodeNumber;
import com.espertech.esper.pattern.MatchedEventConvertor;
import com.espertech.esper.pattern.PatternAgentInstanceContext;
import com.espertech.esper.pattern.PatternExpressionUtil;
import com.espertech.esper.pattern.observer.EventObserver;
import com.espertech.esper.pattern.observer.ObserverEventEvaluator;
import com.espertech.esper.pattern.observer.ObserverFactorySupport;
import com.espertech.esper.pattern.observer.ObserverParameterException;

/**
 * usage: PATTERN[ event.a -> event.b -> timer:event(x,y), where x is time in seconds (type int), y is name of time field;
 * logic: calculate delta of both events (Math.abs(a.y - b.y) >= x)
 * tests: nifi-esper-e2-processors/src/test/java/nw102/TestCustomTimerPatternObserver.java
 */

public class EventTimerObserverFactory extends ObserverFactorySupport {
	
	private List<ExprNode> parameters;
	private MatchedEventConvertor convertor;

	@Override
	public void setObserverParameters(List<ExprNode> observerParameters, 
									  MatchedEventConvertor convertor, 
									  ExprValidationContext validationContext) throws ObserverParameterException {
		if (observerParameters.size() != 2) //time_discrepancy, time_field
			throw new ObserverParameterException("PARAMETERS: [time_discrepancy] in seconds (type int), [event's attribute] by which time discrepancy should be compared"); 
		this.parameters = observerParameters;
		this.convertor = convertor;
	}

	@Override
	public EventObserver makeObserver(PatternAgentInstanceContext context, 
									  MatchedEventMap events, 
									  ObserverEventEvaluator observerEventEvaluator, 
									  EvalStateNodeNumber stateNodeId, 
									  Object observerState,
									  boolean isFilterChildNonQuitting) {
		List<Object> objects = PatternExpressionUtil.evaluate("EventTimerObserver", events, parameters, convertor, null);
		return new EventTimerObserver(events, observerEventEvaluator, objects);
	}

	@Override
	public boolean isNonRestarting() {
		return false;
	}
	
}
