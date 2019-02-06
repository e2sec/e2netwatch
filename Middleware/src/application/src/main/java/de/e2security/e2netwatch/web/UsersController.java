package de.e2security.e2netwatch.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import de.e2security.e2netwatch.usermanagement.dto.EmailCheckDTO;
import de.e2security.e2netwatch.usermanagement.dto.ListDTO;
import de.e2security.e2netwatch.usermanagement.dto.PasswordResetDTO;
import de.e2security.e2netwatch.usermanagement.dto.UserAdminUpdateDTO;
import de.e2security.e2netwatch.usermanagement.dto.UserDTO;
import de.e2security.e2netwatch.usermanagement.dto.UserInsertDTO;
import de.e2security.e2netwatch.usermanagement.dto.UserUpdateDTO;
import de.e2security.e2netwatch.usermanagement.dto.UsernameCheckDTO;
import de.e2security.e2netwatch.usermanagement.service.UsersService;
import de.e2security.e2netwatch.utils.constants.Mappings;

/**
 * Rest endpoint for Users inquiries
 *
 * @author Hrvoje
 *
 */
@RestController
@RequestMapping(value = Mappings.USERS)
public class UsersController {
	
	@Autowired
	private UsersService manager;
	
	/**
	 * Get all users
	 * 
	 * @return list of all users
	 */
	@RequestMapping(method = RequestMethod.GET, headers = "Accept=application/json")
	public ListDTO<UserDTO> getUsers() {
		return manager.getUsers();
	}
	
	/**
	 * Get current user
	 * 
	 * @return current user
	 */
	@RequestMapping(value = "/profile", method = RequestMethod.GET, headers = "Accept=application/json")
	public UserDTO getUser() {
		return manager.getProfile();
	}
	
	/**
	 * Update user profile
	 */
	@RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
	@ResponseStatus(value=HttpStatus.OK)
	public UserDTO updateUser(@RequestBody UserUpdateDTO user) {
		return manager.updateUser(user);
	}
	
	/**
	 * Add user
	 */
	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseStatus(value=HttpStatus.CREATED)
	@PreAuthorize("hasRole('ADMIN')")
	public UserDTO addUser(@RequestBody UserInsertDTO user) {
		return manager.addUser(user);
	}
	
	/**
	 * Delete user
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	@ResponseStatus(value=HttpStatus.NO_CONTENT)
	@PreAuthorize("hasRole('ADMIN')")
	public void deleteUser(@PathVariable("id") int id) {
		manager.deleteUser(id);
	}
	
	/**
	 * Reset user password
	 */
	@RequestMapping(value="/resetPassword", method = RequestMethod.PUT, headers = "Accept=application/json")
	@ResponseStatus(value=HttpStatus.OK)
	public void resetPassword(@RequestBody PasswordResetDTO password) {
		manager.resetUserPassword(password);
	}
	
	/**
	 * Deactivate user
	 */
	@RequestMapping(value = "/deactivate/{id}", method = RequestMethod.PUT, headers = "Accept=application/json")
	@ResponseStatus(value=HttpStatus.OK)
	@PreAuthorize("hasRole('ADMIN')")
	public void deactivateUser(@PathVariable("id") int id) {
		manager.deactivateUser(id);
	}
	
	/**
	 * Activate user
	 */
	@RequestMapping(value = "/activate/{id}", method = RequestMethod.PUT, headers = "Accept=application/json")
	@ResponseStatus(value=HttpStatus.OK)
	@PreAuthorize("hasRole('ADMIN')")
	public void activateUser(@PathVariable("id") int id) {
		manager.activateUser(id);
	}
	
	/**
	 * Update user's complete data
	 */
	@RequestMapping(value = "/update", method = RequestMethod.PUT, headers = "Accept=application/json")
	@ResponseStatus(value=HttpStatus.OK)
	@PreAuthorize("hasRole('ADMIN')")
	public UserDTO updateUserComplete(@RequestBody UserAdminUpdateDTO user) {
		return manager.updateUserComplete(user);
	}
	
	/**
	 * Check if email is available
	 */
	@RequestMapping(value = "/email/{email:.+}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseStatus(value=HttpStatus.OK)
	public EmailCheckDTO checkEmailValidity(@PathVariable("email") String email) {
		return manager.checkEmailValidity(email);
	}
	
	/**
	 * Check if username is available
	 */
	@RequestMapping(value = "/username/{username:.+}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseStatus(value=HttpStatus.OK)
	public UsernameCheckDTO checkUsernameValidity(@PathVariable("username") String username) {
		return manager.checkUsernameValidity(username);
	}
	
}
