package de.e2security.nifi.controller.esper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.nifi.annotation.documentation.CapabilityDescription;
import org.apache.nifi.annotation.documentation.Tags;
import org.apache.nifi.annotation.lifecycle.OnDisabled;
import org.apache.nifi.annotation.lifecycle.OnEnabled;
import org.apache.nifi.components.PropertyDescriptor;
import org.apache.nifi.controller.AbstractControllerService;
import org.apache.nifi.controller.ConfigurationContext;
import org.apache.nifi.processor.exception.ProcessException;
import org.apache.nifi.reporting.InitializationException;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;

@Tags({"e2", "esper"})
@CapabilityDescription("main esper engine controller")
public class EsperEngineService extends AbstractControllerService implements EsperService {
	
    private static final List<PropertyDescriptor> properties;
    private EPServiceProvider engine;

    static {
        final List<PropertyDescriptor> props = new ArrayList<>();
        properties = Collections.unmodifiableList(props);
    }

    @Override
    protected List<PropertyDescriptor> getSupportedPropertyDescriptors() {
        return properties;
    }

    @OnEnabled
    public void onEnabled(final ConfigurationContext context) throws InitializationException {
    }

    @OnDisabled
    public void shutdown() {
    	engine.destroy();
    }

    @Override
    public EPServiceProvider execute() throws ProcessException {
    	Configuration config = new Configuration();
    	config.getEngineDefaults().getLogging().setEnableExecutionDebug(true);
    	engine = EPServiceProviderManager.getDefaultProvider(config);
    	return engine;
    }

}
