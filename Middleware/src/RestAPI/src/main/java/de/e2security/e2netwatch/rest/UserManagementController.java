package de.e2security.e2netwatch.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.e2security.e2netwatch.business.manager.UserManagementManager;
import de.e2security.e2netwatch.model.JsonReturnData;
import de.e2security.e2netwatch.rest.dto.UserReturnResultData;
import de.e2security.e2netwatch.utils.Mappings;

/**
 * Rest endpoint for UserManagement inquiries
 *
 * @author Hrvoje
 *
 */
@RestController
@RequestMapping(value = Mappings.USERS)
public class UserManagementController {
	
	@Autowired
	private UserManagementManager manager;
	
	/**
	 * Get current user
	 * 
	 * @return current user as JSON
	 */
	@RequestMapping(value = "/getCurrent", method = RequestMethod.GET, headers = "Accept=application/json")
	public JsonReturnData<UserReturnResultData> getCurrentUser() {
		JsonReturnData<UserReturnResultData> userResult = manager.getCurrentUser();
		return userResult;
	}
	
}
