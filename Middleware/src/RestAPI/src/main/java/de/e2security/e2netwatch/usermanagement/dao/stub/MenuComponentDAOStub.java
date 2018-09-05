
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


package de.e2security.e2netwatch.usermanagement.dao.stub;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import de.e2security.e2netwatch.usermanagement.dao.MenuComponentDAO;
import de.e2security.e2netwatch.usermanagement.model.MenuComponent;

@Repository
@Profile({"test"})
public class MenuComponentDAOStub implements MenuComponentDAO {
	
	private List<MenuComponent> repository;
	
	@PostConstruct
	public void init() {
		repository = new ArrayList<>();
		
		MenuComponent menuComponent1 = new MenuComponent();
		menuComponent1.setId(1);
		menuComponent1.setName("menu1");
		menuComponent1.setUrl("menu1_url");
		menuComponent1.setIconName("menu1_iconName");
		menuComponent1.setAlignment(null);
		menuComponent1.setNameHidden(null);
		menuComponent1.setSuperMenuComponent(null);
		
		MenuComponent menuComponent2 = new MenuComponent();
		menuComponent2.setId(2);
		menuComponent2.setName("menu2");
		menuComponent2.setUrl("menu2_url");
		menuComponent2.setIconName("menu2_iconName");
		menuComponent2.setAlignment("right");
		menuComponent2.setNameHidden(true);
		menuComponent2.setSuperMenuComponent(null);
		
		MenuComponent menuComponent3 = new MenuComponent();
		menuComponent3.setId(3);
		menuComponent3.setName("submenu3");
		menuComponent3.setUrl("submenu3_url");
		menuComponent3.setIconName("submenu3_iconName");
		menuComponent3.setAlignment(null);
		menuComponent3.setNameHidden(null);
		menuComponent3.setSuperMenuComponent(menuComponent1);
		
		MenuComponent menuComponent4 = new MenuComponent();
		menuComponent4.setId(4);
		menuComponent4.setName("submenu4");
		menuComponent4.setUrl("submenu4_url");
		menuComponent4.setIconName("submenu4_iconName");
		menuComponent4.setAlignment(null);
		menuComponent4.setNameHidden(null);
		menuComponent4.setSuperMenuComponent(null);
		
		MenuComponent menuComponentDivider = new MenuComponent();
		menuComponentDivider.setId(5);
		menuComponentDivider.setName("divider");
		menuComponentDivider.setUrl(null);
		menuComponentDivider.setIconName(null);
		menuComponentDivider.setAlignment(null);
		menuComponentDivider.setNameHidden(null);
		menuComponentDivider.setSuperMenuComponent(null);
		
		repository.add(menuComponent1);
		repository.add(menuComponent2);
		repository.add(menuComponent3);
		repository.add(menuComponent4);
		repository.add(menuComponentDivider);
	}

	@Override
	public MenuComponent getById(Integer id) {
		for(MenuComponent mcu : repository) {
			if(mcu.getId().equals(id)) {
				return mcu;
			}
		}
		return null;
	}
	
}
