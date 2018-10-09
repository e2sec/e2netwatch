package de.e2security.e2netwatch.rest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import de.e2security.e2netwatch.utils.Mappings;

/**
 * Entrypoint to check if Middleware is up
 *
 * @author Hrvoje Zeljko
 *
 */
@RestController
@RequestMapping(value = Mappings.PUBLIC)
public class PublicController {
	
	private final Logger logger = LogManager.getLogger(PublicController.class);
	
	@RequestMapping(value = "/welcome", method = RequestMethod.GET)
	public String rawWelcomeMessage() {
		logger.info("Welcome message pulled");
		return "Hello from Middleware!";
	}
}
