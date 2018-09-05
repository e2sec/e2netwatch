package de.e2security.e2netwatch.spring.security;

public class AuthErrorResponse {
	
	private String error;
	private String errorDescription;
	
	public AuthErrorResponse(String error, String errorDescription) {
		this.error = error;
		this.errorDescription = errorDescription;
	}
	
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

}
