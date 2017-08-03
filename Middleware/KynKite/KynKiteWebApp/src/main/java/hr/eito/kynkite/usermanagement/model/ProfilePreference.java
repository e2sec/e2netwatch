
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
 * User entity for db table "profile_preference"
 * 
 * @author Hrvoje
 *
 */
@Entity
@Table(name = "profile_preference")
public class ProfilePreference {
	
	@Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="usr_id")
    private User user;

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ugr_id")
    private UserGroup userGroup;
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="default_tzn_id")
    private Timezone timezone;
	
	@OneToMany
	@JoinColumn(name="ppr_id")
	private List<ProfileMenu> profileMenus;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public UserGroup getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(UserGroup userGroup) {
		this.userGroup = userGroup;
	}

	public Timezone getTimezone() {
		return timezone;
	}

	public void setTimezone(Timezone timezone) {
		this.timezone = timezone;
	}

	public List<ProfileMenu> getProfileMenus() {
		return profileMenus;
	}

	public void setProfileMenus(List<ProfileMenu> profileMenus) {
		this.profileMenus = profileMenus;
	}
    
}
