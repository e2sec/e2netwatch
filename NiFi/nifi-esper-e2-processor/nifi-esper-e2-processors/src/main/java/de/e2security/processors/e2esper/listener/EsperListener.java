package de.e2security.processors.e2esper.listener;

import java.util.Map;

import org.apache.nifi.flowfile.FlowFile;
import org.apache.nifi.logging.ComponentLog;
import org.apache.nifi.processor.ProcessSession;
import org.apache.nifi.processor.Relationship;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import com.espertech.esper.event.map.MapEventBean;

import de.e2security.processors.e2esper.utilities.SupportUtility;

public class EsperListener implements UpdateListener {
	
	ComponentLog logger;
	String name;
	FlowFile file;
	ProcessSession session;
	Relationship rel;
	
	public EsperListener(ComponentLog logger, String stmtName, ProcessSession session, Relationship rel) {
		this.logger = logger;
		this.name = stmtName;
		this.session = session;
		this.file = session.create();
		this.rel = rel;
	} 

	@Override
	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		for (EventBean event : newEvents) {
			if (event instanceof MapEventBean) {
				Map<?,?> eventAsMap = (Map<?,?>) event.getUnderlying();
				String json = SupportUtility.transformEventMapToJson(eventAsMap);
				logger.debug("[" + name + "]" + "[" + json + "]");	
				file = session.write(file, (outStream) -> {
					outStream.write(json.getBytes());
				});
				file = session.putAttribute(file,"json", json);
				session.transfer(file, rel);
				session.commit();
			}
		}
	}

}
