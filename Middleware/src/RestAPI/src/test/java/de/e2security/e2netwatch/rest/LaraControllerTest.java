
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


package de.e2security.e2netwatch.rest;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.e2security.e2netwatch.model.JsonReturnData;
import de.e2security.e2netwatch.model.lara.LaraParams;
import de.e2security.e2netwatch.model.lara.LaraPolicyInfoParams;
import de.e2security.e2netwatch.model.lara.cisco.LaraCiscoACLReturnResult;
import de.e2security.e2netwatch.model.lara.policies.LaraPolicyInfoReturnResult;
import de.e2security.e2netwatch.model.lara.routers.LaraRoutersReturnResult;
import de.e2security.e2netwatch.model.lara.rules.LaraRulesReturnResult;
import de.e2security.e2netwatch.model.lara.rules.LaraTopHostsReturnResult;
import de.e2security.e2netwatch.rest.LaraController;

/**
 * Tests the LaraController RestController methods.
 *
 * @author Hrvoje
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/config/app-config.xml" })
@ActiveProfiles("test")
public class LaraControllerTest {

	@Autowired
	LaraController controller;
	
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
	 * Testing Lara router IP list API method
	 */
	@Test
	public void testLaraRouterList() {
		JsonReturnData<LaraRoutersReturnResult> result = controller.laraRouterList();
		
		Assert.assertTrue(result.isOK());
	}
	
	/**
	 * Testing Lara policy list API method
	 */
	@Test
	public void testLaraPolicyList() {
		LaraPolicyInfoParams params = new LaraPolicyInfoParams();
		JsonReturnData<LaraPolicyInfoReturnResult> result = controller.laraPolicyList(params);
		
		Assert.assertFalse(result.isOK());
	}
	
	/**
	 * Testing Lara Cisco API method
	 */
	@Test
	public void testLaraCisco() {
		LaraParams params = new LaraParams();
		JsonReturnData<LaraCiscoACLReturnResult> result = controller.laraCisco(params);
		
		Assert.assertFalse(result.isOK());
	}
	
	/**
	 * Testing Lara Rules API method
	 */
	@Test
	public void testLaraRules() {
		LaraParams params = new LaraParams();
		JsonReturnData<LaraRulesReturnResult> result = controller.laraRules(params);
		
		Assert.assertFalse(result.isOK());
	}
	
	/**
	 * Testing Lara Rules top sources for dynamic hosts API method
	 */
	@Test
	public void testLaraTopSrcDynamic() {
		LaraParams params = new LaraParams();
		JsonReturnData<LaraTopHostsReturnResult> result = controller.laraTopSrcDynamic(params);
		
		Assert.assertFalse(result.isOK());
	}
	
	/**
	 * Testing Lara Rules top destinations for dynamic hosts API method
	 */
	@Test
	public void testLaraTopDstDynamic() {
		LaraParams params = new LaraParams();
		JsonReturnData<LaraTopHostsReturnResult> result = controller.laraTopDstDynamic(params);
		
		Assert.assertFalse(result.isOK());
	}
	
	/**
	 * Testing Lara Rules top sources for static hosts API method
	 */
	@Test
	public void testLaraTopSrcStatic() {
		LaraParams params = new LaraParams();
		JsonReturnData<LaraTopHostsReturnResult> result = controller.laraTopSrcStatic(params);
		
		Assert.assertFalse(result.isOK());
	}
	
	/**
	 * Testing Lara Rules top destinations for static hosts API method
	 */
	@Test
	public void testLaraTopDstStatic() {
		LaraParams params = new LaraParams();
		JsonReturnData<LaraTopHostsReturnResult> result = controller.laraTopDstStatic(params);
		
		Assert.assertFalse(result.isOK());
	}
	
}
