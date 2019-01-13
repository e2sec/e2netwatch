package de.e2security.processors.e2esper.utilities;

import org.apache.nifi.controller.ControllerService;
import org.apache.nifi.processor.AbstractSessionFactoryProcessor;
import org.apache.nifi.reporting.InitializationException;
import org.apache.nifi.util.TestRunner;
import org.apache.nifi.util.TestRunners;
import org.junit.After;
import org.junit.Before;

import de.e2security.nifi.controller.esper.EsperEngineService;

public abstract class ProcessorTestSupporter {
	
	public TestRunners runners = new TestRunners();
	public ControllerService controller;
	public TestRunner runner;
	
	public abstract AbstractSessionFactoryProcessor initializeProcessor();
	
	@Before public void init() {
		controller = new EsperEngineService();
		runner = runners.newTestRunner(initializeProcessor());
		//adding controller 
		try {
			runner.addControllerService("EsperEngineService", controller);
			runner.enableControllerService(controller);
		} catch (InitializationException e) {
			e.printStackTrace();
		}
	}
	
	@After public void destroy() throws InterruptedException {
		runner.clearProperties();
		runner.clearProvenanceEvents();
		runner.clearTransferState();
		((EsperEngineService) controller).shutdown();
		runner.disableControllerService(controller);
		runner.shutdown();
	}
	
}
