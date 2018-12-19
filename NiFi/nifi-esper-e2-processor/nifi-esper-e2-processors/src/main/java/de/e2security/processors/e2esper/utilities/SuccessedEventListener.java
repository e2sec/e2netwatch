package de.e2security.processors.e2esper.utilities;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.nifi.logging.ComponentLog;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import com.espertech.esper.event.map.MapEventBean;

public class SuccessedEventListener implements UpdateListener {
	
	ComponentLog logger;
	
	public SuccessedEventListener(ComponentLog logger) {
		this.logger = logger;
	}
	
	private AtomicReference<String> processedEvent = new AtomicReference<>();
	
	public AtomicReference<String> getProcessedEvent() {
		return processedEvent;
	}
	
	@Override
	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		try {
			for (EventBean event : newEvents) {
				if (event instanceof MapEventBean) {
					String catchedEventAsMapEntry = ((Map<?,?>) event.getUnderlying()).entrySet().toString();
					logger.debug("[ESPER DEBUG]: " + "esper listener has sucessfully parsed the following event: " + catchedEventAsMapEntry);
					processedEvent.set(catchedEventAsMapEntry);
				}
			}
		} catch (Exception ex) {
			logger.error("[ESPER ERROR]: " + "UpdateListener couldnt't read underlying object -> see the stacktrace..." );
			ex.printStackTrace();
		}
	}

}
