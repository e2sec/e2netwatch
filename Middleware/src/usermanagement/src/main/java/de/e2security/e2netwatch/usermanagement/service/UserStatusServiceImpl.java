package de.e2security.e2netwatch.usermanagement.service;

import java.util.List;
import java.util.stream.Collectors;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.e2security.e2netwatch.usermanagement.dao.UserStatusRepository;
import de.e2security.e2netwatch.usermanagement.dto.UserStatusDTO;
import de.e2security.e2netwatch.usermanagement.model.UserStatus;

/**
 * Implementation of User status service
 * 
 * @author Hrvoje
 *
 */
@Service
public class UserStatusServiceImpl implements UserStatusService {
	
	UserStatusRepository userStatusRepository;
	DozerBeanMapper mapper;
	
	@Autowired
	public UserStatusServiceImpl(UserStatusRepository userStatusRepository) {
		this.userStatusRepository = userStatusRepository;
		this.mapper = new DozerBeanMapper();
	}

	/*
	 * (non-Javadoc)
	 * @see de.e2security.e2netwatch.usermanagement.service.UserStatusService#getUserStatuses()
	 */
	@Override
	public List<UserStatusDTO> getUserStatuses() {
		
		// Get all user status entities
		
		List<UserStatus> allUserStatuses = userStatusRepository.findAll();
		
		// Map to DTO and return list
		
		return allUserStatuses
				.stream()
				.map(from -> mapper.map(from, UserStatusDTO.class))
				.collect(Collectors.toList());
	}

}
