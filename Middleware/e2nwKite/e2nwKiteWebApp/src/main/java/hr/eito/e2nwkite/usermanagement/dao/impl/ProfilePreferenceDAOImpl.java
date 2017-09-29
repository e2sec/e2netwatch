
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


package hr.eito.e2nwkite.usermanagement.dao.impl;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import hr.eito.e2nwkite.usermanagement.dao.ProfilePreferenceDAO;
import hr.eito.e2nwkite.usermanagement.model.ProfilePreference;

@Repository
@Transactional("userdbData")
public class ProfilePreferenceDAOImpl implements ProfilePreferenceDAO {
	
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public ProfilePreference getGlobal() {
		Session session = sessionFactory.getCurrentSession();
		Query query = session
				.createSQLQuery("select * from profile_preference where usr_id is null and ugr_id is null")
				.addEntity(ProfilePreference.class);
		ProfilePreference profilePreference = (ProfilePreference) query.uniqueResult();
		session.flush();
        return profilePreference;
	}

	@Override
	public ProfilePreference getById(Integer id) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session
				.createSQLQuery("select * from profile_preference where id = :id")
				.addEntity(ProfilePreference.class)
				.setParameter("id", id);
		ProfilePreference profilePreference = (ProfilePreference) query.uniqueResult();
		session.flush();
        return profilePreference;
	}
	
}
