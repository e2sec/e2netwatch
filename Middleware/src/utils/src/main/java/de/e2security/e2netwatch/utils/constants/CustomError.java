package de.e2security.e2netwatch.utils.constants;

/**
 * Enumerator for defining custom error messages
 * used for communication with frontend
 * 
 * @author Hrvoje
 *
 */
public enum CustomError {
	
	INVALID_DATE_TIME_RANGE("Invalid date time range"),
	REPORT_NOT_CREATED("Unknown error - report not created"),
	PARAMETERS_MISSING("Missing parameters"),
	REPORT_ALREADY_EXISTS("Report already exists"),
	
	AQL_RULE_MISSING("Empty rule not allowed"),
	AQL_RULE_ALREADY_EXISTS("Rule already exists"),
	AQL_RULESET_MISSING("AQL rule not found"),
	
	IP_MISSING("IP address missing"),
	IP_INVALID("IP address invalid"),
	
	USERNAME_NOT_RECEIVED("Username not received"),
	USERNAME_TAKEN("Username is already taken"),
	USERNAME_INVALID("Username is not valid"),
	FIRSTNAME_NOT_RECEIVED("First name not received"),
	FIRSTNAME_INVALID("First name is not valid"),
	LASTNAME_NOT_RECEIVED("Last name not received"),
	LASTNAME_INVALID("Last name is not valid"),
	EMAIL_NOT_RECEIVED("Email not received"),
	EMAIL_TAKEN("Email is already taken"),
	EMAIL_INVALID("Email is not valid"),
	USER_NOT_FOUND("User not found"),
	USER_ALREADY_DEACTIVATED("User is deactivated already"),
	USER_ALREADY_ACTIVATED("User is activated already"),
	USERSTATUS_NOT_FOUND("User status with provided id not found"),
	USERGROUP_NOT_RECEIVED("User group not received"),
	USERGROUP_NOT_FOUND("User group with provided id not found");
	
	private final String errorMessage;
	
	CustomError(final String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public String getErrorMessage() {
		return this.errorMessage;
	}
	
	@Override
	public String toString() {
		return this.errorMessage;
	}

}
