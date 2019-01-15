package de.e2security.e2netwatch.usermanagement.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import de.e2security.e2netwatch.usermanagement.model.User;

/**
 * Repository for User
 * 
 * @author Hrvoje
 *
 */
public interface UserRepository extends JpaRepository<User, Integer> {
	
	/**
	 * Getting the User by username
	 * 
	 * @param username
	 * 
	 * @return User
	 */
	public User findByUsername(String username);
	
	/**
	 * Getting the User by email
	 * 
	 * @param email
	 * 
	 * @return User
	 */
	public User findByEmail(String email);
	
}
