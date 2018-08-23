
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


package hr.eito.kynkite.usermanagement.dao;

import java.util.List;

import hr.eito.kynkite.usermanagement.model.ProfileMenu;

/**
 * DAO for ProfileMenu
 * 
 * @author Hrvoje
 *
 */
public interface ProfileMenuDAO {
	
	/**
	 * Getting the ProfileMenu by id
	 * 
	 * @param id
	 * @return ProfileMenu
	 */
	public ProfileMenu getById(final Integer id);
	
	/**
	 * Getting the list of ProfileMenus by ProfilePreference
	 * 
	 * @param pprId ProfilePreference ID
	 * 
	 * @return list of ProfileMenus
	 */
	public List<ProfileMenu> getAllByProfilePreference(final Integer pprId);

}
