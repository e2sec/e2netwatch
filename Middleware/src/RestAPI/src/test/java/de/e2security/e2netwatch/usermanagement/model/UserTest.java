
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


package de.e2security.e2netwatch.usermanagement.model;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.e2security.e2netwatch.usermanagement.model.Authority;
import de.e2security.e2netwatch.usermanagement.model.User;
import de.e2security.e2netwatch.usermanagement.model.UserGroup;

/**
 * Tests the User class.
 *
 * @author Hrvoje
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/config/app-config.xml" })
@ActiveProfiles("test")
public class UserTest {
	
	/**
	 * Test setting and getting properties
	 */
	@Test
	public void test() {
		// Preparing all necessary test data
		Authority aty1 = new Authority();
		aty1.setId(1);
		aty1.setName("ROLE_USER");
		Authority aty2 = new Authority();
		aty2.setId(2);
		aty2.setName("ROLE_ADMIN");
		Authority aty3 = new Authority();
		aty3.setId(3);
		aty3.setName("ROLE_SECURITY");
		Authority aty4 = new Authority();
		aty4.setId(4);
		aty4.setName("ROLE_LARA");
		
		Set<Authority> authoritiesUserGroup1 = new HashSet<>();
		authoritiesUserGroup1.add(aty1);
		authoritiesUserGroup1.add(aty2);
		Set<Authority> authoritiesUserGroup2 = new HashSet<>();
		authoritiesUserGroup2.add(aty3);
		
		UserGroup userGroup1 = new UserGroup();
		userGroup1.setAuthorities(authoritiesUserGroup1);
		UserGroup userGroup2 = new UserGroup();
		userGroup2.setAuthorities(authoritiesUserGroup2);
		
		Set<UserGroup> userGroups = new HashSet<>();
		userGroups.add(userGroup1);
		userGroups.add(userGroup2);
		
		// AUthorities list directly for user
		Set<Authority> authoritiesUser = new HashSet<>();
		authoritiesUser.add(aty4);
		
		Integer id = 1;
		String username = "username";
		String password = "password";
		String firstName = "John";
		String lastName = "Doe";
		String email = "mail@mail";
		
		User user = new User();
		user.setId(id);
		user.setUsername(username);
		user.setPassword(password);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEmail(email);
		user.setAuthorities(authoritiesUser);
		user.setUserGroups(userGroups);
		
		Assert.assertEquals("User id set and get wrong", id, user.getId());
		Assert.assertEquals("User username set and get wrong", username, user.getUsername());
		Assert.assertEquals("User password set and get wrong", password, user.getPassword());
		Assert.assertEquals("User firstName set and get wrong", firstName, user.getFirstName());
		Assert.assertEquals("User lastName set and get wrong", lastName, user.getLastName());
		Assert.assertEquals("User email set and get wrong", email, user.getEmail());
		
		Assert.assertEquals("User's UserGroup number wrong", 2, user.getUserGroups().size());
		
		Assert.assertEquals("Number of authorities for User is wrong", 1, user.getAuthorities().size());
		
		
	}

}
