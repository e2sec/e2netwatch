
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

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Tests the Timezone class.
 *
 * @author Hrvoje
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/config/app-config.xml" })
@ActiveProfiles("test")
public class TimezoneTest {
	
	/**
	 * Test setting and getting properties
	 */
	@Test
	public void test() {
		Integer id = 1;
		String name = "name";
		BigDecimal offset = new BigDecimal(3.5);
		
		Timezone timezone = new Timezone();
		timezone.setId(id);
		timezone.setName(name);
		timezone.setOffset(offset);
		
		Assert.assertEquals("Timezone id set and get wrong", id, timezone.getId());
		Assert.assertEquals("Timezone name set and get wrong", name, timezone.getName());
		Assert.assertEquals("Timezone offset set and get wrong", offset, timezone.getOffset());
	}

}
