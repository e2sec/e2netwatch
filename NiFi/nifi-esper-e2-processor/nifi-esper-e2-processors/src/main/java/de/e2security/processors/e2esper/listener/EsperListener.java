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

	private final ComponentLog logger;
	private AtomicReference<ProcessSessionFactory> sessionFactory;
	private final Relationship rel;

	public EsperListener(ComponentLog logger, Relationship rel) {
		this.logger = logger;
		this.rel = rel;
		this.sessionFactory = new AtomicReference<>();
	} 

	@Override
	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		final ProcessSession session = this.sessionFactory.get().createSession();
		final AtomicReference<FlowFile> file = new AtomicReference<>(session.create());
		final EventBean event = newEvents[0];
		final AtomicReference<String> result = new AtomicReference<>();
		if (event instanceof MapEventBean) {
			final Map<?,?> eventAsMap = (Map<?,?>) event.getUnderlying();
			result.set(SupportUtility.transformEventMapToJson(eventAsMap));
			logger.debug("[" + result.get() + "]");	
		} else if (event.getUnderlying() instanceof MetricEvent) {
			final String json = SupportUtility.transformMetricEventToJson((MetricEvent) event.getUnderlying());
			result.set(json);
		}
		if (result.get() != null) {
			file.set(session.write(file.get(), (outStream) -> {
				outStream.write(result.get().getBytes());
			}));
			session.getProvenanceReporter().route(file.get(), rel);
			session.transfer(file.get(), rel);
			session.commit();
		} else {
			session.remove(file.get());
		}
	}

	public void setSession(ProcessSessionFactory sessionFactory) {
		this.sessionFactory.compareAndSet(null, sessionFactory);
	}

}
