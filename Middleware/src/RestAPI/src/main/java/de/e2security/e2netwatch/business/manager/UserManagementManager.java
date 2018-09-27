package de.e2security.e2netwatch.business.manager;

import de.e2security.e2netwatch.model.JsonReturnData;
import de.e2security.e2netwatch.rest.dto.UserReturnResultData;

/**
 * UserManagement manager for managing User requests and data
 * 
 * @author Hrvoje
 *
 */
public interface UserManagementManager {
	
	/**
	 * Get current user data
	 *
	 * @return the current user data
	 */
	JsonReturnData<UserReturnResultData> getCurrentUser();
	
}
