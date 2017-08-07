
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


package hr.eito.kynkite.usermanagement.model.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import hr.eito.kynkite.usermanagement.model.MenuComponent;
import hr.eito.kynkite.usermanagement.model.ProfileMenu;

/**
 * Class encapsulating Menu Component information
 * 
 * @author Hrvoje
 *
 */
@JsonInclude(Include.NON_NULL)
public class MenuReturnResultData {
	
	private String name;
	private String url;
	private String iconName;
	private String alignment;
	private Boolean nameHidden;
	private List<MenuReturnResultData> submenu;
	
	/**
	 * Create MenuReturnResultData based on ProfileMenu DB mapped class object 
	 * 
	 * @param menu ProfileMenu object from which is created
	 */
	public MenuReturnResultData(final ProfileMenu menu) {
		if (menu!=null && menu.getMenuComponent()!=null) {
			// Defining direct values
			MenuComponent menuComponent = menu.getMenuComponent();
			this.name = menuComponent.getName();
			this.url = menuComponent.getUrl();
			this.iconName = menuComponent.getIconName();
			this.alignment = menuComponent.getAlignment();
			this.nameHidden = menuComponent.getNameHidden();
			// Defining ordered submenu list
			if (menu.getProfileSubmenus()!=null && menu.getProfileSubmenus().size() > 0) {
				// Ordering submenu list
				List<ProfileMenu> submenus = 
						menu.getProfileSubmenus()
						.stream()
						.sorted((ProfileMenu pm1, ProfileMenu pm2) -> Integer.compare(pm1.getPosition(),pm2.getPosition()))
						.collect(Collectors.toList());
				// Converting submenu items to MenuReturnResultData
				submenu = new ArrayList<>();
				for (ProfileMenu sm : submenus) {
					submenu.add(new MenuReturnResultData(sm));
				}
			}
		}
	}
	
	/**
	 * Default constructor for testing
	 */
	public MenuReturnResultData() {}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getIconName() {
		return iconName;
	}
	public void setIconName(String iconName) {
		this.iconName = iconName;
	}
	public String getAlignment() {
		return alignment;
	}
	public void setAlignment(String alignment) {
		this.alignment = alignment;
	}
	public Boolean isNameHidden() {
		return nameHidden;
	}
	public void setNameHidden(Boolean nameHidden) {
		this.nameHidden = nameHidden;
	}
	public List<MenuReturnResultData> getSubmenu() {
		return submenu;
	}
	public void setSubmenu(List<MenuReturnResultData> submenu) {
		this.submenu = submenu;
	}
	
}
