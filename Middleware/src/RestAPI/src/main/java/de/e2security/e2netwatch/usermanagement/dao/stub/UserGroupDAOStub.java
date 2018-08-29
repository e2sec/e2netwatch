
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


package de.e2security.e2netwatch.usermanagement.dao.stub;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import de.e2security.e2netwatch.usermanagement.dao.AuthorityDAO;
import de.e2security.e2netwatch.usermanagement.dao.ProfilePreferenceDAO;
import de.e2security.e2netwatch.usermanagement.dao.UserGroupDAO;
import de.e2security.e2netwatch.usermanagement.model.Authority;
import de.e2security.e2netwatch.usermanagement.model.UserGroup;

@Repository
@Profile({"test"})
public class UserGroupDAOStub implements UserGroupDAO {
	
	@Autowired
	private AuthorityDAO authorityDAO;
	
	@Autowired
	private ProfilePreferenceDAO profilePreferenceDAO;
	
	private Set<UserGroup> repository;
	
	@PostConstruct
	public void init() {
		repository = new HashSet<>();
		
		UserGroup ug1 = new UserGroup();
		ug1.setName("Administrators");
		ug1.setId(1);
		Set<Authority> authorities1 = new HashSet<>();
		authorities1.add(authorityDAO.getByName("ROLE_ADMIN"));
		ug1.setAuthorities(authorities1);
		ug1.setProfilePreference(profilePreferenceDAO.getById(2));
		profilePreferenceDAO.getById(2).setUserGroup(ug1);
		
		UserGroup ug2 = new UserGroup();
		ug2.setName("Users");
		ug2.setId(2);
		Set<Authority> authorities2 = new HashSet<>();
		authorities2.add(authorityDAO.getByName("ROLE_USER"));
		ug1.setAuthorities(authorities2);
		
		repository.add(ug1);
		repository.add(ug2);
	}

	@Override
	public UserGroup getByName(String name) {
		for(UserGroup ug : repository) {
			if(StringUtils.equals(ug.getName(), name)) {
				return ug;
			}
		}
		return null;
	}
	
}
