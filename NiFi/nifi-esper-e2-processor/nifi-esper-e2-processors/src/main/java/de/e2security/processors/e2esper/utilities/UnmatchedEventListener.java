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
import com.espertech.esper.client.UnmatchedListener;
import com.espertech.esper.event.map.MapEventBean;

public class UnmatchedEventListener implements UnmatchedListener {
	
	ComponentLog logger;
	Relationship relationship;
	AtomicReference<Optional<Pair<String,Relationship>>> resultReference;
	
	public UnmatchedEventListener(ComponentLog logger, Relationship relationship, AtomicReference<Optional<Pair<String,Relationship>>> finalResult) {
		this.logger = logger;
		this.relationship = relationship;
		this.resultReference = finalResult;
	}

	@Override
	public void update(EventBean event) {
		if (event instanceof MapEventBean) {
			Map<?,?> eventAsMap = (Map<?,?>) event.getUnderlying();
			String catchedEventAsMapEntry = eventAsMap.entrySet().toString();
			logger.debug(success("UNMATCHED EVENT", catchedEventAsMapEntry));
			resultReference.set(Optional.of(new ImmutablePair<>(SupportUtility.transformEventMapToJson(eventAsMap), relationship)));
			}
	}

}
