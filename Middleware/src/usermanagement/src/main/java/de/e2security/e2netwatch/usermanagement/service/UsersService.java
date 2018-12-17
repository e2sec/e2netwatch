package de.e2security.e2netwatch.usermanagement.service;

import de.e2security.e2netwatch.usermanagement.dto.EmailCheckDTO;
import de.e2security.e2netwatch.usermanagement.dto.ListDTO;
import de.e2security.e2netwatch.usermanagement.dto.PasswordResetDTO;
import de.e2security.e2netwatch.usermanagement.dto.UserAdminUpdateDTO;
import de.e2security.e2netwatch.usermanagement.dto.UserDTO;
import de.e2security.e2netwatch.usermanagement.dto.UserInsertDTO;
import de.e2security.e2netwatch.usermanagement.dto.UserUpdateDTO;
import de.e2security.e2netwatch.usermanagement.dto.UsernameCheckDTO;
import de.e2security.e2netwatch.usermanagement.model.User;

/**
 * UserManagement manager for managing User requests and data
 * 
 * @author Hrvoje
 *
 */
public interface UsersService {
	
	/**
	 * Get user database entity by username. Used only during login.
	 *
	 * @return user
	 */
	User findByUsername(String username);
	
	/**
	 * Get current user profile
	 *
	 * @return current user
	 */
	UserDTO getProfile();
	
	/**
	 * Update user
	 * 
	 * @param user to be updated
	 * 
	 * @return updated user
	 */
	UserDTO updateUser(UserUpdateDTO user);
	
	/**
	 * Insert user
	 * 
	 * @param user data as object
	 * 
	 * @return created user
	 */
	UserDTO addUser(UserInsertDTO user);
	
	/**
	 * Delete user
	 * 
	 * @param id of the user
	 */
	void deleteUser(int id);
	
	/**
	 * Service method for reseting user's password
	 * 
	 * @param password object received consists of password and rewritten password
	 */
	void resetUserPassword(PasswordResetDTO password);
	
	/**
	 * Deactivate user
	 * 
	 * @param id of the user
	 * 
	 * @return changed user as object
	 */
	UserDTO deactivateUser(int id);
	
	/**
	 * Activate user
	 * 
	 * @param id of the user
	 * 
	 * @return changed user as object
	 */
	UserDTO activateUser(int id);
	
	/**
	 * Update user's complete data
	 * 
	 * @param user to be updated
	 * 
	 * @return updated user
	 */
	UserDTO updateUserComplete(UserAdminUpdateDTO user);
	
	/**
	 * Check email validity and availability
	 * 
	 * @param email address to check
	 * 
	 * @return object with info whether email address is valid and available
	 */
	EmailCheckDTO checkEmailValidity(String email);
	
	/**
	 * Check username validity and availability
	 * 
	 * @param username to check
	 * 
	 * @return object with info whether username address is valid and available
	 */
	UsernameCheckDTO checkUsernameValidity(String username);
	
	/**
	 * Get all users
	 * 
	 * @return object with list of all users and other info
	 */
	ListDTO<UserDTO> getUsers();
	
}
