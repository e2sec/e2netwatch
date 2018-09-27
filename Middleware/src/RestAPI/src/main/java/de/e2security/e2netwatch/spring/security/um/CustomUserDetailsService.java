
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


package de.e2security.e2netwatch.spring.security.um;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import de.e2security.e2netwatch.usermanagement.dao.UserRepository;
import de.e2security.e2netwatch.usermanagement.model.Authority;
import de.e2security.e2netwatch.usermanagement.model.User;
import de.e2security.e2netwatch.usermanagement.model.UserGroup;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Username was not found: " + username);
        }

        final Set<Authority> authoritiesOfUser = user.getAuthorities();
        final Set<UserGroup> userGroupsOfUser = user.getUserGroups();
        for (final UserGroup userGroup : userGroupsOfUser) {
        	authoritiesOfUser.addAll(userGroup.getAuthorities());
        }
        List<String> listOfAuthorityNames = authoritiesOfUser.stream().map(Authority::getName).collect(Collectors.toList());
        String[] roleStringsAsArray = listOfAuthorityNames.toArray(new String[0]);
        final List<GrantedAuthority> auths = AuthorityUtils.createAuthorityList(roleStringsAsArray);

        return new UmUser(user.getUsername(), user.getPassword(), auths, user.getId());
    }
}
