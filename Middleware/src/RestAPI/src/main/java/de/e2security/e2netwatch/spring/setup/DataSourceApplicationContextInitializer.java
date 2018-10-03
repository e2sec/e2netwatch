package de.e2security.e2netwatch.spring.setup;

import java.io.IOException;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.support.ResourcePropertySource;

public class DataSourceApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
	
	private final Logger logger = LoggerFactory.getLogger(DataSourceApplicationContextInitializer.class);
	
	private final HelperInitializer helper = new HelperInitializer();

    @Override
    public void initialize(final ConfigurableApplicationContext applicationContext) {
    	final ConfigurableEnvironment environment = applicationContext.getEnvironment();
    	String envTarget = null;
    	try {
    		envTarget = helper.getEnvTarget(environment);
			environment.getPropertySources().addFirst(new ResourcePropertySource("classpath:persistence-" + envTarget + ".properties"));
			// Database migration with Flyway
	    	Flyway flyway = new Flyway();
	    	flyway.setDataSource(
	    			environment.getProperty("jdbc.um.url"), 
	    			environment.getProperty("jdbc.um.username"), 
	    			environment.getProperty("jdbc.um.password")
	    			);
	    	if (flyway.info().current() == null) {
	    		flyway.setBaselineVersionAsString("0");
	    		flyway.baseline();
	    	} else {
	    		flyway.migrate();
	    	}
		} catch (IOException ioEx) {
			if (envTarget != null) {
                logger.warn("Didn't find env-" + envTarget + ".properties in classpath so not loading it in the AppContextInitialized", ioEx);
            }
		}
    }

}
