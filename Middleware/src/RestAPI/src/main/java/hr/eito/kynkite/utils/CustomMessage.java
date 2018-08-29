package hr.eito.kynkite.utils;

/**
 * Enumerator for defining custom success messages
 * used for communication with frontend
 * 
 * @author Hrvoje
 *
 */
public enum CustomMessage {
	
	AQL_RULE_VALID("Aql rule is valid");
	
	private final String message;
	
	CustomMessage(final String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return this.message;
	}

}
