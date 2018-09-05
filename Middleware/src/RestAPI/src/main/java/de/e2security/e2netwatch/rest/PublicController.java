package de.e2security.e2netwatch.rest;

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
	
	@RequestMapping(value = "/welcome", method = RequestMethod.GET)
	public String test() {
		return "{\"message\":\"Welcome to middleware!\"}";
	}
}
