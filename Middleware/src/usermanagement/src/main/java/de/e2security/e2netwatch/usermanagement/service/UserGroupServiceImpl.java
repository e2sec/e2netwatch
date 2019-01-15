package de.e2security.e2netwatch.usermanagement.service;

import java.util.List;
import java.util.stream.Collectors;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.e2security.e2netwatch.usermanagement.dao.UserGroupRepository;
import de.e2security.e2netwatch.usermanagement.dto.UserGroupDTO;
import de.e2security.e2netwatch.usermanagement.model.UserGroup;

/**
 * Implementation of User group service
 * 
 * @author Hrvoje
 *
 */
@Service
public class UserGroupServiceImpl implements UserGroupService {
	
	UserGroupRepository userGroupRepository;
	DozerBeanMapper mapper;
	
	@Autowired
	public UserGroupServiceImpl(UserGroupRepository userGroupRepository) {
		this.userGroupRepository = userGroupRepository;
		this.mapper = new DozerBeanMapper();
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.e2security.e2netwatch.usermanagement.service.UserGroupService#getUserGroups()
	 */
	@Override
	public List<UserGroupDTO> getUserGroups() {
		
		// Get all user group entities
		
		List<UserGroup> allUserGroups = userGroupRepository.findAll();
		
		// Map to DTO and return list
		
		return allUserGroups
				.stream()
				.map(from -> mapper.map(from, UserGroupDTO.class))
				.collect(Collectors.toList());
	}

}
