
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


package hr.eito.kynkite.usermanagement.helper.impl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import hr.eito.kynkite.usermanagement.helper.RequestMiner;

/**
 * Implementation of RequestMiner interface
 * 
 * @author Hrvoje
 *
 */
@Component
@Profile({"dev","prod"})
public class RequestMinerImpl implements RequestMiner {
	
    private String USER_COOKIE_NAME = "c_user";
	
	@Autowired
	private HttpServletRequest request;
	
	/**
	 * Getting username for the current user.
	 * <p>From request cookie username is pulled
	 * 
	 * @return usernama as String or null if nothing found
	 */
	@Override
	public String getCurrentUsername() {
		String username = null;
		if (request.getCookies() != null) {
			for (int i = 0; i < request.getCookies().length; i++) {
	            if (request.getCookies()[i].getName().equals(USER_COOKIE_NAME)) {
	            	username = request.getCookies()[i].getValue();
	            }
	        }
        }
		return username;
	}
	
}
