package de.e2security.e2netwatch.usermanagement.service;

import de.e2security.e2netwatch.usermanagement.dto.ProfilePreferenceDTO;

/**
 * Profile preference service for servicing data for entity ProfilePreference
 * 
 * @author Hrvoje
 *
 */
public interface ProfilePreferenceService {
	
	/**
	 * Get profile preferences for current user
	 *
	 * @return Profile preference data for current user
	 */
	ProfilePreferenceDTO getForCurrent();
	
	/**
	 * Get global profile preferences
	 * 
	 * @return global profile preference data
	 */
	ProfilePreferenceDTO getGlobal();
	
}
