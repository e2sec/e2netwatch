package de.e2security.e2netwatch.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.e2security.e2netwatch.usermanagement.dto.UserStatusDTO;
import de.e2security.e2netwatch.usermanagement.service.UserStatusService;
import de.e2security.e2netwatch.utils.constants.Mappings;

/**
 * Rest endpoint for UserStatus inquiries
 *
 * @author Hrvoje
 *
 */
@RestController
@RequestMapping(value = Mappings.USERSTATUSES)
public class UserStatusController {
	
	@Autowired
	private UserStatusService manager;
	
	/**
	 * Get all user statuses
	 * 
	 * @return list of all user statuses
	 */
	@RequestMapping(method = RequestMethod.GET, headers = "Accept=application/json")
	public List<UserStatusDTO> getUserStatuses() {
		return manager.getUserStatuses();
	}
	
}
