package de.e2security.e2netwatch.usermanagement.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserAdminUpdateDTO {
	
	private Integer id;
	private String username;
    private String firstName;
    private String lastName;
    private String email;
    private Integer userStatusId;
    private List<Integer> userGroupIds;

    public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		if (firstName != null)
			this.firstName = firstName.trim().toUpperCase();
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		if (lastName != null)
			this.lastName = lastName.trim().toUpperCase();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		if (email != null)
			this.email = email.trim().toLowerCase();
	}

	public Integer getUserStatusId() {
		return userStatusId;
	}

	public void setUserStatusId(Integer userStatusId) {
		this.userStatusId = userStatusId;
	}

	public List<Integer> getUserGroupIds() {
		return userGroupIds;
	}
	
	public void setUserGroupId(Integer userGroupId) {
		List<Integer> userGroupIds = new ArrayList<Integer>();
		userGroupIds.add(userGroupId);
		this.userGroupIds = userGroupIds;
	}

	@JsonIgnore
	public void setUserGroupIds(List<Integer> userGroupIds) {
		this.userGroupIds = userGroupIds;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		if (username != null)
			this.username = username.trim();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
    
}
