package de.e2security.e2netwatch.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.e2security.e2netwatch.usermanagement.dto.UserGroupDTO;
import de.e2security.e2netwatch.usermanagement.service.UserGroupService;
import de.e2security.e2netwatch.utils.constants.Mappings;

/**
 * Rest endpoint for UserGroup inquiries
 *
 * @author Hrvoje
 *
 */
@RestController
@RequestMapping(value = Mappings.USERGROUPS)
public class UserGroupController {
	
	@Autowired
	private UserGroupService manager;
	
	/**
	 * Get all user groups
	 * 
	 * @return list of all user groups
	 */
	@RequestMapping(method = RequestMethod.GET, headers = "Accept=application/json")
	public List<UserGroupDTO> getUserGroups() {
		return manager.getUserGroups();
	}
	
}
