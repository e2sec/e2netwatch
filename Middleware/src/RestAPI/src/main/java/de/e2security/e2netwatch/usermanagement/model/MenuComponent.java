
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


package de.e2security.e2netwatch.usermanagement.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * User entity for db table "menu_component"
 * 
 * @author Hrvoje
 *
 */
@Entity
@Table(name = "menu_component")
public class MenuComponent {
	
	@Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
	
	@Column(name = "name")
    private String name;
	
	@Column(name = "url")
    private String url;
	
	@Column(name = "icon_name")
    private String iconName;
	
	@Column(name = "alignment")
    private String alignment;
	
	@Column(name = "name_hidden", columnDefinition = "BIT", length = 1)
    @NotNull
    private Boolean nameHidden;
	
	@Column(name = "default_position")
    private Integer defaultPosition;
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="default_super_mnc_id")
    private MenuComponent superMenuComponent;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

	public Boolean getNameHidden() {
		return nameHidden;
	}

	public void setNameHidden(Boolean nameHidden) {
		this.nameHidden = nameHidden;
	}

	public Integer getDefaultPosition() {
		return defaultPosition;
	}

	public void setDefaultPosition(Integer defaultPosition) {
		this.defaultPosition = defaultPosition;
	}

	public MenuComponent getSuperMenuComponent() {
		return superMenuComponent;
	}

	public void setSuperMenuComponent(MenuComponent superMenuComponent) {
		this.superMenuComponent = superMenuComponent;
	}
    
}
