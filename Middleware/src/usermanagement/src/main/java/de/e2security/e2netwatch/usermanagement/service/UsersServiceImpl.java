package de.e2security.e2netwatch.usermanagement.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.google.common.base.Preconditions;

import de.e2security.e2netwatch.usermanagement.dao.UserGroupRepository;
import de.e2security.e2netwatch.usermanagement.dao.UserRepository;
import de.e2security.e2netwatch.usermanagement.dao.UserStatusRepository;
import de.e2security.e2netwatch.usermanagement.dto.EmailCheckDTO;
import de.e2security.e2netwatch.usermanagement.dto.ListDTO;
import de.e2security.e2netwatch.usermanagement.dto.PasswordResetDTO;
import de.e2security.e2netwatch.usermanagement.dto.UserAdminUpdateDTO;
import de.e2security.e2netwatch.usermanagement.dto.UserDTO;
import de.e2security.e2netwatch.usermanagement.dto.UserInsertDTO;
import de.e2security.e2netwatch.usermanagement.dto.UserUpdateDTO;
import de.e2security.e2netwatch.usermanagement.dto.UsernameCheckDTO;
import de.e2security.e2netwatch.usermanagement.model.User;
import de.e2security.e2netwatch.usermanagement.model.UserGroup;
import de.e2security.e2netwatch.usermanagement.model.UserStatus;
import de.e2security.e2netwatch.utils.constants.CustomError;

/**
 * UserManagement manager implementation for managing User requests and data
 * 
 * @author Hrvoje
 *
 */
@Service
public class UsersServiceImpl implements UsersService {
		
	RequestMiner requestMiner;
	PasswordEncoder passwordEncoder;
	
	UserRepository userRepository;
	UserStatusRepository userStatusRepository;
	UserGroupRepository userGroupRepository;
	
	DozerBeanMapper mapper;
	
	private static final int PASSWORD_MIN_LENGTH = 8;
	private static final int USERNAME_MIN_LENGTH = 4;
	
	@Autowired
	public UsersServiceImpl(RequestMiner requestMiner, PasswordEncoder argonPasswordEncoder, UserRepository userRepository, 
			UserStatusRepository userStatusRepository, UserGroupRepository userGroupRepository) {
		this.requestMiner = requestMiner;
		this.passwordEncoder = argonPasswordEncoder;
		this.userRepository = userRepository;
		this.userStatusRepository = userStatusRepository;
		this.userGroupRepository = userGroupRepository;
		this.mapper = new DozerBeanMapper();
	}

	/*
	 * (non-Javadoc)
	 * @see de.e2security.e2netwatch.usermanagement.service.UserManagementService#findByUsername(java.lang.String)
	 */
	@Override
	public User findByUsername(String username) {
		User user = userRepository.findByUsername(username);
		return user;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.e2security.e2netwatch.business.manager.UserManagementManager#getProfile()
	 */
	@Override
	public UserDTO getProfile() {
		
		// Get current user
		
		User user = userRepository.findByUsername(requestMiner.getCurrentUsername());
		
		// Prepare and return object
		
		UserDTO userDto = mapper.map(user, UserDTO.class);
		return userDto;
	}

	/*
	 * (non-Javadoc)
	 * @see de.e2security.e2netwatch.business.manager.UserManagementManager#updateUser(de.e2security.e2netwatch.usermanagement.model.dto.UserUpdateDTO)
	 */
	@Override
	@Transactional
	public UserDTO updateUser(UserUpdateDTO userUpdateDTO) {
		
		// Check input data
				
		Preconditions.checkNotNull(userUpdateDTO.getFirstName(), CustomError.FIRSTNAME_NOT_RECEIVED);
		Preconditions.checkArgument(isValidName(userUpdateDTO.getFirstName()), CustomError.FIRSTNAME_INVALID);
		
		Preconditions.checkNotNull(userUpdateDTO.getLastName(), CustomError.LASTNAME_NOT_RECEIVED);
		Preconditions.checkArgument(isValidName(userUpdateDTO.getLastName()), CustomError.LASTNAME_INVALID);
		
		Preconditions.checkNotNull(userUpdateDTO.getEmail(), CustomError.EMAIL_NOT_RECEIVED);
		Preconditions.checkArgument(EmailValidator.getInstance().isValid(userUpdateDTO.getEmail()), CustomError.EMAIL_INVALID);
		
		// Get current user
		
		User user = userRepository.findByUsername(requestMiner.getCurrentUsername());
		
		// Check mail availability
		
		if (!userUpdateDTO.getEmail().equals(user.getEmail())) 
			checkEmailAvailability(userUpdateDTO.getEmail());
		
		// Update user data
		
		user.setFirstName(userUpdateDTO.getFirstName());
		user.setLastName(userUpdateDTO.getLastName());
		user.setEmail(userUpdateDTO.getEmail());
		
		// Prepare and return user object
		
		UserDTO userDto = mapper.map(user, UserDTO.class);
		return userDto;
	}

	/*
	 * (non-Javadoc)
	 * @see de.e2security.e2netwatch.business.manager.UserManagementManager#resetUserPassword(de.e2security.e2netwatch.usermanagement.model.dto.PasswordResetDTO)
	 */
	@Override
	@Transactional
	public void resetUserPassword(PasswordResetDTO password) {
		
		// Check input data
		
		Preconditions.checkNotNull(password.getPassword(), "Password not received");
		Preconditions.checkNotNull(password.getRepeatedPassword(), "Repeated password not received");
		
		Preconditions.checkArgument(password.getPassword().equals(password.getRepeatedPassword()), "Passwords did not match");
		Preconditions.checkArgument(password.getPassword().length() >= PASSWORD_MIN_LENGTH, 
				String.format("Password must be at least %d characters long", PASSWORD_MIN_LENGTH));
		
		// Update user password
		
		User user = userRepository.findByUsername(requestMiner.getCurrentUsername());
		user.setPassword(passwordEncoder.encode(password.getPassword()));
	}

	/*
	 * (non-Javadoc)
	 * @see de.e2security.e2netwatch.business.manager.UserManagementManager#addUser(de.e2security.e2netwatch.usermanagement.model.dto.UserInsertDTO)
	 */
	@Override
	@Transactional
	public UserDTO addUser(UserInsertDTO userInsertDto) {
		
		// Check data
				
		Preconditions.checkNotNull(userInsertDto.getUsername(), CustomError.USERNAME_NOT_RECEIVED);
		Preconditions.checkArgument(isValidUsername(userInsertDto.getUsername()), CustomError.USERNAME_INVALID);
		
		Preconditions.checkNotNull(userInsertDto.getFirstName(), CustomError.FIRSTNAME_NOT_RECEIVED);
		Preconditions.checkArgument(isValidName(userInsertDto.getFirstName()), CustomError.FIRSTNAME_INVALID);
		
		Preconditions.checkNotNull(userInsertDto.getLastName(), CustomError.LASTNAME_NOT_RECEIVED);
		Preconditions.checkArgument(isValidName(userInsertDto.getLastName()), CustomError.LASTNAME_INVALID);
		
		Preconditions.checkNotNull(userInsertDto.getEmail(), CustomError.EMAIL_NOT_RECEIVED);
		Preconditions.checkArgument(EmailValidator.getInstance().isValid(userInsertDto.getEmail()), CustomError.EMAIL_INVALID);
		
		// Check if email address and username are available

		checkUsernameAvailability(userInsertDto.getUsername());
		checkEmailAvailability(userInsertDto.getEmail());
		
		// Save new user
		
		String rawPassword = generatePassword();
		String encodedPassword = passwordEncoder.encode(rawPassword);
		
		User user = new User();
		
		user.setUsername(userInsertDto.getUsername());
		user.setPassword(encodedPassword);
		user.setFirstName(userInsertDto.getFirstName());
		user.setLastName(userInsertDto.getLastName());
		user.setEmail(userInsertDto.getEmail());
		user.setUserStatus(getUserStatus(userInsertDto.getUserStatusId()));
		user.setUserGroups(getUserGroups(userInsertDto.getUserGroupIds()));
		
		User savedUser = userRepository.save(user);
		
		// Prepare and return user object
		
		UserDTO userDto = mapper.map(savedUser, UserDTO.class);
		userDto.setGeneratedPassword(rawPassword);
		return userDto;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.e2security.e2netwatch.business.manager.UserManagementManager#deleteUser(int)
	 */
	@Override
	@Transactional
	public void deleteUser(int id) {
		
		// Get user with ID
		
		Optional<User> userOpt = userRepository.findById(id);
		
		// Check if exists
		
		if (!userOpt.isPresent())
			throw new EntityNotFoundException(CustomError.USER_NOT_FOUND.toString());
		
		userRepository.delete(userOpt.get());
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.e2security.e2netwatch.business.manager.UserManagementManager#deactivateUser(int)
	 */
	@Override
	@Transactional
	public UserDTO deactivateUser(int id) {
		
		// Get user for change
		
		Optional<User> optUser = userRepository.findById(id);
		
		// Check resource
		
		Assert.isTrue(optUser.isPresent(), CustomError.USER_NOT_FOUND.getErrorMessage());
		
		User user = optUser.get();
		
		Assert.isTrue(user.getUserStatus().getCode().equals("ACTIVE"), CustomError.USER_ALREADY_DEACTIVATED.getErrorMessage());
		
		// Change user and return
		
		UserStatus deactivatedStatus = userStatusRepository.findByCode("NOT_ACTIVE");
		user.setUserStatus(deactivatedStatus);
		
		UserDTO userDto = mapper.map(user, UserDTO.class);
		return userDto;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.e2security.e2netwatch.business.manager.UserManagementManager#activateUser(int)
	 */
	@Override
	@Transactional
	public UserDTO activateUser(int id) {
		
		// Get user for change
		
		Optional<User> optUser = userRepository.findById(id);
		
		// Check resource
		
		Assert.isTrue(optUser.isPresent(), CustomError.USER_NOT_FOUND.getErrorMessage());
		
		User user = optUser.get();
		
		Assert.isTrue(user.getUserStatus().getCode().equals("NOT_ACTIVE"), CustomError.USER_ALREADY_ACTIVATED.getErrorMessage());
		
		// Change user and return
		
		UserStatus activatedStatus = userStatusRepository.findByCode("ACTIVE");
		user.setUserStatus(activatedStatus);
		
		UserDTO userDto = mapper.map(user, UserDTO.class);
		return userDto;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.e2security.e2netwatch.business.manager.UserManagementManager#updateUserComplete(de.e2security.e2netwatch.usermanagement.model.dto.UserAdminUpdateDTO)
	 */
	@Override
	@Transactional
	public UserDTO updateUserComplete(UserAdminUpdateDTO userUpdateDto) {
		
		// Check data
		
		Preconditions.checkNotNull(userUpdateDto.getUsername(), CustomError.USERNAME_NOT_RECEIVED);
		Preconditions.checkArgument(isValidUsername(userUpdateDto.getUsername()), CustomError.USERNAME_INVALID);
		
		Preconditions.checkNotNull(userUpdateDto.getFirstName(), CustomError.FIRSTNAME_NOT_RECEIVED);
		Preconditions.checkArgument(isValidName(userUpdateDto.getFirstName()), CustomError.FIRSTNAME_INVALID);
		
		Preconditions.checkNotNull(userUpdateDto.getLastName(), CustomError.LASTNAME_NOT_RECEIVED);
		Preconditions.checkArgument(isValidName(userUpdateDto.getLastName()), CustomError.LASTNAME_INVALID);
		
		Preconditions.checkNotNull(userUpdateDto.getEmail(), CustomError.EMAIL_NOT_RECEIVED);
		Preconditions.checkArgument(EmailValidator.getInstance().isValid(userUpdateDto.getEmail()), CustomError.EMAIL_INVALID);
		
		// Get user for change
		
		User user = getUser(userUpdateDto.getId());
		
		// Check availability of email address and username
		
		if (!userUpdateDto.getUsername().equals(user.getUsername()))
			checkUsernameAvailability(userUpdateDto.getUsername());
			
		if (!userUpdateDto.getEmail().equals(user.getEmail()))
			checkEmailAvailability(userUpdateDto.getEmail());
		
		// Save new user
		
		user.setUsername(userUpdateDto.getUsername());
		user.setFirstName(userUpdateDto.getFirstName());
		user.setLastName(userUpdateDto.getLastName());
		user.setEmail(userUpdateDto.getEmail());
		user.setUserStatus(getUserStatus(userUpdateDto.getUserStatusId()));
		user.setUserGroups(getUserGroups(userUpdateDto.getUserGroupIds()));
		
		// Prepare and return user object
		
		UserDTO userDto = mapper.map(user, UserDTO.class);
		return userDto;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.e2security.e2netwatch.business.manager.UserManagementManager#checkEmailAvailability(java.lang.String)
	 */
	@Override
	public EmailCheckDTO checkEmailValidity(String email) {
		
		// Do validation and check availability and return object with info
		
		Boolean emailAddressValid = EmailValidator.getInstance().isValid(email);
		Boolean emailAddressAvailable = (emailAddressValid ? isEmailUnconstrained(email) : null);
		
		return new EmailCheckDTO(email, emailAddressValid, emailAddressAvailable);
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.e2security.e2netwatch.business.manager.UserManagementManager#checkUsernameAvailability(java.lang.String)
	 */
	@Override
	public UsernameCheckDTO checkUsernameValidity(String username) {
		
		// Do validation and check availability and return object with info
		
		Boolean usernameValid = isValidUsername(username);
		Boolean usernameAvailable = (usernameValid ? isUsernameUnconstrained(username) : null);
		
		return new UsernameCheckDTO(username, usernameValid, usernameAvailable);
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.e2security.e2netwatch.business.manager.UserManagementManager#getUsers()
	 */
	@Override
	public ListDTO<UserDTO> getUsers() {
		
		// Get all user entities
		
		List<User> allUsers = userRepository.findAll();
		
		// Map to DTO list
		
		List<UserDTO> allUsersDto = allUsers
				.stream()
				.map(from -> mapper.map(from, UserDTO.class))
				.collect(Collectors.toList());
		return new ListDTO<>(allUsersDto, null);
	}
	
	/*
	 * Private service methods
	 */
	
	/**
	 * Method to generate random password string
	 * 
	 * @return generated random password string
	 */
	private String generatePassword() {
		return RandomStringUtils.random(PASSWORD_MIN_LENGTH, true, true);
	}
	
	/**
	 * Method to check whether username is not recorded already
	 * 
	 * @param username to be checked
	 * 
	 * @return true if username is not taken already
	 */
	private boolean isUsernameUnconstrained(String username) {
		try {
			checkUsernameAvailability(username);
		} catch(EntityExistsException e) {
			return false;
		}
		return true;
	}
	
	/**
	 * Method to check whether email is not recorded already
	 * 
	 * @param email to be checked
	 * 
	 * @return true if email is not taken already
	 */
	private boolean isEmailUnconstrained(String email) {
		try {
			checkEmailAvailability(email);
		} catch(EntityExistsException e) {
			return false;
		}
		return true;
	}
	
	/**
	 * Method to check whether name is valid
	 * 
	 * @param name to be checked
	 * 
	 * @return true if name is valid, otherwise false
	 */
	private boolean isValidName(String name) {
		if (!name.equals("")) {
			return true;
		}
		return false;
	}
	
	/**
	 * Method to check whether username is valid
	 * 
	 * @param username to be checked
	 * 
	 * @return true if username is valid, otherwise false
	 */
	private boolean isValidUsername(String username) {
		return username.matches(String.format("[a-zA-Z0-9]{%d,}", USERNAME_MIN_LENGTH));
	}
	
	/**
	 * Method to validate email address availability for User entity
	 * 
	 * @param email address to be checked
	 */
	private void checkEmailAvailability(String email) {
		User user = userRepository.findByEmail(email);
		if (user != null)
			throw new EntityExistsException(CustomError.EMAIL_TAKEN.toString());
	}
	
	/**
	 * Method to validate username availability for User entity
	 * 
	 * @param username to be checked
	 */
	private void checkUsernameAvailability(String username) {
		User user = userRepository.findByUsername(username);
		if (user != null)
			throw new EntityExistsException(CustomError.USERNAME_TAKEN.toString());
	}
	
	/**
	 * Method to get user status based on ID, with additional validation
	 * 
	 * @userStatusId id of the user status
	 * @return user status object
	 * @throws EntityNotFoundException if no user status found for provided ID
	 */
	private UserStatus getUserStatus(Integer userStatusId) throws EntityNotFoundException {
		if(userStatusId == null) {
			return userStatusRepository.findByCode("NOT_ACTIVE");
		} else {
			Optional<UserStatus> us = userStatusRepository.findById(userStatusId);
			if(us.isPresent()) {
				return us.get();
			} else {
				throw new EntityNotFoundException(CustomError.USERSTATUS_NOT_FOUND.toString());
			}
		}
	}
	
	/**
	 * Method to get list of user groups based on IDs, with additional validation
	 * 
	 * @param userGroupIds list of user group IDs
	 * @return Hash set of user group objects
	 * @throws EntityNotFoundException if no user group found for one of the IDs
	 */
	private HashSet<UserGroup> getUserGroups(List<Integer> userGroupIds) throws EntityNotFoundException {
		
		Preconditions.checkNotNull(userGroupIds, CustomError.USERGROUP_NOT_RECEIVED);
		Preconditions.checkArgument(userGroupIds.size() > 0, CustomError.USERGROUP_NOT_RECEIVED);
		
		List<UserGroup> userGroups = new ArrayList<>();
		for (Integer userGroupId : userGroupIds) {
			if (userGroupId != null) {
				Optional<UserGroup> ug = userGroupRepository.findById(userGroupId);
				if(ug.isPresent()) {
					userGroups.add(ug.get());
				} else {
					throw new EntityNotFoundException(CustomError.USERGROUP_NOT_FOUND.toString());
				}
			} else {
				throw new EntityNotFoundException(CustomError.USERGROUP_NOT_FOUND.toString());
			}
		}
		return new HashSet<>(userGroups);
	}
	
	/**
	 * Method for getting User from DB, with additional checks
	 * 
	 * @param userId ID of the user
	 * @return user entity
	 * @throws EntityNotFoundException if no user found by ID
	 */
	private User getUser(Integer userId) throws EntityNotFoundException {
		
		Preconditions.checkNotNull(userId, CustomError.USER_NOT_FOUND);
		
		Optional<User> optionalUser = userRepository.findById(userId);
		
		// Check if user exists
		
		if(optionalUser.isPresent()) {
			return optionalUser.get();
		} else {
			throw new EntityNotFoundException(CustomError.USER_NOT_FOUND.toString());
		}
	}

}
