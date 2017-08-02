
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


package hr.eito.kynkite.usermanagement.model;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Tests the Authority class.
 *
 * @author Hrvoje
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/config/app-config.xml" })
@ActiveProfiles("test")
public class AuthorityTest {
	
	/**
	 * Test setting and getting properties
	 */
	@Test
	public void test() {
		Integer id = 1;
		String name = "name";
		String description = "description";
		
		Authority authority = new Authority();
		authority.setId(id);
		authority.setName(name);
		authority.setDescription(description);
		
		Assert.assertEquals("Authority id set and get wrong", id, authority.getId());
		Assert.assertEquals("Authority name set and get wrong", name, authority.getName());
		Assert.assertEquals("Authority description set and get wrong", description, authority.getDescription());
	}

}
