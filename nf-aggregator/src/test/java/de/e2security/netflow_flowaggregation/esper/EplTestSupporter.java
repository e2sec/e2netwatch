package de.e2security.netflow_flowaggregation.esper;

import org.junit.After;
import org.junit.Before;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;

import de.e2security.netflow_flowaggregation.model.protocols.NetflowEvent;
import de.e2security.netflow_flowaggregation.model.protocols.NetflowEventOrdered;
import de.e2security.netflow_flowaggregation.model.protocols.TcpConnection;
import de.e2security.netflow_flowaggregation.model.protocols.UdpConnection;

public abstract class EplTestSupporter {
	
	/*
	 * this is main epl test class, thus all EventTypes should be defined in init();
	 */
	EPServiceProvider engine;
	EPAdministrator admin;
	EPRuntime runtime;
	
	@Before public void init() {
		Configuration config = new Configuration();
		config.addEventType(NetflowEvent.class);
		config.addEventType(NetflowEventOrdered.class);
		config.addEventType(TcpConnection.class);
		config.addEventType(UdpConnection.class);
		config.getEngineDefaults().getThreading().setInternalTimerEnabled(false);
		engine = EPServiceProviderManager.getDefaultProvider(config);
		runtime = engine.getEPRuntime();
		admin = engine.getEPAdministrator();
	}

	@After public void destroy() {
		engine.destroy();
	}

}
