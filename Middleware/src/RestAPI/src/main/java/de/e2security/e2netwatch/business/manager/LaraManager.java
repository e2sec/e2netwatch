
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

import de.e2security.e2netwatch.model.JsonReturnData;
import de.e2security.e2netwatch.model.lara.LaraParams;
import de.e2security.e2netwatch.model.lara.LaraPolicyInfoParams;
import de.e2security.e2netwatch.model.lara.cisco.LaraCiscoACLReturnResult;
import de.e2security.e2netwatch.model.lara.policies.LaraPolicyInfoReturnResult;
import de.e2security.e2netwatch.model.lara.routers.LaraRoutersReturnResult;
import de.e2security.e2netwatch.model.lara.rules.LaraRulesReturnResult;
import de.e2security.e2netwatch.model.lara.rules.LaraTopHostsReturnResult;

/**
 * Lara manager for managing Lara requests and data
 * 
 * @author Hrvoje
 *
 */
public interface LaraManager {
	
	/**
	 * Manage Lara Cisco ACL data
	 *
	 * @param params id of the policy which consists necessary data
	 *
	 * @return the Lara Cisco JSON data
	 */
	JsonReturnData<LaraCiscoACLReturnResult> laraCisco(final LaraParams params);
	
	/**
	 * Manage Lara Rules data
	 *
	 * @param params id of the policy which consists necessary data
	 *
	 * @return the Lara Rules JSON data
	 */
	JsonReturnData<LaraRulesReturnResult> laraRules(final LaraParams params);
	
	/**
	 * Manage Lara Rules top sources for dynamic hosts data
	 *
	 * @param params id of the policy which consists necessary data
	 *
	 * @return the Lara Rules top sources for dynamic hosts JSON data
	 */
	JsonReturnData<LaraTopHostsReturnResult> laraTopSrcDynamic(final LaraParams params);
	
	/**
	 * Manage Lara Rules top destinations for dynamic hosts  data
	 *
	 * @param params id of the policy which consists necessary data
	 *
	 * @return the Lara Rules top destinations for dynamic hosts JSON data
	 */
	JsonReturnData<LaraTopHostsReturnResult> laraTopDstDynamic(final LaraParams params);
	
	/**
	 * Manage Lara Rules top sources for static hosts data
	 *
	 * @param params id of the policy which consists necessary data
	 *
	 * @return the Lara Rules top sources for static hosts JSON data
	 */
	JsonReturnData<LaraTopHostsReturnResult> laraTopSrcStatic(final LaraParams params);
	
	/**
	 * Manage Lara Rulus top destinations for static hosts data
	 *
	 * @param params id of the policy which consists necessary data
	 *
	 * @return the Lara Rules top destinations for static hosts JSON data
	 */
	JsonReturnData<LaraTopHostsReturnResult> laraTopDstStatic(final LaraParams params);
	
	/**
	 * Manage getting router IP list for LARA
	 *
	 * @return the router list JSON data
	 */
	JsonReturnData<LaraRoutersReturnResult> getRouterList();
	
	/**
	 * Manage getting policy list for specific router
	 *
	 * @param params router IP address for getting router's policies
	 *
	 * @return the Lara policy list for router
	 */
	JsonReturnData<LaraPolicyInfoReturnResult> getPolicyList(final LaraPolicyInfoParams params);
}
