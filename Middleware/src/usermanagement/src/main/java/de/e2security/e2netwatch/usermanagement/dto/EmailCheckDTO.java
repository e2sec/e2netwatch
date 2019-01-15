package de.e2security.e2netwatch.usermanagement.dto;

public class EmailCheckDTO {
	
	private String email;
	private Boolean valid;
	private Boolean available;
	
	public EmailCheckDTO (String email, Boolean valid, Boolean available) {
		this.email = email;
		this.valid = valid;
		this.available = available;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		if (email != null)
			this.email = email.trim().toLowerCase();
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
