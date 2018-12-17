package de.e2security.e2netwatch.usermanagement.service;

import de.e2security.e2netwatch.usermanagement.dto.ListDTO;
import de.e2security.e2netwatch.usermanagement.dto.TimezoneDTO;

/**
 * TimezonesManager
 * 
 * @author Hrvoje
 *
 */
public interface TimezonesService {
	
	/**
	 * Get all timezones
	 *
	 * @return timezones
	 */
	ListDTO<TimezoneDTO> getAll();
	
}
