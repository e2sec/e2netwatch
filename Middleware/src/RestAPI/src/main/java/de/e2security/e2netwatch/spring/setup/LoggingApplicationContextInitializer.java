package de.e2security.e2netwatch.spring.setup;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

public class LoggingApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
		
	private final HelperInitializer helper = new HelperInitializer();

    /**
     * Sets logging configuration according to environment
     */
    @Override
    public void initialize(final ConfigurableApplicationContext applicationContext) {
        final ConfigurableEnvironment environment = applicationContext.getEnvironment();
        String envTarget = helper.getEnvTarget(environment);

        // Configuration file for logging depending on environment
        String loggingConfigFileName = "logging/log4j2-"+envTarget+".json";
        LoggerContext context = (LoggerContext) LogManager.getContext(false);
        ClassLoader classLoader = getClass().getClassLoader();
    	File file = new File(classLoader.getResource(loggingConfigFileName).getFile());
    	context.setConfigLocation(file.toURI());
    }

}
