package de.e2security.e2netwatch.usermanagement.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import de.e2security.e2netwatch.usermanagement.model.ProfilePreference;

/**
 * Repository for ProfilePreference
 * 
 * @author Hrvoje
 *
 */
public interface ProfilePreferenceRepository extends JpaRepository<ProfilePreference, Integer> {
	
	/**
	 * Getting the global profile preference
	 * 
	 * @return ProfilePreference
	 */
	@Query("SELECT pp FROM ProfilePreference pp WHERE pp.user is null and pp.userGroup is null")
	public ProfilePreference findGlobal();
	
}
