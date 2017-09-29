
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


package hr.eito.e2nwkite.usermanagement.dao.stub;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import hr.eito.e2nwkite.usermanagement.dao.AuthorityDAO;
import hr.eito.e2nwkite.usermanagement.model.Authority;

@Repository
@Profile({"test"})
public class AuthorityDAOStub implements AuthorityDAO {
	
	private List<Authority> repository;
	
	@PostConstruct
	public void init() {
		repository = new ArrayList<>();
		
		Authority a1 = new Authority();
		a1.setId(1);
		a1.setName("ROLE_ADMIN");
		
		Authority a2 = new Authority();
		a2.setId(2);
		a2.setName("ROLE_USER");
		
		repository.add(a1);
		repository.add(a2);
	}

	@Override
	public Authority getByName(String name) {
		for(Authority a : repository) {
			if(StringUtils.equals(a.getName(), name)) {
				return a;
			}
		}
		return null;
	}
	
}
