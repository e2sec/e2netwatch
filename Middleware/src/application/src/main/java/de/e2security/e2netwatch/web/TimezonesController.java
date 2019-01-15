package de.e2security.e2netwatch.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.e2security.e2netwatch.usermanagement.dto.ListDTO;
import de.e2security.e2netwatch.usermanagement.dto.TimezoneDTO;
import de.e2security.e2netwatch.usermanagement.service.TimezonesService;
import de.e2security.e2netwatch.utils.constants.Mappings;

/**
 * Rest endpoint for Timezones
 *
 * @author Hrvoje
 *
 */
@RestController
@RequestMapping(value = Mappings.TIMEZONES)
public class TimezonesController {
	
	@Autowired
	private TimezonesService manager;
	
	/**
	 * Get current user
	 * 
	 * @return current user as JSON
	 */
	@RequestMapping(method = RequestMethod.GET, headers = "Accept=application/json")
	public ListDTO<TimezoneDTO> getAll() {
		return manager.getAll();
	}
	
}
