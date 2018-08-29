
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


package de.e2security.e2netwatch.usermanagement.service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.collect.Collections2;

import de.e2security.e2netwatch.usermanagement.dao.UserDAO;
import de.e2security.e2netwatch.usermanagement.model.Authority;
import de.e2security.e2netwatch.usermanagement.model.User;
import de.e2security.e2netwatch.usermanagement.model.UserGroup;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	
    @Autowired
    private UserDAO userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.getByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Username was not found: " + username);
        }

        final Set<Authority> authoritiesOfUser = user.getAuthorities();
        final Set<UserGroup> userGroupsOfUser = user.getUserGroups();
        for (final UserGroup userGroup : userGroupsOfUser) {
        	authoritiesOfUser.addAll(userGroup.getAuthorities());
        }
        final Function<Object, String> toStringFunction = Functions.toStringFunction();
        final Collection<String> rolesToString = Collections2.transform(authoritiesOfUser, toStringFunction);
        final String[] roleStringsAsArray = rolesToString.toArray(new String[rolesToString.size()]);
        final List<GrantedAuthority> auths = AuthorityUtils.createAuthorityList(roleStringsAsArray);

        return new UmUser(user.getUsername(), user.getPassword(), auths, user.getId());
    }
}
