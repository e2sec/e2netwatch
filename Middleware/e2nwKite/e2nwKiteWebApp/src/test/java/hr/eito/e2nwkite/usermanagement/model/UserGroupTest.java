
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
 * Tests the UserGroup class.
 *
 * @author Hrvoje
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/config/app-config.xml" })
@ActiveProfiles("test")
public class UserGroupTest {
	
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
		
		List<Authority> authorities = new ArrayList<>();
		authorities.add(aty1);
		authorities.add(aty2);
		
		Integer id = 1;
		String name = "name";
		String description = "description";
		
		UserGroup userGroup = new UserGroup();
		userGroup.setId(id);
		userGroup.setName(name);
		userGroup.setDescription(description);
		userGroup.setAuthorities(authorities);
		
		Assert.assertEquals("UserGroup id set and get wrong", id, userGroup.getId());
		Assert.assertEquals("UserGroup name set and get wrong", name, userGroup.getName());
		Assert.assertEquals("UserGroup description set and get wrong", description, userGroup.getDescription());
				
		Assert.assertEquals("Number of authorities for UserGroup is wrong", 2, userGroup.getAuthorities().size());
	}

}
