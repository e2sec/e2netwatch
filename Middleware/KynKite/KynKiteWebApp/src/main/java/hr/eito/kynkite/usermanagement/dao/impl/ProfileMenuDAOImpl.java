
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


package hr.eito.kynkite.usermanagement.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import hr.eito.kynkite.usermanagement.dao.ProfileMenuDAO;
import hr.eito.kynkite.usermanagement.model.ProfileMenu;

@Repository
@Transactional("userTransactionManager")
public class ProfileMenuDAOImpl implements ProfileMenuDAO {
	
	@PersistenceContext(unitName="userEntityManager")
	private EntityManager em;

	@Override
	public ProfileMenu getById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ProfileMenu> getAllByProfilePreference(Integer pprId) {
		Query query = em.createNativeQuery(
			    "select * from profile_menu where ppr_id = :ppr_id", ProfileMenu.class)
				.setParameter("ppr_id", pprId);
		List<ProfileMenu> profileMenus = query.getResultList();
		return profileMenus;
	}
	
}
