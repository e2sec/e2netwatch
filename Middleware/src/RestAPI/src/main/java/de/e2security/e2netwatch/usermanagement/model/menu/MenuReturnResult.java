
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


package de.e2security.e2netwatch.usermanagement.model.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import de.e2security.e2netwatch.usermanagement.model.ProfileMenu;

/**
 * Represents all necessary return data for Menu information
 * 
 * @author Hrvoje
 *
 */
public class MenuReturnResult {

	private int recordsTotal;
	private int recordsFiltered;
	private List<MenuReturnResultData> data;

	/**
	 * Create MenuReturnResult based on list of ProfileMenus
	 * 
	 * @param result - list of ProfileMenus
	 */
	public MenuReturnResult(final List<ProfileMenu> result) {
		data = new ArrayList<MenuReturnResultData>();

		this.recordsFiltered = 0;
		this.recordsTotal = 0;
		
		// If there is some ProfileMenu
		if (result!=null && result.size() > 0) {
			MenuReturnResultData menuReturnResultData;
			// Order list of ProfileMenus by position
			List<ProfileMenu> orderedMenus = 
					result
					.stream()
					.sorted((ProfileMenu pm1, ProfileMenu pm2) -> Integer.compare(pm1.getPosition(),pm2.getPosition()))
					.collect(Collectors.toList());
			// Convert all ProfileMenus to MenuReturnResultDatas
			for (ProfileMenu pm : orderedMenus) {
				menuReturnResultData = new MenuReturnResultData(pm);
				data.add(menuReturnResultData);
				this.recordsFiltered++;
				this.recordsTotal++;
			}
		}
	}
	
	/**
	 * @return the recordsTotal
	 */
	public int getRecordsTotal() {
		return recordsTotal;
	}

	/**
	 * @return the recordsFiltered
	 */
	public int getRecordsFiltered() {
		return recordsFiltered;
	}

	/**
	 * @return the data
	 */
	public List<MenuReturnResultData> getData() {
		return data;
	}
}
