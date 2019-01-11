package de.e2security.e2netwatch.usermanagement.service;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.e2security.e2netwatch.usermanagement.dao.ProfilePreferenceRepository;
import de.e2security.e2netwatch.usermanagement.dao.UserRepository;
import de.e2security.e2netwatch.usermanagement.dto.ProfilePreferenceDTO;
import de.e2security.e2netwatch.usermanagement.model.ProfilePreference;
import de.e2security.e2netwatch.usermanagement.model.User;

/**
 * Implementation of Profile preference service
 * 
 * @author Hrvoje
 *
 */
@Service
public class ProfilePreferenceServiceImpl implements ProfilePreferenceService {
		
	RequestMiner requestMiner;
	
	UserRepository userRepository;
	ProfilePreferenceRepository profilePreferenceRepository;
	
	DozerBeanMapper mapper;
	
	@Autowired
	public ProfilePreferenceServiceImpl(RequestMiner requestMiner, UserRepository userRepository, 
			ProfilePreferenceRepository profilePreferenceRepository) {
		this.requestMiner = requestMiner;
		this.userRepository = userRepository;
		this.profilePreferenceRepository = profilePreferenceRepository;
		this.mapper = new DozerBeanMapper();
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.e2security.e2netwatch.usermanagement.service.ProfilePreferenceService#getForCurrent()
	 */
	@Override
	public ProfilePreferenceDTO getForCurrent() {
		
		// Get current user
		
		User user = userRepository.findByUsername(requestMiner.getCurrentUsername());
		
		ProfilePreferenceDTO profilePreferenceDTO = null;
		if (user.getProfilePreference() != null) {
			profilePreferenceDTO = mapper.map(user.getProfilePreference(), ProfilePreferenceDTO.class);	
		} else {
			profilePreferenceDTO = getGlobal();
		}
		
		return profilePreferenceDTO;
		
	}

	/*
	 * (non-Javadoc)
	 * @see de.e2security.e2netwatch.usermanagement.service.ProfilePreferenceService#getGlobal()
	 */
	@Override
	public ProfilePreferenceDTO getGlobal() {
		
		// Get global profile preference
		
		ProfilePreference globalProfilePreference = profilePreferenceRepository.findGlobal();
		
		ProfilePreferenceDTO profilePreferenceDto = mapper.map(globalProfilePreference, ProfilePreferenceDTO.class);
		return profilePreferenceDto;
	}
	
	
}
