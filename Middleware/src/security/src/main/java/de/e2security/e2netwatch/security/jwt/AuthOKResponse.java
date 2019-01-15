package de.e2security.e2netwatch.security.jwt;

public class AuthOKResponse {

	private String token;
	private long expireTimeInS;
	
	public AuthOKResponse(String token, long expireTimeInS) {
		this.token = token;
		this.expireTimeInS = expireTimeInS;
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
	
}
