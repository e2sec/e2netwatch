package de.e2security.e2netwatch.usermanagement.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import de.e2security.e2netwatch.usermanagement.model.UserStatus;

/**
 * Repository for UserStatus
 * 
 * @author Hrvoje
 *
 */
public interface UserStatusRepository extends JpaRepository<UserStatus, Integer> {
	
	/**
	 * Getting the UserStatus by code
	 * 
	 * @param code of the status
	 * 
	 * @return UserStatus with provided code
	 */
	public UserStatus findByCode(String code);
	
}
