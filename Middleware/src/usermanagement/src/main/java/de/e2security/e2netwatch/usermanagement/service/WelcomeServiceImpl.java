package de.e2security.e2netwatch.usermanagement.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class WelcomeServiceImpl implements WelcomeService {
	
	private Logger logger = LogManager.getLogger(WelcomeServiceImpl.class);

	/*
	 * (non-Javadoc)
	 * @see de.e2security.e2netwatch.usermanagement.service.WelcomeService#greeting()
	 */
	@Override
	public String greeting() {
		logger.info("Logging User Management welcome party");
		return "Welcome to User Management";
	}

}
