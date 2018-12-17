package de.e2security.e2netwatch.usermanagement.dto;

public class PasswordResetDTO {
	
    private String password;
    private String repeatedPassword;
    
    public PasswordResetDTO(String password, String repeatedPassword) {
    	if (password != null)
    		this.password = password.trim();
    	if (repeatedPassword != null)
    		this.repeatedPassword = repeatedPassword.trim();
    }
    
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password.trim();
	}
	
	public String getRepeatedPassword() {
		return repeatedPassword;
	}
	
	public void setRepeatedPassword(String repeatedPassword) {
		this.repeatedPassword = repeatedPassword.trim();
	}
    
}
