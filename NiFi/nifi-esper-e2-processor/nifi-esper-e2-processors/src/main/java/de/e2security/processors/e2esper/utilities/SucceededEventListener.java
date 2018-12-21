package de.e2security.processors.e2esper.utilities;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.nifi.logging.ComponentLog;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import com.espertech.esper.event.map.MapEventBean;

import static de.e2security.processors.e2esper.utilities.EsperProcessorLogger.*;

public class SucceededEventListener implements UpdateListener {

	ComponentLog logger;

	public SucceededEventListener(ComponentLog logger) {
		this.logger = logger;
	}

	private AtomicReference<String> processedEvent = new AtomicReference<>();

	public String getProcessedEvent() {
		return processedEvent.get();
	}

	@Override
	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		for (EventBean event : newEvents) {
			if (event instanceof MapEventBean) { //expected all inbound events as map, otherwise exception
				Map<?,?> eventAsMap = (Map<?,?>) event.getUnderlying();
				String catchedEventAsMapEntry = eventAsMap.entrySet().toString();
				logger.debug(success("SUCCEEDED EVENT", catchedEventAsMapEntry));
				processedEvent.set(SupportUtility.transformEventMapToJson(eventAsMap));
			}
		}
	}

}
