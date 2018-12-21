package de.e2security.processors.e2esper.utilities;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.nifi.logging.ComponentLog;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UnmatchedListener;
import com.espertech.esper.event.map.MapEventBean;

import static de.e2security.processors.e2esper.utilities.EsperProcessorLogger.*;

public class UnmatchedEventListener implements UnmatchedListener {
	
	ComponentLog logger;
	
	public UnmatchedEventListener(ComponentLog logger) {
		this.logger = logger;
	}
	
	private AtomicReference<String> unmatchedEventAtomic = new AtomicReference<>();
	
	public String getUnmatchedEvent() {
		return unmatchedEventAtomic.get();
	}

	@Override
	public void update(EventBean event) {
		if (event instanceof MapEventBean) {
			Map<?,?> eventAsMap = (Map<?,?>) event.getUnderlying();
			String catchedEventAsMapEntry = eventAsMap.entrySet().toString();
			logger.debug(success("UNMATCHED EVENT", catchedEventAsMapEntry));
			unmatchedEventAtomic.set(SupportUtility.transformEventMapToJson(eventAsMap));
		}
		String unmatchedEvent = event.getUnderlying().toString();
		logger.debug(success("UNMATCHED EVENT AS STRING", unmatchedEvent));
		unmatchedEventAtomic.set(unmatchedEvent);
	}

}
