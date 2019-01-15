package de.e2security.e2netwatch.usermanagement.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import de.e2security.e2netwatch.usermanagement.model.UserGroup;

/**
 * Repository for UserGroup
 * 
 * @author Hrvoje
 *
 */
public interface UserGroupRepository extends JpaRepository<UserGroup, Integer> {
	
}
