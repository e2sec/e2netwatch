package de.e2security.netflow_flowaggregation.utils;

import java.util.stream.Stream;

import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;

public final class EsperUtil {
	
	public static EPServiceProvider registerEvents(Class... classes) {
		EPServiceProvider engine = EPServiceProviderManager.getDefaultProvider();
		Stream.of(classes).forEach(clazz -> {
			engine.getEPAdministrator().getConfiguration().addEventType(clazz);
		});
		return engine;
	}
}
