
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

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.e2security.e2netwatch.usermanagement.model.MenuComponent;
import de.e2security.e2netwatch.usermanagement.model.ProfileMenu;
import de.e2security.e2netwatch.usermanagement.model.menu.MenuReturnResult;

/**
 * Tests the MenuReturnResult.
 *
 * @author Hrvoje
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/config/app-config.xml" })
@ActiveProfiles("test")
public class MenuReturnResultTest {

	/**
	 * Test with a dummy result set.
	 */
	@Test
	public void testEmptyResults() {
		List<ProfileMenu> q = new ArrayList<>();

		MenuReturnResult d = new MenuReturnResult(q);

		Assert.assertEquals(0, d.getData().size());
		Assert.assertEquals(0, d.getRecordsTotal());
		Assert.assertEquals(0, d.getRecordsFiltered());
	}
	
	/**
	 * Test with concrete data
	 */
	@Test
	public void test() {
		// Data for checking order of root ProfileMenus
		String firstMenuName = "menu1";
		String secondMenuName = "menu2";
		
		// Creation of 2 root menus, first with 3 submenus, second with 1 submenu
		MenuComponent menuComponent1 = new MenuComponent();
		menuComponent1.setId(1);
		menuComponent1.setName(firstMenuName);
		menuComponent1.setUrl("menu1_url");
		menuComponent1.setIconName("menu1_iconName");
		menuComponent1.setAlignment(null);
		menuComponent1.setNameHidden(null);
		menuComponent1.setSuperMenuComponent(null);
		
		MenuComponent menuComponent2 = new MenuComponent();
		menuComponent2.setId(2);
		menuComponent2.setName(secondMenuName);
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
		
		ProfileMenu menu3 = new ProfileMenu();
		menu3.setId(3);
		menu3.setPosition(1);
		menu3.setMenuComponent(menuComponent3);
		menu3.setProfileSubmenus(null);
		
		ProfileMenu menu4 = new ProfileMenu();
		menu4.setId(4);
		menu4.setPosition(2);
		menu4.setMenuComponent(menuComponentDivider);
		menu4.setProfileSubmenus(null);
		
		ProfileMenu menu5 = new ProfileMenu();
		menu5.setId(5);
		menu5.setPosition(3);
		menu5.setMenuComponent(menuComponent4);
		menu5.setProfileSubmenus(null);
		
		ProfileMenu menu6 = new ProfileMenu();
		menu6.setId(1);
		menu6.setPosition(1);
		menu6.setMenuComponent(menuComponent4);
		menu6.setProfileSubmenus(null);
		
		List<ProfileMenu> profileSubmenus1 = new ArrayList<>();
		profileSubmenus1.add(menu5);
		profileSubmenus1.add(menu3);
		profileSubmenus1.add(menu4);
		ProfileMenu menu1 = new ProfileMenu();
		menu1.setId(1);
		menu1.setPosition(1);
		menu1.setMenuComponent(menuComponent1);
		menu1.setProfileSubmenus(profileSubmenus1);
		
		List<ProfileMenu> profileSubmenus2 = new ArrayList<>();
		profileSubmenus2.add(menu6);
		ProfileMenu menu2 = new ProfileMenu();
		menu2.setId(2);
		menu2.setPosition(2);
		menu2.setMenuComponent(menuComponent2);
		menu2.setProfileSubmenus(profileSubmenus2);
		
		// List of root menus
		List<ProfileMenu> q = new ArrayList<>();
		q.add(menu2);
		q.add(menu1);

		// Creating return result
		MenuReturnResult data = new MenuReturnResult(q);

		Assert.assertNotNull("MenuReturnResult data not null", data.getData());
		Assert.assertEquals("MenuReturnResult size of root menus not as expected", 2, data.getData().size());
		
		Assert.assertEquals("MenuReturnResultData first element name not as expected", 
				firstMenuName, data.getData().get(0).getName());
		Assert.assertEquals("MenuReturnResultData second element name not as expected", 
				secondMenuName, data.getData().get(1).getName());
	}
	
}
