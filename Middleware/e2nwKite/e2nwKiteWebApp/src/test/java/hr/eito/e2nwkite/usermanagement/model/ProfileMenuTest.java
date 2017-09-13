
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


package hr.eito.e2nwkite.usermanagement.model;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Tests the ProfileMenu class.
 *
 * @author Hrvoje
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/config/app-config.xml" })
@ActiveProfiles("test")
public class ProfileMenuTest {
	
	/**
	 * Test setting and getting properties
	 */
	@Test
	public void test() {
		List<ProfileMenu> profileSubmenus = new ArrayList<>();
		profileSubmenus.add(new ProfileMenu());
		profileSubmenus.add(new ProfileMenu());
		ProfilePreference profilePreference = new ProfilePreference();
		
		Integer id = 1;
		MenuComponent menuComponent = new MenuComponent();
		Integer position = 1;
		
		ProfileMenu profileMenu = new ProfileMenu();
		profileMenu.setId(id);
		profileMenu.setMenuComponent(menuComponent);
		profileMenu.setPosition(position);
		profileMenu.setProfileSubmenus(profileSubmenus);
		profileMenu.setProfilePreference(profilePreference);
		
		Assert.assertEquals("ProfileMenu id set and get wrong", id, profileMenu.getId());
		Assert.assertEquals("ProfileMenu menuComponent set and get wrong", menuComponent, profileMenu.getMenuComponent());
		Assert.assertEquals("ProfileMenu position set and get wrong", position, profileMenu.getPosition());
		Assert.assertEquals("ProfileMenu profilePreference set and get wrong", profilePreference, profileMenu.getProfilePreference());
		
		Assert.assertEquals("Number of profileSubmenus for ProfileMenu is wrong", 2, profileMenu.getProfileSubmenus().size());
	}

}
