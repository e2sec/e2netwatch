package de.e2security.processors.e2esper.utilities;

import static de.e2security.processors.e2esper.utilities.EsperProcessorLogger.success;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.nifi.logging.ComponentLog;
import org.apache.nifi.processor.Relationship;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import com.espertech.esper.event.map.MapEventBean;

public class SucceededEventListener implements UpdateListener {

	ComponentLog logger;
	Relationship relationship;
	AtomicReference<Optional<Pair<String,Relationship>>> resultReference;
	
	public SucceededEventListener(ComponentLog logger, Relationship rel, AtomicReference<Optional<Pair<String,Relationship>>> finalResult) {
		this.logger = logger;
		this.relationship = rel;
		this.resultReference = finalResult;
	}

	@Override
	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		for (EventBean event : newEvents) {
			if (event instanceof MapEventBean) { //expected all inbound events as map, otherwise exception
				Map<?,?> eventAsMap = (Map<?,?>) event.getUnderlying();
				String catchedEventAsMapEntry = eventAsMap.entrySet().toString();
				logger.debug(success("SUCCEEDED EVENT", catchedEventAsMapEntry));
				resultReference.set(Optional.of(new ImmutablePair<>(SupportUtility.transformEventMapToJson(eventAsMap),relationship)));
			}
		}
	}

}
