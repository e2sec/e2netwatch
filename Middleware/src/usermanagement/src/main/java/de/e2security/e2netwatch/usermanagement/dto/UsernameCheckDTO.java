package de.e2security.e2netwatch.usermanagement.dto;

public class UsernameCheckDTO {
	
	private String username;
	private Boolean valid;
	private Boolean available;
	
	public UsernameCheckDTO (String username, Boolean valid, Boolean available) {
		this.username = username;
		this.valid = valid;
		this.available = available;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		if (username != null)
			this.username = username.trim().toLowerCase();
	}

	public Boolean isAvailable() {
		return available;
	}

	public void setAvailable(Boolean available) {
		this.available = available;
	}

	public Boolean isValid() {
		return valid;
	}

	public void setValid(Boolean valid) {
		this.valid = valid;
	}

}
