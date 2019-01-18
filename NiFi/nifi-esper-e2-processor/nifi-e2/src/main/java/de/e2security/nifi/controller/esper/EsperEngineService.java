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
import org.apache.nifi.processor.util.StandardValidators;
import org.apache.nifi.reporting.InitializationException;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;

@Tags({"e2", "esper"})
@CapabilityDescription("main esper engine controller")
public class EsperEngineService extends AbstractControllerService implements EsperService {

	public static final PropertyDescriptor ENABLE_INTERNAL_TIMING = new PropertyDescriptor.Builder()
			.name("EnableInternalTiming")
			.displayName("Enable Internal Timing")
			.description("activate internal timing instead of external one")
			.required(true)
			.defaultValue("true")
			.addValidator(StandardValidators.BOOLEAN_VALIDATOR)
			.build();
	
	public static final PropertyDescriptor ENABLE_ENGINE_METRIC = new PropertyDescriptor.Builder()
			.name("EnableEngineMetric")
			.displayName("Enable Engine Metric")
			.description("activate default listeners esper engine metric")
			.required(true)
			.defaultValue("true")
			.addValidator(StandardValidators.BOOLEAN_VALIDATOR)
			.build();
	
	public static final PropertyDescriptor ENABLE_STATEMENT_METRIC = new PropertyDescriptor.Builder()
			.name("EnableStmtMetric")
			.displayName("Enable Stmt Metric")
			.description("activate default listeners for esper statement metric")
			.required(true)
			.defaultValue("true")
			.addValidator(StandardValidators.BOOLEAN_VALIDATOR)
			.build();
	
	public static final PropertyDescriptor ENABLE_JMX_METRICS = new PropertyDescriptor.Builder()
			.name("EnableJmxMetrics")
			.displayName("Enable JMX Metrics")
			.description("activate default listener for jmx metrics for esper engine")
			.required(true)
			.defaultValue("true")
			.addValidator(StandardValidators.BOOLEAN_VALIDATOR)
			.build();

	public static final PropertyDescriptor ENABLE_ENGINE_DEBUG = new PropertyDescriptor.Builder()
			.name("EnableEngineDebug")
			.displayName("Enable Debug Level")
			.description("set engine's log level to DEBUG")
			.required(true)
			.defaultValue("true")
			.addValidator(StandardValidators.BOOLEAN_VALIDATOR)
			.build();
	
	public static final PropertyDescriptor SET_ENGINE_INTERVAL = new PropertyDescriptor.Builder()
			.name("SetEngineInterval")
			.displayName("Set Engine Interval")
			.description("set Engine interval in ms")
			.required(true)
			.defaultValue("1000")
			.addValidator(StandardValidators.NUMBER_VALIDATOR)
			.build();
	
	public static final PropertyDescriptor SET_STMT_INTERVAL = new PropertyDescriptor.Builder()
			.name("SetStmtInterval")
			.displayName("Set Stmt Interval")
			.description("set statement interval in ms")
			.required(true)
			.defaultValue("1000")
			.addValidator(StandardValidators.NUMBER_VALIDATOR)
			.build();
	
	
    @Override
    protected List<PropertyDescriptor> getSupportedPropertyDescriptors() {
        return properties;
    }
    
    private volatile EPServiceProvider engine;

    @OnEnabled
    public void onEnabled(final ConfigurationContext context) throws InitializationException {
    	//*** ESPER CONFIGURATION ***\\
    	final Configuration config = new Configuration();
    	config.addPlugInPatternObserver("timer", "event", "de.e2security.nifi.controller.esper.observer.EventTimerObserverFactory");
    	
    	if (context.getProperty(ENABLE_ENGINE_METRIC).asBoolean().booleanValue() 
    		|| context.getProperty(ENABLE_JMX_METRICS).asBoolean().booleanValue()
    		|| context.getProperty(ENABLE_STATEMENT_METRIC).asBoolean().booleanValue() ) {
    		config.getEngineDefaults().getMetricsReporting().setEnableMetricsReporting(true);
    		config.getEngineDefaults().getMetricsReporting().setEngineInterval(context.getProperty(SET_ENGINE_INTERVAL).asLong());
    		config.getEngineDefaults().getMetricsReporting().setStatementInterval(context.getProperty(SET_STMT_INTERVAL).asLong());
    	}
    	
   		config.getEngineDefaults().getLogging().setEnableExecutionDebug(context.getProperty(ENABLE_ENGINE_DEBUG).asBoolean().booleanValue());
   		config.getEngineDefaults().getMetricsReporting().setJmxEngineMetrics(context.getProperty(ENABLE_JMX_METRICS).asBoolean().booleanValue());
   		config.getEngineDefaults().getThreading().setInternalTimerEnabled(context.getProperty(ENABLE_INTERNAL_TIMING).asBoolean().booleanValue());
    	
    	//*** ENGINE SETTINGS ***\\\
   		engine = EPServiceProviderManager.getDefaultProvider(config);
    		
    	if (context.getProperty(ENABLE_ENGINE_METRIC).asBoolean().booleanValue()) {
    		engine.getEPAdministrator().createEPL("@Name('EngineMetric') select * from com.espertech.esper.client.metric.EngineMetric");
    	}
    	
    	if (context.getProperty(ENABLE_STATEMENT_METRIC).asBoolean().booleanValue()) {
    		engine.getEPAdministrator().createEPL("@Name('StmtMetric') select * from com.espertech.esper.client.metric.StatementMetric"); 
    	}
    	
    }

    @OnDisabled
    public void shutdown() {
    	engine.destroy();
    }

    @Override
    public EPServiceProvider execute() throws ProcessException {
    	return engine;
    }

    private static final List<PropertyDescriptor> properties;
    
    static {
    	final List<PropertyDescriptor> props = new ArrayList<>();
    	props.add(ENABLE_ENGINE_DEBUG);
    	props.add(ENABLE_ENGINE_METRIC);
    	props.add(ENABLE_JMX_METRICS);
    	props.add(ENABLE_STATEMENT_METRIC);
    	props.add(SET_ENGINE_INTERVAL);
    	props.add(SET_STMT_INTERVAL);
    	props.add(ENABLE_INTERNAL_TIMING);
    	properties = Collections.unmodifiableList(props);
    }
    
}
