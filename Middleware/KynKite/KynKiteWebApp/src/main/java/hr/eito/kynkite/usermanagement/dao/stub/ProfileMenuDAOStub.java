
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


package hr.eito.kynkite.usermanagement.dao.stub;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import hr.eito.kynkite.usermanagement.dao.MenuComponentDAO;
import hr.eito.kynkite.usermanagement.dao.ProfileMenuDAO;
import hr.eito.kynkite.usermanagement.dao.ProfilePreferenceDAO;
import hr.eito.kynkite.usermanagement.model.ProfileMenu;

@Repository
@Profile({"test"})
public class ProfileMenuDAOStub implements ProfileMenuDAO {
	
	@Autowired
	private MenuComponentDAO menuComponentDAO;
	
	@Autowired
	private ProfilePreferenceDAO profilePreferenceDAO;
	
	private List<ProfileMenu> repository;
	
	@PostConstruct
	public void init() {
		repository = new ArrayList<>();
		
		ProfileMenu menu3 = new ProfileMenu();
		menu3.setId(3);
		menu3.setPosition(1);
		menu3.setMenuComponent(menuComponentDAO.getById(3));
		menu3.setProfileSubmenus(null);
		
		ProfileMenu menu4 = new ProfileMenu();
		menu4.setId(4);
		menu4.setPosition(2);
		menu4.setMenuComponent(menuComponentDAO.getById(5));
		menu4.setProfileSubmenus(null);
		
		ProfileMenu menu5 = new ProfileMenu();
		menu5.setId(5);
		menu5.setPosition(3);
		menu5.setMenuComponent(menuComponentDAO.getById(4));
		menu5.setProfileSubmenus(null);
		
		ProfileMenu menu6 = new ProfileMenu();
		menu6.setId(1);
		menu6.setPosition(1);
		menu6.setMenuComponent(menuComponentDAO.getById(4));
		menu6.setProfileSubmenus(null);
		
		List<ProfileMenu> profileSubmenus1 = new ArrayList<>();
		profileSubmenus1.add(menu5);
		profileSubmenus1.add(menu3);
		profileSubmenus1.add(menu4);
		ProfileMenu menu1 = new ProfileMenu();
		menu1.setId(1);
		menu1.setPosition(1);
		menu1.setMenuComponent(menuComponentDAO.getById(1));
		menu1.setProfileSubmenus(profileSubmenus1);
		menu1.setProfilePreference(profilePreferenceDAO.getById(2));
		
		List<ProfileMenu> profileSubmenus2 = new ArrayList<>();
		profileSubmenus2.add(menu6);
		ProfileMenu menu2 = new ProfileMenu();
		menu2.setId(2);
		menu2.setPosition(2);
		menu2.setMenuComponent(menuComponentDAO.getById(2));
		menu2.setProfileSubmenus(profileSubmenus2);
		menu2.setProfilePreference(profilePreferenceDAO.getById(2));
		
		repository.add(menu1);
		repository.add(menu2);
		repository.add(menu3);
		repository.add(menu4);
		repository.add(menu5);
		repository.add(menu6);
	}

	@Override
	public ProfileMenu getById(Integer id) {
		for(ProfileMenu pmu : repository) {
			if(pmu.getId().equals(id)) {
				return pmu;
			}
		}
		return null;
	}

	@Override
	public List<ProfileMenu> getAllByProfilePreference(Integer pprId) {
		List<ProfileMenu> profileMenus = new ArrayList<>();
		for(ProfileMenu pmu : repository) {
			if(pmu.getProfilePreference()!=null && pmu.getProfilePreference().getId().equals(pprId)) {
				profileMenus.add(pmu);
			}
		}
		return profileMenus;
	}
	
}
