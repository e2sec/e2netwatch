package de.e2security.e2netwatch.spring.setup;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.support.ResourcePropertySource;

public class MyApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
	
    private final Logger logger = LoggerFactory.getLogger(MyApplicationContextInitializer.class);
    
    private final HelperInitializer helper = new HelperInitializer();

    /**
     * Sets the active profile.
     */
    @Override
    public void initialize(final ConfigurableApplicationContext applicationContext) {
        final ConfigurableEnvironment environment = applicationContext.getEnvironment();
        String envTarget = null;
        try {
            envTarget = helper.getEnvTarget(environment);
            environment.getPropertySources().addFirst(new ResourcePropertySource("classpath:env-" + envTarget + ".properties"));

            final String activeProfiles = environment.getProperty("spring.profiles.active");
            if (activeProfiles != null) {
                environment.setActiveProfiles(activeProfiles.split(","));
            }
        } catch (final IOException ioEx) {
            if (envTarget != null) {
                logger.warn("Didn't find env-" + envTarget + ".properties in classpath so not loading it in the AppContextInitialized", ioEx);
            }
        }
    }

}
