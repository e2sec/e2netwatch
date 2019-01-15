package de.e2security.e2netwatch.security.userdetails;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import de.e2security.e2netwatch.usermanagement.model.Authority;
import de.e2security.e2netwatch.usermanagement.model.User;
import de.e2security.e2netwatch.usermanagement.model.UserGroup;
import de.e2security.e2netwatch.usermanagement.service.UsersService;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UsersService manager;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = manager.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Username was not found: " + username);
        }

        final Set<Authority> authoritiesOfUser = new HashSet<>();
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
