package de.e2security.e2netwatch.utils.constants;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;

public class MyCustomError {
	
	Date timestamp;
	int status;
	String error;
	String message;
	
	public MyCustomError(int status, String error, String message) {
		this.timestamp = new Date();
		this.status = status;
		this.error = error;
		this.message = message;
	}
	
	public String getTimestamp() {
		OffsetDateTime odt = this.timestamp.toInstant().atOffset( ZoneOffset.UTC );
	    return odt.toString();
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

}
