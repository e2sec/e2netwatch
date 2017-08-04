
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


package hr.eito.kynkite.usermanagement.dao.stub;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import hr.eito.kynkite.usermanagement.dao.UserDAO;
import hr.eito.kynkite.usermanagement.dao.UserGroupDAO;
import hr.eito.kynkite.usermanagement.model.User;
import hr.eito.kynkite.usermanagement.model.UserGroup;

@Repository
@Profile({"test"})
public class UserDAOStub implements UserDAO {
	
	@Autowired
	private UserGroupDAO userGroupDAO;
	
	private List<User> repository;
	
	public UserDAOStub() {
		repository = new ArrayList<>();
		
		User u1 = new User();
		u1.setId(1);
		u1.setUsername("username");
		u1.setFirstName("FN_user1");
		u1.setLastName("LN_user1");
		u1.setEmail("email_user1");
		
		List<UserGroup> userGroups = new ArrayList<>();
		userGroups.add(userGroupDAO.getByName("Administrators"));
		u1.setUserGroups(userGroups);
		
		repository.add(u1);
	}

	/**
	 * Stub method for getting User by username
	 */
	@Override
	public User getByUsername(String username) {
		for(User u : repository) {
			if(StringUtils.equals(u.getUsername(), username)) {
				return u;
			}
		}
		return null;
	}
	
}
