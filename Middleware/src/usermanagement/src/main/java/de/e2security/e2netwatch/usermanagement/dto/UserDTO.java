package de.e2security.e2netwatch.usermanagement.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserDTO {
	
    private Integer id;
    private String username;
    private String generatedPassword;
    private String firstName;
    private String lastName;
    private String email;
    private List<UserGroupDTO> userGroups;
    private UserStatusDTO userStatus;
    private ProfilePreferenceDTO profilePreference;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

	@JsonIgnore
	public List<UserGroupDTO> getUserGroups() {
		return userGroups;
	}

	public void setUserGroups(List<UserGroupDTO> userGroups) {
		this.userGroups = userGroups;
	}

	public UserStatusDTO getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(UserStatusDTO userStatus) {
		this.userStatus = userStatus;
	}

	public ProfilePreferenceDTO getProfilePreference() {
		return profilePreference;
	}

	public void setProfilePreference(ProfilePreferenceDTO profilePreference) {
		this.profilePreference = profilePreference;
	}

	public String getGeneratedPassword() {
		return generatedPassword;
	}

	public void setGeneratedPassword(String generatedPassword) {
		this.generatedPassword = generatedPassword;
	}
	
	public UserGroupDTO getUserGroup() {
		return userGroups.get(0);
	}
    
}
