package de.e2security.e2netwatch.security.jwt;

public class AuthOKResponse {

	private String token;
	private long expireTimeInS;
	private String role;
	
	public AuthOKResponse(String token, long expireTimeInS, String role) {
		this.token = token;
		this.expireTimeInS = expireTimeInS;
		this.setRole(role);
	}
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public long getExpireTimeInS() {
		return expireTimeInS;
	}
	public void setExpireTimeInS(long expireTimeInS) {
		this.expireTimeInS = expireTimeInS;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
}
