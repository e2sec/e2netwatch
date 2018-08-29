
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

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.e2security.e2netwatch.usermanagement.model.ProfileMenu;
import de.e2security.e2netwatch.usermanagement.model.ProfilePreference;
import de.e2security.e2netwatch.usermanagement.model.Timezone;
import de.e2security.e2netwatch.usermanagement.model.User;
import de.e2security.e2netwatch.usermanagement.model.UserGroup;

/**
 * Tests the ProfilePreference class.
 *
 * @author Hrvoje
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/config/app-config.xml" })
@ActiveProfiles("test")
public class ProfilePreferenceTest {
	
	/**
	 * Test setting and getting properties
	 */
	@Test
	public void test() {
		Integer id = 1;
		User user = new User();
		UserGroup userGroup = new UserGroup();
		Timezone timezone = new Timezone();
		List<ProfileMenu> profileMenus = new ArrayList<>();
		profileMenus.add(new ProfileMenu());
		profileMenus.add(new ProfileMenu());
		
		ProfilePreference profilePreference = new ProfilePreference();
		profilePreference.setId(id);
		profilePreference.setUser(user);
		profilePreference.setUserGroup(userGroup);
		profilePreference.setTimezone(timezone);
		
		Assert.assertEquals("ProfilePreference id set and get wrong", id, profilePreference.getId());
		Assert.assertEquals("ProfilePreference user set and get wrong", user, profilePreference.getUser());
		Assert.assertEquals("ProfilePreference userGroup set and get wrong", userGroup, profilePreference.getUserGroup());
		Assert.assertEquals("ProfilePreference timezone set and get wrong", timezone, profilePreference.getTimezone());
	}

}
