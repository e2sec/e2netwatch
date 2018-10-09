package de.e2security.e2netwatch.spring.setup;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.env.ConfigurableEnvironment;

import com.google.common.base.Preconditions;

public class HelperInitializer {
	
    private final Logger logger = LogManager.getLogger(HelperInitializer.class);

    private static final String ENV_TARGET = "envTarget";

    /**
     * Retrieve the environment name
     * 
     * @param environment
     * @return The env target variable.
     */
    public String getEnvTarget(final ConfigurableEnvironment environment) {
        String target = environment.getProperty(ENV_TARGET);
        if (target == null) {
            logger.warn("Didn't find a value for {} in the current Environment!", ENV_TARGET);
            logger.info("Didn't find a value for {} in the current Environment!, using the default `dev`", ENV_TARGET);
            target = "dev";
        }

        return Preconditions.checkNotNull(target);
    }

}
