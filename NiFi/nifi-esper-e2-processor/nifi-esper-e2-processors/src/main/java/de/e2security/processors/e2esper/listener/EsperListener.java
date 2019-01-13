package de.e2security.processors.e2esper.listener;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.nifi.flowfile.FlowFile;
import org.apache.nifi.logging.ComponentLog;
import org.apache.nifi.processor.ProcessSession;
import org.apache.nifi.processor.ProcessSessionFactory;
import org.apache.nifi.processor.Relationship;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import com.espertech.esper.client.metric.MetricEvent;
import com.espertech.esper.event.map.MapEventBean;

import de.e2security.processors.e2esper.utilities.SupportUtility;

public class EsperListener implements UpdateListener {

	private ComponentLog logger;
	private ProcessSessionFactory sessionFactory;
	private Relationship rel;

	public EsperListener(ComponentLog logger, Relationship rel) {
		this.logger = logger;
		this.rel = rel;
	} 

	@Override
	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		ProcessSession session = this.sessionFactory.createSession();
		FlowFile file = session.create();
		EventBean event = newEvents[0];
		final AtomicReference<String> result = new AtomicReference<>();
		if (event instanceof MapEventBean) {
			Map<?,?> eventAsMap = (Map<?,?>) event.getUnderlying();
			 result.set(SupportUtility.transformEventMapToJson(eventAsMap));
			logger.debug("[" + result.get() + "]");	
		} else if (event.getUnderlying() instanceof MetricEvent) {
			String json = SupportUtility.transformMetricEventToJson((MetricEvent) event.getUnderlying());
			result.set(json);
		}
		if (result.get() != null) {
			file = session.write(file, (outStream) -> {
				outStream.write(result.get().getBytes());
			});
			session.getProvenanceReporter().route(file, rel);
			session.transfer(file, rel);
			session.commit();
		} else {
			session.remove(file);
		}
	}

	public void setSession(ProcessSessionFactory sessioFactory) {
		if (this.sessionFactory == null) {
			this.sessionFactory = sessioFactory;
		}
	}

}
