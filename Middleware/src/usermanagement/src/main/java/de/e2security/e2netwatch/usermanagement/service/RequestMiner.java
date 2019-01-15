package de.e2security.e2netwatch.usermanagement.service;

/**
 * Helper interface for getting necessary information 
 * from HttpServletRequest for user management
 *
 * @author Hrvoje
 */
public interface RequestMiner {
	
	/**
	 * Getting username for the current user.
	 * <p>From request cookie username is pulled
	 * 
	 * @return username as String or null if nothing found
	 */
	public String getCurrentUsername();
	
}
