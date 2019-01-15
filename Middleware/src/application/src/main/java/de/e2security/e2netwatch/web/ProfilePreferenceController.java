package de.e2security.e2netwatch.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import de.e2security.e2netwatch.usermanagement.dto.ProfilePreferenceDTO;
import de.e2security.e2netwatch.usermanagement.dto.ProfilePreferenceUpdateDTO;
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
	
	/**
	 * Update profile preferences
	 */
	@RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
	@ResponseStatus(value=HttpStatus.OK)
	public ProfilePreferenceDTO updateProfilePreferences(@RequestBody ProfilePreferenceUpdateDTO profilePreferences) {
		return manager.updateProfilePreferences(profilePreferences);
	}
	
	/**
	 * Update global profile preferences
	 */
	@RequestMapping(value = "/global", method = RequestMethod.PUT, headers = "Accept=application/json")
	@ResponseStatus(value=HttpStatus.OK)
	public ProfilePreferenceDTO updateGlobal(@RequestBody ProfilePreferenceUpdateDTO profilePreferences) {
		return manager.updateGlobal(profilePreferences);
	}
	
}
