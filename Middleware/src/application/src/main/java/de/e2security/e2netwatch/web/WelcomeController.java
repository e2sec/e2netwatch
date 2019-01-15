package de.e2security.e2netwatch.web;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.e2security.e2netwatch.usermanagement.service.WelcomeService;
import de.e2security.e2netwatch.utils.constants.Mappings;

@RestController
@RequestMapping(value = Mappings.PUBLIC)
public class WelcomeController {
	
	private static final Logger logger = LogManager.getLogger(WelcomeController.class);
	
	@Autowired private WelcomeService usermanagementWelcomeService;
	
	@GetMapping("/")
    public Map<String, String> home() {
		
		logger.info("Logging welcome party");
		//logger.warn("Logging welcome party");
		//logger.error("Logging welcome party");
		
		Map<String, String> welcomeMap = new HashMap<>();
		welcomeMap.put("message", "Welcome to middleware.application");
        return welcomeMap;
    }
	
	@GetMapping("/usermanagement")
    public Map<String, String> usermanagement() {
		Map<String, String> welcomeMap = new HashMap<>();
		welcomeMap.put("message", usermanagementWelcomeService.greeting());
        return welcomeMap;
    }

}
