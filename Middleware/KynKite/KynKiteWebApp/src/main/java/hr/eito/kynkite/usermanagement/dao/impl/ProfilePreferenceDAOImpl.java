
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

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import hr.eito.kynkite.usermanagement.dao.ProfilePreferenceDAO;
import hr.eito.kynkite.usermanagement.model.ProfilePreference;

@Repository
@Transactional("userTransactionManager")
public class ProfilePreferenceDAOImpl implements ProfilePreferenceDAO {
	
	@PersistenceContext(unitName="userEntityManager")
	private EntityManager em;

	@Override
	public ProfilePreference getGlobal() {
		Query query = em.createNativeQuery(
			    "select * from profile_preference where usr_id is null and ugr_id is null", ProfilePreference.class);
		try {
			ProfilePreference profilePreference = (ProfilePreference) query.getSingleResult();
			return profilePreference;
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public ProfilePreference getById(Integer id) {
		Query query = em.createQuery(
			    "select * from profile_preference where id = :id")
				.setParameter("id", id);
		try {
			ProfilePreference profilePreference = (ProfilePreference) query.getSingleResult();
			return profilePreference;
		} catch (NoResultException e) {
			return null;
		}
	}
	
}
