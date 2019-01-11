package de.e2security.e2netwatch.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.e2security.e2netwatch.usermanagement.dto.ProfilePreferenceDTO;
import de.e2security.e2netwatch.usermanagement.service.ProfilePreferenceService;
import de.e2security.e2netwatch.utils.constants.Mappings;

/**
 * Rest endpoint for ProfilePreferences inquiries
 *
 * @author Hrvoje
 *
 */
@RestController
@RequestMapping(value = Mappings.PROFILE_PREFERENCES)
public class ProfilePreferenceController {
	
	@Autowired
	private ProfilePreferenceService manager;
	
	/**
	 * Get current user
	 * 
	 * @return current user
	 */
	@RequestMapping(value = "/current", method = RequestMethod.GET, headers = "Accept=application/json")
	public ProfilePreferenceDTO getForCurrentUser() {
		return manager.getForCurrent();
	}
	
}
