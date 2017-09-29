
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


package hr.eito.e2nwkite.usermanagement.model.menu;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import hr.eito.e2nwkite.usermanagement.model.MenuComponent;
import hr.eito.e2nwkite.usermanagement.model.ProfileMenu;

/**
 * Tests the MenuReturnResultData.
 *
 * @author Hrvoje
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/config/app-config.xml" })
@ActiveProfiles("test")
public class MenuReturnResultDataTest {

	/**
	 * Test the default contructor
	 */
	@Test
	public void testDefaultConstructor() {
		MenuReturnResultData data = new MenuReturnResultData();

		Assert.assertNotNull("MenuReturnResultData object created with default constructor should not be null", data);
		Assert.assertEquals("MenuReturnResultData name should be null", null, data.getName()); 
		Assert.assertEquals("MenuReturnResultData url should be null", null, data.getUrl()); 
		Assert.assertEquals("MenuReturnResultData iconName should be null", null, data.getIconName()); 
		Assert.assertEquals("MenuReturnResultData alignment should be null", null, data.getAlignment()); 
		Assert.assertEquals("MenuReturnResultData nameHidden should be null", null, data.isNameHidden());
		Assert.assertEquals("MenuReturnResultData submenu should be null", null, data.getSubmenu());
	}
	
	/**
	 * Test the null input
	 */
	@Test
	public void testNullInputConstructor() {
		ProfileMenu menu = null;
		
		MenuReturnResultData data = new MenuReturnResultData(menu);

		Assert.assertNotNull("MenuReturnResultData object created with main constructor should not be null", data);
		Assert.assertEquals("MenuReturnResultData name should be null", null, data.getName()); 
		Assert.assertEquals("MenuReturnResultData url should be null", null, data.getUrl()); 
		Assert.assertEquals("MenuReturnResultData iconName should be null", null, data.getIconName()); 
		Assert.assertEquals("MenuReturnResultData alignment should be null", null, data.getAlignment()); 
		Assert.assertEquals("MenuReturnResultData nameHidden should be null", null, data.isNameHidden());
		Assert.assertEquals("MenuReturnResultData submenu should be null", null, data.getSubmenu());
	}
	
	/**
	 * Test - has ProfileMenu but not ProfileMenu.MenuComponent
	 */
	@Test
	public void testNullMenuComponent() {
		ProfileMenu menu = new ProfileMenu();
		menu.setMenuComponent(null);
		
		MenuReturnResultData data = new MenuReturnResultData(menu);

		Assert.assertNotNull("MenuReturnResultData object created with main constructor should not be null", data);
		Assert.assertEquals("MenuReturnResultData name should be null", null, data.getName()); 
		Assert.assertEquals("MenuReturnResultData url should be null", null, data.getUrl()); 
		Assert.assertEquals("MenuReturnResultData iconName should be null", null, data.getIconName()); 
		Assert.assertEquals("MenuReturnResultData alignment should be null", null, data.getAlignment()); 
		Assert.assertEquals("MenuReturnResultData nameHidden should be null", null, data.isNameHidden());
		Assert.assertEquals("MenuReturnResultData submenu should be null", null, data.getSubmenu());
	}
	
	/**
	 * Test - no profile submenus or submenu empty
	 */
	@Test
	public void testSubmenu() {
		ProfileMenu menu = new ProfileMenu();
		menu.setMenuComponent(new MenuComponent());
		
		MenuReturnResultData data;
		
		// Submenu is null
		menu.setProfileSubmenus(null);
		data = new MenuReturnResultData(menu);

		Assert.assertNotNull("MenuReturnResultData object created with main constructor should not be null", data);
		Assert.assertEquals("MenuReturnResultData submenu should be null", null, data.getSubmenu());
		
		// Submenu is empty
		menu.setProfileSubmenus(new ArrayList<>());
		data = new MenuReturnResultData(menu);

		Assert.assertNotNull("MenuReturnResultData object created with main constructor should not be null", data);
		Assert.assertEquals("MenuReturnResultData submenu should be null", null, data.getSubmenu());
	}
	
	/**
	 * Test with concrete data
	 */
	@Test
	public void test() {
		String name = "menu1";
		String url = "menu1_url";
		String iconName = "menu1_iconName";
		String alignment = "right";
		Boolean nameHidden = true;
		MenuComponent superMenuComponent = null;
		
		String firstSubmenuName = "submenu3";
		String secondSubmenuName = "divider";
		String thirdSubmenuName = "submenu4";
		
		MenuComponent menuComponent1 = new MenuComponent();
		menuComponent1.setId(1);
		menuComponent1.setName(name);
		menuComponent1.setUrl(url);
		menuComponent1.setIconName(iconName);
		menuComponent1.setAlignment(alignment);
		menuComponent1.setNameHidden(nameHidden);
		menuComponent1.setSuperMenuComponent(superMenuComponent);
		
		MenuComponent menuComponent3 = new MenuComponent();
		menuComponent3.setId(3);
		menuComponent3.setName(firstSubmenuName);
		menuComponent3.setUrl("submenu3_url");
		menuComponent3.setIconName("submenu3_iconName");
		menuComponent3.setAlignment(null);
		menuComponent3.setNameHidden(null);
		menuComponent3.setSuperMenuComponent(menuComponent1);
		
		MenuComponent menuComponent4 = new MenuComponent();
		menuComponent4.setId(4);
		menuComponent4.setName(thirdSubmenuName);
		menuComponent4.setUrl("submenu4_url");
		menuComponent4.setIconName("submenu4_iconName");
		menuComponent4.setAlignment(null);
		menuComponent4.setNameHidden(null);
		menuComponent4.setSuperMenuComponent(null);
		
		MenuComponent menuComponentDivider = new MenuComponent();
		menuComponentDivider.setId(5);
		menuComponentDivider.setName(secondSubmenuName);
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
		
		List<ProfileMenu> profileSubmenus1 = new ArrayList<>();
		profileSubmenus1.add(menu5);
		profileSubmenus1.add(menu3);
		profileSubmenus1.add(menu4);
		ProfileMenu menu1 = new ProfileMenu();
		menu1.setId(1);
		menu1.setPosition(1);
		menu1.setMenuComponent(menuComponent1);
		menu1.setProfileSubmenus(profileSubmenus1);
		
		MenuReturnResultData data = new MenuReturnResultData(menu1);

		Assert.assertNotNull("MenuReturnResultData object created with main constructor should not be null", data);
		
		Assert.assertEquals("MenuReturnResultData name not as expected", name, data.getName()); 
		Assert.assertEquals("MenuReturnResultData url not as expected", url, data.getUrl()); 
		Assert.assertEquals("MenuReturnResultData iconName not as expected", iconName, data.getIconName()); 
		Assert.assertEquals("MenuReturnResultData alignment not as expected", alignment, data.getAlignment()); 
		Assert.assertEquals("MenuReturnResultData nameHidden not as expected", nameHidden, data.isNameHidden());
		
		Assert.assertEquals("MenuReturnResultData submenu size not as expected", 3, data.getSubmenu().size());
		Assert.assertEquals("MenuReturnResultData submenu first element name not as expected", 
				firstSubmenuName, data.getSubmenu().get(0).getName());
		Assert.assertEquals("MenuReturnResultData submenu second element name not as expected", 
				secondSubmenuName, data.getSubmenu().get(1).getName());
		Assert.assertEquals("MenuReturnResultData submenu third element name not as expected", 
				thirdSubmenuName, data.getSubmenu().get(2).getName());
	}
	
}
