package de.e2security.e2netwatch.business.manager;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.e2security.e2netwatch.model.JsonReturnData;
import de.e2security.e2netwatch.rest.dto.UserReturnResultData;
import de.e2security.e2netwatch.usermanagement.dao.UserRepository;
import de.e2security.e2netwatch.usermanagement.helper.RequestMiner;
import de.e2security.e2netwatch.usermanagement.model.Authority;
import de.e2security.e2netwatch.usermanagement.model.User;
import de.e2security.e2netwatch.usermanagement.model.UserGroup;

/**
 * UserManagement manager implementation for managing User requests and data
 * 
 * @author Hrvoje
 *
 */
@Component
public class UserManagementManagerImpl implements UserManagementManager {
	
	@Autowired
	private RequestMiner requestMiner;
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public JsonReturnData<UserReturnResultData> getCurrentUser() {
		// Get current user's username
		String username = requestMiner.getCurrentUsername();
		
		// Getting the User
		User user = userRepository.findByUsername(username);
		
		final Set<Authority> authoritiesOfUser = user.getAuthorities();
        final Set<UserGroup> userGroupsOfUser = user.getUserGroups();
        for (final UserGroup userGroup : userGroupsOfUser) {
        	authoritiesOfUser.addAll(userGroup.getAuthorities());
        }
        List<String> listOfAuthorityNames = authoritiesOfUser.stream().map(Authority::getName).collect(Collectors.toList());
		
		UserReturnResultData userReturnResultData = new UserReturnResultData(user, listOfAuthorityNames);
		
		return new JsonReturnData<UserReturnResultData>(userReturnResultData);
	}

}
