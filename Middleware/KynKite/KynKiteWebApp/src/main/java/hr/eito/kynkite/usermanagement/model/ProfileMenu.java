
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

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * User entity for db table "profile_menu"
 * 
 * @author Hrvoje
 *
 */
@Entity
@Table(name = "profile_menu")
public class ProfileMenu {
	
	@Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="mnc_id")
    private MenuComponent menuComponent;
	
	@Column(name = "position")
    private Integer position;
	
	@OneToMany
	@JoinColumn(name="super_prm_id")
	private List<ProfileMenu> profileSubmenus;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

	public MenuComponent getMenuComponent() {
		return menuComponent;
	}

	public void setMenuComponent(MenuComponent menuComponent) {
		this.menuComponent = menuComponent;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public List<ProfileMenu> getProfileSubmenus() {
		return profileSubmenus;
	}

	public void setProfileSubmenus(List<ProfileMenu> profileSubmenus) {
		this.profileSubmenus = profileSubmenus;
	}
    
}
