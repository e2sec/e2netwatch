package de.e2security.e2netwatch.usermanagement.service;

import java.util.List;

import de.e2security.e2netwatch.usermanagement.dto.UserStatusDTO;

/**
 * User status service for servicing data for entity UserStatus
 * @author Hrvoje
 *
 */
public interface UserStatusService {
	
	/**
	 * Get all user statuses
	 * 
	 * @return list of user statuses
	 */
	List<UserStatusDTO> getUserStatuses();

}
