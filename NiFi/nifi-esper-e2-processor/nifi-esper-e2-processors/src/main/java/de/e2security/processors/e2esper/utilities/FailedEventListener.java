package de.e2security.processors.e2esper.utilities;

import org.apache.nifi.logging.ComponentLog;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UnmatchedListener;

public class FailedEventListener implements UnmatchedListener {
	
	ComponentLog logger;
	
	public FailedEventListener(ComponentLog logger) {
		this.logger = logger;
	}

	@Override
	public void update(EventBean theEvent) {
		String failedEv = theEvent.getUnderlying().toString();
		logger.debug("[ESPER DEBUG][UNMATCHED EVENT]: " + "esper couldn't process the following event: " + failedEv);
	}

}
