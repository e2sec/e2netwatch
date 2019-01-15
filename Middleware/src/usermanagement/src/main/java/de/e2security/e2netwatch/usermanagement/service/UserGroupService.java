package de.e2security.e2netwatch.usermanagement.service;

import java.util.List;

import de.e2security.e2netwatch.usermanagement.dto.UserGroupDTO;

/**
 * User group service for servicing data for entity UserGroup
 * 
 * @author Hrvoje
 *
 */
public interface UserGroupService {
	
	/**
	 * Getting all user groups
	 * 
	 * @return list of user groups
	 */
	List<UserGroupDTO> getUserGroups();

}
