
/*
    Copyright (C) 2017 e-ito Technology Services GmbH
    e-mail: info@e-ito.de
    
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/


package de.e2security.e2netwatch.business.manager;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.e2security.e2netwatch.model.JsonReturnData;
import de.e2security.e2netwatch.rest.dto.MenuReturnResult;
import de.e2security.e2netwatch.rest.dto.UserReturnResultData;
import de.e2security.e2netwatch.usermanagement.dao.ProfileMenuDAO;
import de.e2security.e2netwatch.usermanagement.dao.ProfilePreferenceDAO;
import de.e2security.e2netwatch.usermanagement.dao.UserDAO;
import de.e2security.e2netwatch.usermanagement.helper.RequestMiner;
import de.e2security.e2netwatch.usermanagement.model.Authority;
import de.e2security.e2netwatch.usermanagement.model.ProfileMenu;
import de.e2security.e2netwatch.usermanagement.model.ProfilePreference;
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
	private UserDAO userDAO;
	
	@Autowired
	private ProfilePreferenceDAO profilePreferenceDAO;
	
	@Autowired
	private ProfileMenuDAO profileMenuDAO;
	
	@Override
	public JsonReturnData<MenuReturnResult> getMenuForCurrentUser() {
		// Get current user
		String username = requestMiner.getCurrentUsername();
		
		// TODO
		// Check if username is null
		
		// Get the Menu result
		MenuReturnResult menuReturnResult = null;
		try {
			menuReturnResult = this.getMenuForUser(username);
		} catch (Exception e) {
			return new JsonReturnData<>(e.getMessage());
		}
		
		return new JsonReturnData<MenuReturnResult>(menuReturnResult);
	}
	
	/**
	 * Getting the MenuReturnResult for specified username
	 * 
	 * @param username
	 * @return MenuReturnResult object
	 * 
	 * @throws Exception with appropriate message
	 */
	private MenuReturnResult getMenuForUser(final String username) throws Exception {
		ProfilePreference profilePreference = null;

		// Getting the User
		User user = userDAO.getByUsername(username);

		// Detect the ProfilePreference for the User
		if (user!=null) {
			// Get ProfilePreference record for user
			if (user.getProfilePreference()!=null) {
				profilePreference = user.getProfilePreference();
			} else {
				for (UserGroup userGroup : user.getUserGroups()) {
					if (userGroup.getProfilePreference()!=null) {
						profilePreference = userGroup.getProfilePreference();
						break;
					}
				}
				if (profilePreference==null) {
					profilePreference = profilePreferenceDAO.getGlobal();
				}
			}
		} else {
			throw new Exception("User was not found");
		}
		
		// Create MenuReturnResult base on detected ProfilePreference
		MenuReturnResult menuReturnResult;
		if (profilePreference==null) {
			menuReturnResult = new MenuReturnResult(null);
		} else {
			// Get ProfileMenus for given ProfilePreference and create menu
			List<ProfileMenu> profileMenus = profileMenuDAO.getAllByProfilePreference(profilePreference.getId());
			menuReturnResult = new MenuReturnResult(profileMenus);
		}
		
		return menuReturnResult;
	}

	@Override
	public JsonReturnData<UserReturnResultData> getCurrentUser() {
		// Get current user's username
		String username = requestMiner.getCurrentUsername();
		
		// Getting the User
		User user = userDAO.getByUsername(username);
		
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
