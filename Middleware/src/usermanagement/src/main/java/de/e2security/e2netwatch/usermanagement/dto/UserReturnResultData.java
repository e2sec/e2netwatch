package de.e2security.e2netwatch.usermanagement.dto;

import java.util.List;
import java.util.Set;

import de.e2security.e2netwatch.usermanagement.model.User;
import de.e2security.e2netwatch.usermanagement.model.UserGroup;
import de.e2security.e2netwatch.usermanagement.model.UserStatus;

/**
 * Class encapsulating User information
 * 
 * @author Hrvoje
 *
 */
public class UserReturnResultData {
	
	private String username;
	private String firstName;
	private String lastName;
	private String email;
	private UserStatus userStatus;
	private List<String> roles;
	private Set<UserGroup> userGroups;
	
	/**
	 * Create UserReturnResultData based on User DB mapped class object 
	 * 
	 * @param user User object from which is created
	 */
	public UserReturnResultData(final User user, final List<String> roles) {
		this.username = user.getUsername();
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.email = user.getEmail();
		this.userStatus = user.getUserStatus();
		this.roles = roles;
		this.userGroups = user.getUserGroups();
	}
	
	/**
	 * Default constructor for testing
	 */
	public UserReturnResultData() {}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public UserStatus getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(UserStatus userStatus) {
		this.userStatus = userStatus;
	}

	public Set<UserGroup> getUserGroups() {
		return userGroups;
	}

	public void setUserGroups(Set<UserGroup> userGroups) {
		this.userGroups = userGroups;
	}
	
}
