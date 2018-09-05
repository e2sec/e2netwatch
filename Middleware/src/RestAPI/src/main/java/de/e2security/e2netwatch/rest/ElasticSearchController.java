
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.e2security.e2netwatch.business.manager.ElasticSearchManager;
import de.e2security.e2netwatch.model.JsonReturnData;
import de.e2security.e2netwatch.model.cmdb.CMDBParams;
import de.e2security.e2netwatch.model.cmdb.CMDBReturnResult;
import de.e2security.e2netwatch.model.hostreport.HostDetailsReportParams;
import de.e2security.e2netwatch.model.hostreport.HostDetailsReportReturnResult;
import de.e2security.e2netwatch.model.hostreport.HostReportParams;
import de.e2security.e2netwatch.model.hostreport.HostReportReturnResult;
import de.e2security.e2netwatch.model.netflow.NetflowParams;
import de.e2security.e2netwatch.model.newsticker.NewsTickerParams;
import de.e2security.e2netwatch.model.newsticker.NewsTickerReturnResult;
import de.e2security.e2netwatch.model.ntv.NetflowReturnResult;
import de.e2security.e2netwatch.utils.Mappings;

/**
 * Creates the REST endpoints for the middleware. These are the URLs that the front end calls to get
 * data from ElasticSearch.
 *
 * @author Marko
 *
 */
@RestController
@RequestMapping(value = Mappings.ELASTICSEARCH)
public class ElasticSearchController {

	@Autowired
	ElasticSearchManager elasticsSearchManager;
	
	/**
	 * Method for filtering data by IP address
	 *
	 * @param params the parameters for filtering data
	 *
	 * @return The result 
	 */
	@RequestMapping(value = "/resets", method = RequestMethod.POST, headers = "Accept=application/json")
	public JsonReturnData<NetflowReturnResult> loadDataByIP(@RequestBody NetflowParams params) {
		return elasticsSearchManager.loadDataByIP(params);
	}

	/**
	 * Newsticker POST method. Takes a request body that is JSON.
	 *
	 * @param params the parameters for the query
	 *
	 * @return result data as JSON.
	 */
	@RequestMapping(value = "/newsTicker", method = RequestMethod.POST, headers = "Accept=application/json")
	JsonReturnData<NewsTickerReturnResult> newsticker(@RequestBody NewsTickerParams params) {
		JsonReturnData<NewsTickerReturnResult> newsTickerJson = this.elasticsSearchManager.newsticker(params);
		return newsTickerJson;
	}

	/**
	 * Execute and return data for the parameterized CMDB report.
	 *
	 * @param params the parameters for the query
	 *
	 * @return the CMDB report results
	 */
	@RequestMapping(value = "/cmdbReport", method = RequestMethod.POST, headers = "Accept=application/json")
	JsonReturnData<CMDBReturnResult> cmdbReport(@RequestBody CMDBParams params) {
		return elasticsSearchManager.cmdbReport(params);
	}
	
	/**
	 * HostReport POST method. Takes a request body that is JSON.
	 *
	 * @param params the parameters for the query
	 *
	 * @return result data as JSON.
	 */
	@RequestMapping(value = "/hostReport", method = RequestMethod.POST, headers = "Accept=application/json")
	JsonReturnData<HostReportReturnResult> hostReport(@RequestBody HostReportParams params) {
		JsonReturnData<HostReportReturnResult> hostReportJson = elasticsSearchManager.hostReport(params);
		return hostReportJson;
	}

	/**
	 * HostDetailsReport POST method. Takes a request body that is JSON.
	 *
	 * @param params the parameters for the query
	 *
	 * @return result data as JSON.
	 */
	@RequestMapping(value = "/hostDetailsReport", method = RequestMethod.POST, headers = "Accept=application/json")
	JsonReturnData<HostDetailsReportReturnResult> hostDetailsReport(@RequestBody HostDetailsReportParams params) {
		JsonReturnData<HostDetailsReportReturnResult> hostDetailsReportJson = elasticsSearchManager.hostDetailsReport(params);
		return hostDetailsReportJson;
	}
}
