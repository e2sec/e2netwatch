
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


package hr.eito.e2nwkite.usermanagement.dao.stub;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import hr.eito.e2nwkite.usermanagement.dao.AuthorityDAO;
import hr.eito.e2nwkite.usermanagement.dao.ProfilePreferenceDAO;
import hr.eito.e2nwkite.usermanagement.dao.UserGroupDAO;
import hr.eito.e2nwkite.usermanagement.model.Authority;
import hr.eito.e2nwkite.usermanagement.model.UserGroup;

@Repository
@Profile({"test"})
public class UserGroupDAOStub implements UserGroupDAO {
	
	@Autowired
	private AuthorityDAO authorityDAO;
	
	@Autowired
	private ProfilePreferenceDAO profilePreferenceDAO;
	
	private List<UserGroup> repository;
	
	@PostConstruct
	public void init() {
		repository = new ArrayList<>();
		
		UserGroup ug1 = new UserGroup();
		ug1.setName("Administrators");
		ug1.setId(1);
		List<Authority> authorities1 = new ArrayList<>();
		authorities1.add(authorityDAO.getByName("ROLE_ADMIN"));
		ug1.setAuthorities(authorities1);
		ug1.setProfilePreference(profilePreferenceDAO.getById(2));
		profilePreferenceDAO.getById(2).setUserGroup(ug1);
		
		UserGroup ug2 = new UserGroup();
		ug2.setName("Users");
		ug2.setId(2);
		List<Authority> authorities2 = new ArrayList<>();
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
