package de.e2security.processors.e2esper.utilities;

import java.util.concurrent.atomic.AtomicReference;

import org.apache.nifi.logging.ComponentLog;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UnmatchedListener;

import static de.e2security.processors.e2esper.utilities.EsperProcessorLogger.*;

public class FailedEventListener implements UnmatchedListener {
	
	ComponentLog logger;
	
	private AtomicReference<String> unmatchedEventAtomic = new AtomicReference<>();
	
	public FailedEventListener(ComponentLog logger) {
		this.logger = logger;
	}

	@Override
	public void update(EventBean theEvent) {
		String unmatchedEvent = theEvent.getUnderlying().toString();
		logger.debug(success("UNMATCHED EVENT", unmatchedEvent));
		unmatchedEventAtomic.set(unmatchedEvent);
	}

}
