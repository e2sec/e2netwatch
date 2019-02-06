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
	
	public volatile TestRunners runners = new TestRunners();
	public volatile ControllerService controller;
	public volatile TestRunner runner;
	
	public abstract AbstractSessionFactoryProcessor initializeProcessor();
	
	@Before public void init() {
		controller = new EsperEngineService();
		runner = runners.newTestRunner(initializeProcessor());
		runner.setValidateExpressionUsage(false);
		//adding controller 
		try {
			runner.addControllerService("EsperEngineService", controller);
			runner.enableControllerService(controller);
		} catch (InitializationException e) {
			runner.setProperty(controller, EsperEngineService.ENABLE_STATEMENT_METRIC, "true");
			runner.setProperty(controller, EsperEngineService.ENABLE_ENGINE_METRIC, "true");
			e.printStackTrace();
		}
	}
	
	@After public void destroy() throws InterruptedException {
		runner.clearProperties();
		runner.clearProvenanceEvents();
		runner.clearTransferState();
		runner.disableControllerService(controller);
		runner.shutdown();
	}
	
}
