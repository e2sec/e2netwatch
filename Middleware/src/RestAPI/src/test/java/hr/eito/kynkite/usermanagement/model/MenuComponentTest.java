
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


package hr.eito.kynkite.usermanagement.model;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Tests the MenuComponent class.
 *
 * @author Hrvoje
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/config/app-config.xml" })
@ActiveProfiles("test")
public class MenuComponentTest {
	
	/**
	 * Test setting and getting properties
	 */
	@Test
	public void test() {
		Integer id = 1;
		String name = "name";
		String url = "url";
		String iconName = "iconName";
		String alignment = "right";
		Boolean nameHidden = true;
		Integer defaultPosition = 1;
		MenuComponent superMenuComponent = new MenuComponent();
		
		MenuComponent menuComponent = new MenuComponent();
		menuComponent.setId(id);
		menuComponent.setName(name);
		menuComponent.setUrl(url);
		menuComponent.setAlignment(alignment);
		menuComponent.setIconName(iconName);
		menuComponent.setDefaultPosition(defaultPosition);
		menuComponent.setNameHidden(nameHidden);
		menuComponent.setSuperMenuComponent(superMenuComponent);
		
		Assert.assertEquals("MenuComponent id set and get wrong", id, menuComponent.getId());
		Assert.assertEquals("MenuComponent name set and get wrong", name, menuComponent.getName());
		Assert.assertEquals("MenuComponent url set and get wrong", url, menuComponent.getUrl());
		Assert.assertEquals("MenuComponent iconName set and get wrong", iconName, menuComponent.getIconName());
		Assert.assertEquals("MenuComponent alignment set and get wrong", alignment, menuComponent.getAlignment());
		Assert.assertEquals("MenuComponent nameHidden set and get wrong", nameHidden, menuComponent.getNameHidden());
		Assert.assertEquals("MenuComponent defaultPosition set and get wrong", 
				defaultPosition, menuComponent.getDefaultPosition());
		Assert.assertEquals("MenuComponent superMenuComponent set and get wrong", 
				superMenuComponent, menuComponent.getSuperMenuComponent());
	}

}
