
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


package hr.eito.kynkite.rest;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import hr.eito.kynkite.usermanagement.model.menu.MenuReturnResult;
import hr.eito.model.JsonReturnData;

/**
 * Tests the UserManagementController RestController methods.
 *
 * @author Hrvoje
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/config/app-config.xml" })
@ActiveProfiles("test")
public class UserManagementControllerTest {

	@Autowired
	UserManagementController controller;
	
	/**
	 * Runs before the tests start.
	 */
	@BeforeClass
	public static void testStart() {}
	
	/**
	 * Runs after the tests end.
	 */
	@AfterClass
	public static void testEnd() {}
	
	/**
	 * Check we have a controller.
	 */
	@Test
	public void testController() {
		Assert.assertNotNull(controller);
	}

	/**
	 * Test getting Menu API method
	 */
	@Test
	public void testGettingMenuResult() {
		JsonReturnData<MenuReturnResult> result = controller.getMenuForCurrentUser();
		
		Assert.assertNotNull("JsonReturnData with MenuReturnResult should not be null", result);
		Assert.assertTrue("JsonReturnData with MenuReturnResult should return OK status", result.isOK());
	}
	
}
