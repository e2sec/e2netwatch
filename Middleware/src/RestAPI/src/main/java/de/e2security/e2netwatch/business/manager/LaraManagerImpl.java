
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


package de.e2security.e2netwatch.business.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

import de.e2security.e2netwatch.business.dao.LaraDao;
import de.e2security.e2netwatch.model.JsonReturnData;
import de.e2security.e2netwatch.model.QueryResult;
import de.e2security.e2netwatch.model.lara.LaraParams;
import de.e2security.e2netwatch.model.lara.LaraPolicyInfoParams;
import de.e2security.e2netwatch.model.lara.cisco.LaraCiscoACLQueryResultField;
import de.e2security.e2netwatch.model.lara.cisco.LaraCiscoACLReturnResult;
import de.e2security.e2netwatch.model.lara.policies.LaraPolicyInfoQueryResultField;
import de.e2security.e2netwatch.model.lara.policies.LaraPolicyInfoReturnResult;
import de.e2security.e2netwatch.model.lara.routers.LaraRoutersReturnResult;
import de.e2security.e2netwatch.model.lara.rules.LaraRulesQueryResultField;
import de.e2security.e2netwatch.model.lara.rules.LaraRulesReturnResult;
import de.e2security.e2netwatch.model.lara.rules.LaraTopHostsQueryResultField;
import de.e2security.e2netwatch.model.lara.rules.LaraTopHostsReturnResult;


/**
 * Lara manager for managing Lara requests and data
 * 
 * @author Hrvoje
 *
 */
@Component
public class LaraManagerImpl implements LaraManager {
		
	@Autowired
	private LaraDao laraDao;
	
	/**
	 * Manage Lara Cisco ACL data
	 * 
	 * @param params parameters to filter date
	 *
	 * @return the Lara Cisco JSON data
	 */
	@Override
	public JsonReturnData<LaraCiscoACLReturnResult> laraCisco(final LaraParams params) {
		JsonReturnData<QueryResult<LaraCiscoACLQueryResultField>> queryResultRD = null;
		
		// Setting the parameters variables
		String policyId = null;
		if (params!=null) {
			policyId = params.getPolicyId();
		}
		
		// Policy ID must be set
		if (policyId!=null) {
			queryResultRD = this.laraDao.laraCiscoACL(policyId);
		} else {
			queryResultRD = new JsonReturnData<>();
		}
		
		if(queryResultRD.isOK()) {
			return new JsonReturnData<LaraCiscoACLReturnResult>(new LaraCiscoACLReturnResult(queryResultRD.getContent()));
		}

		return new JsonReturnData<LaraCiscoACLReturnResult>(queryResultRD.getErrorMessage());
	}
	
	/**
	 * Manage Lara Rules data
	 * 
	 * @param params parameters to filter date
	 * 
	 * @return the Lara Rules JSON data
	 */
	@Override
	public JsonReturnData<LaraRulesReturnResult> laraRules(final LaraParams params) {
		JsonReturnData<QueryResult<LaraRulesQueryResultField>> queryResultRD = null;

		// Setting the parameters variables
		String policyId = null;
		if (params!=null) {
			policyId = params.getPolicyId();
		}
		
		// Policy ID must be set
		if (policyId!=null) {
			queryResultRD = this.laraDao.laraRules(policyId);
		} else {
			queryResultRD = new JsonReturnData<>();
		}
		
		if(queryResultRD.isOK()) {
			return new JsonReturnData<LaraRulesReturnResult>(new LaraRulesReturnResult(queryResultRD.getContent()));
		}

		return new JsonReturnData<LaraRulesReturnResult>(queryResultRD.getErrorMessage());
	}
	
	/**
	 * Manage Lara Rules top sources for dynamic hosts data
	 * 
	 * @param params parameters to filter date
	 * 
	 * @return the Lara Rules top sources for dynamic hosts JSON data
	 */
	@Override
	public JsonReturnData<LaraTopHostsReturnResult> laraTopSrcDynamic(final LaraParams params) {
		JsonReturnData<QueryResult<LaraTopHostsQueryResultField>> queryResultRD = null;

		// Setting the parameters variables
		String policyId = null;
		if (params!=null) {
			policyId = params.getPolicyId();
		}
		
		// Policy ID must be set
		if (policyId!=null) {
			queryResultRD = this.laraDao.laraTopSrcDynamic(policyId);
		} else {
			queryResultRD = new JsonReturnData<>();
		}
		
		if(queryResultRD.isOK()) {
			return new JsonReturnData<LaraTopHostsReturnResult>(new LaraTopHostsReturnResult(queryResultRD.getContent()));
		}

		return new JsonReturnData<LaraTopHostsReturnResult>(queryResultRD.getErrorMessage());
	}
	
	/**
	 * Manage Lara Rules top destinations for dynamic hosts data
	 * 
	 * @param params parameters to filter date
	 * 
	 * @return the Lara Rules top destinations for dynamic hosts JSON data
	 */
	@Override
	public JsonReturnData<LaraTopHostsReturnResult> laraTopDstDynamic(final LaraParams params) {
		JsonReturnData<QueryResult<LaraTopHostsQueryResultField>> queryResultRD = null;

		// Setting the parameters variables
		String policyId = null;
		if (params!=null) {
			policyId = params.getPolicyId();
		}
		
		// Policy ID must be set
		if (policyId!=null) {
			queryResultRD = this.laraDao.laraTopDstDynamic(policyId);
		} else {
			queryResultRD = new JsonReturnData<>();
		}
		
		if(queryResultRD.isOK()) {
			return new JsonReturnData<LaraTopHostsReturnResult>(new LaraTopHostsReturnResult(queryResultRD.getContent()));
		}

		return new JsonReturnData<LaraTopHostsReturnResult>(queryResultRD.getErrorMessage());
	}
	
	/**
	 * Manage Lara Rules top sources for static hosts data
	 * 
	 * @param params parameters to filter date
	 * 
	 * @return the Lara Rules top sources for static hosts JSON data
	 */
	@Override
	public JsonReturnData<LaraTopHostsReturnResult> laraTopSrcStatic(final LaraParams params) {
		JsonReturnData<QueryResult<LaraTopHostsQueryResultField>> queryResultRD = null;

		// Setting the parameters variables
		String policyId = null;
		if (params!=null) {
			policyId = params.getPolicyId();
		}
		
		// Policy ID must be set
		if (policyId!=null) {
			queryResultRD = this.laraDao.laraTopSrcStatic(policyId);
		} else {
			queryResultRD = new JsonReturnData<>();
		}
		
		if(queryResultRD.isOK()) {
			return new JsonReturnData<LaraTopHostsReturnResult>(new LaraTopHostsReturnResult(queryResultRD.getContent()));
		}

		return new JsonReturnData<LaraTopHostsReturnResult>(queryResultRD.getErrorMessage());
	}
	
	/**
	 * Manage Lara Rules top destinations for static hosts data
	 * 
	 * @param params parameters to filter date
	 * 
	 * @return the Lara Rules top destinations for static hosts JSON data
	 */
	@Override
	public JsonReturnData<LaraTopHostsReturnResult> laraTopDstStatic(final LaraParams params) {
		JsonReturnData<QueryResult<LaraTopHostsQueryResultField>> queryResultRD = null;

		// Setting the parameters variables
		String policyId = null;
		if (params!=null) {
			policyId = params.getPolicyId();
		}
		
		// Policy ID must be set
		if (policyId!=null) {
			queryResultRD = this.laraDao.laraTopDstStatic(policyId);
		} else {
			queryResultRD = new JsonReturnData<>();
		}
		
		if(queryResultRD.isOK()) {
			return new JsonReturnData<LaraTopHostsReturnResult>(new LaraTopHostsReturnResult(queryResultRD.getContent()));
		}

		return new JsonReturnData<LaraTopHostsReturnResult>(queryResultRD.getErrorMessage());
	}
	
	/**
	 * Manage getting router IP list for LARA
	 *
	 * @return the router list JSON data
	 */
	@Override
	public JsonReturnData<LaraRoutersReturnResult> getRouterList() {
		JsonReturnData<JsonNode> queryResultRD = this.laraDao.getRouterList(); 
		if(queryResultRD.isOK()) {
			return new JsonReturnData<LaraRoutersReturnResult>(new LaraRoutersReturnResult(queryResultRD.getContent()));
		}

		return new JsonReturnData<LaraRoutersReturnResult>(queryResultRD.getErrorMessage());
	}
	
	/**
	 * Manage getting policy list for specific router
	 * 
	 * @param params parameters to filter date
	 * 
	 * @return the Lara policy list for router
	 */
	@Override
	public JsonReturnData<LaraPolicyInfoReturnResult> getPolicyList(final LaraPolicyInfoParams params) {
		JsonReturnData<QueryResult<LaraPolicyInfoQueryResultField>> queryResultRD = null;

		// Setting the parameters variables
		String routerIpAddress = null;
		if (params!=null) {
			routerIpAddress = params.getRouterIpAddress();
		}		
		
		// Router IP must be set
		if (routerIpAddress!=null) {
			queryResultRD = this.laraDao.getPolicyList(routerIpAddress);
		} else {
			queryResultRD = new JsonReturnData<>();
		}
		
		if(queryResultRD.isOK()) {
			return new JsonReturnData<LaraPolicyInfoReturnResult>(new LaraPolicyInfoReturnResult(queryResultRD.getContent()));
		}

		return new JsonReturnData<LaraPolicyInfoReturnResult>(queryResultRD.getErrorMessage());
	}

}
