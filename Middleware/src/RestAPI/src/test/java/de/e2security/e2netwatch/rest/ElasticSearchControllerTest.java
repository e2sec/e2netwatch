
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
import de.e2security.e2netwatch.model.cmdb.CMDBParams;
import de.e2security.e2netwatch.model.cmdb.CMDBReturnResult;
import de.e2security.e2netwatch.model.hostreport.HostDetailsReportParams;
import de.e2security.e2netwatch.model.hostreport.HostDetailsReportReturnResult;
import de.e2security.e2netwatch.model.hostreport.HostReportParams;
import de.e2security.e2netwatch.model.hostreport.HostReportReturnResult;
import de.e2security.e2netwatch.model.newsticker.NewsTickerParams;
import de.e2security.e2netwatch.model.newsticker.NewsTickerReturnResult;
import de.e2security.e2netwatch.rest.ElasticSearchController;

/**
 * Tests the ElasticSearchController RestController methods.
 *
 * @author Hrvoje
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/config/app-config.xml" })
@ActiveProfiles("test")
public class ElasticSearchControllerTest {

	@Autowired
	ElasticSearchController controller;
	
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
	 * TODO: Test method for filtering data by IP address API method
	 */
//	@Test
//	public void testLoadDataByIP() {
//		NetflowParams params = new NetflowParams();
//		params.setIpAddress("");
//		String result = controller.loadDataByIP(params);
//		
//		Assert.assertTrue(result.isOK());
//	}
	
	/**
	 * Test Newsticker API method
	 */
	@Test
	public void testNewsticker() {
		NewsTickerParams params = new NewsTickerParams();
		JsonReturnData<NewsTickerReturnResult> result = controller.newsticker(params);
		
		Assert.assertTrue(result.isOK());
	}
	
	/**
	 * Test Host report API method
	 */
	@Test
	public void testHostReport() {
		HostReportParams params = new HostReportParams();
		JsonReturnData<HostReportReturnResult> result = controller.hostReport(params);
		
		Assert.assertTrue(result.isOK());
	}
	
	/**
	 * Test Host details report API method
	 */
	@Test
	public void testHostDetailsReport() {
		HostDetailsReportParams params = new HostDetailsReportParams();
		JsonReturnData<HostDetailsReportReturnResult> result = controller.hostDetailsReport(params);
		
		Assert.assertFalse(result.isOK());
	}
	
	/**
	 * Test CMDB report API method
	 */
	@Test
	public void testCmdbReport() {
		CMDBParams params = new CMDBParams();
		JsonReturnData<CMDBReturnResult> result = controller.cmdbReport(params);
		
		Assert.assertFalse(result.isOK());
	}
	
}
