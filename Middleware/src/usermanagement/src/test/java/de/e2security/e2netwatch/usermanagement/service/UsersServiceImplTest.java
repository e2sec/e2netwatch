package de.e2security.e2netwatch.usermanagement.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

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

/**
 * Tests the UserManagementService.
 *
 * @author Hrvoje
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(UsersServiceImpl.class)
public class UsersServiceImplTest {
	
	private UsersService usersServiceImpl;
	
	@Mock private UserRepository userRepository;
	@Mock private UserStatusRepository userStatusRepository;
	@Mock private UserGroupRepository userGroupRepository;
	@Mock private RequestMiner requestMiner;
	@Mock private PasswordEncoder passwordEncoder;
	
	private User current_user = Mockito.mock(User.class);
	
	@Before
	public void setup() throws Exception {
		MockitoAnnotations.initMocks(this);
		usersServiceImpl = PowerMockito.spy(new UsersServiceImpl(
				requestMiner, passwordEncoder, userRepository, userStatusRepository, userGroupRepository));
	}
	
	/*
	 * findByUsername
	 */
	
	@Test
	public void findByUsername() {
		
		String username = "username";
		User user = mock(User.class);
		
		when(userRepository.findByUsername(username)).thenReturn(user);
		
		User userActual = usersServiceImpl.findByUsername(username);
		
		assertEquals("Expected users are not the same", user, userActual);
	}
	
	/*
	 * resetUserPassword
	 */
	
	@Test(expected = NullPointerException.class)
	public void resetUserPassword_fail_1() {
		
		User user = Mockito.mock(User.class);
		Mockito.when(requestMiner.getCurrentUsername()).thenReturn("user");
		Mockito.when(passwordEncoder.encode("password")).thenReturn("encoded_password");
		Mockito.when(userRepository.findByUsername("user")).thenReturn(user);
		
		// when password is null
		
		PasswordResetDTO pass2 = new PasswordResetDTO(null, "repeated password");
		usersServiceImpl.resetUserPassword(pass2);
	}
	
	@Test(expected = NullPointerException.class)
	public void resetUserPassword_fail_2() {
		
		User user = Mockito.mock(User.class);
		Mockito.when(requestMiner.getCurrentUsername()).thenReturn("user");
		Mockito.when(passwordEncoder.encode("password")).thenReturn("encoded_password");
		Mockito.when(userRepository.findByUsername("user")).thenReturn(user);
		
		// when repeated password is null
		PasswordResetDTO pass3 = new PasswordResetDTO("password", null);
		usersServiceImpl.resetUserPassword(pass3);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void resetUserPassword_fail_3() {
		
		User user = Mockito.mock(User.class);
		Mockito.when(requestMiner.getCurrentUsername()).thenReturn("user");
		Mockito.when(passwordEncoder.encode("password")).thenReturn("encoded_password");
		Mockito.when(userRepository.findByUsername("user")).thenReturn(user);
		
		// when passwords do not match
		PasswordResetDTO pass4 = new PasswordResetDTO("password", "password_not_match");
		usersServiceImpl.resetUserPassword(pass4);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void resetUserPassword_fail_4() {
		
		User user = Mockito.mock(User.class);
		Mockito.when(requestMiner.getCurrentUsername()).thenReturn("user");
		Mockito.when(passwordEncoder.encode("password")).thenReturn("encoded_password");
		Mockito.when(userRepository.findByUsername("user")).thenReturn(user);
		
		// when passwords is not valid
		PasswordResetDTO pass5 = new PasswordResetDTO("pass", "pass");
		usersServiceImpl.resetUserPassword(pass5);
	}
	
	@Test
	public void resetUserPassword_ok() {
		
		User user = Mockito.mock(User.class);
		Mockito.when(requestMiner.getCurrentUsername()).thenReturn("user");
		Mockito.when(passwordEncoder.encode("password")).thenReturn("encoded_password");
		Mockito.when(userRepository.findByUsername("user")).thenReturn(user);
		
		// when password ok
		PasswordResetDTO pass6 = new PasswordResetDTO("password", "password");
		usersServiceImpl.resetUserPassword(pass6);
	}
	
	/*
	 * addUser
	 */
	
	@Test(expected = NullPointerException.class)
	public void addUser_invaliddata_1() {
		
		// username is null
		
		UserInsertDTO userInsertDto = new UserInsertDTO();
		userInsertDto.setUsername(null);
		
		usersServiceImpl.addUser(userInsertDto);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void addUser_invaliddata_2() {
		
		// username invalid
		
		String username_invalid = "user.name";
		
		UserInsertDTO userInsertDto = new UserInsertDTO();
		userInsertDto.setUsername(username_invalid);
		
		usersServiceImpl.addUser(userInsertDto);
	}
	
	@Test(expected = NullPointerException.class)
	public void addUser_invaliddata_3() {
		
		// first name null
		
		UserInsertDTO userInsertDto = new UserInsertDTO();
		userInsertDto.setUsername("username");
		userInsertDto.setFirstName(null);
		
		usersServiceImpl.addUser(userInsertDto);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void addUser_invaliddata_4() {
		
		// first name invalid
		
		String invalidFirstName = "";
		
		UserInsertDTO userInsertDto = new UserInsertDTO();
		userInsertDto.setUsername("username");
		userInsertDto.setFirstName(invalidFirstName);
		
		usersServiceImpl.addUser(userInsertDto);
	}
	
	@Test(expected = NullPointerException.class)
	public void addUser_invaliddata_5() {
		
		// last name null
		
		UserInsertDTO userInsertDto = new UserInsertDTO();
		userInsertDto.setUsername("username");
		userInsertDto.setFirstName("John");
		userInsertDto.setLastName(null);

		usersServiceImpl.addUser(userInsertDto);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void addUser_invaliddata_6() {
		
		// last name invalid
		
		String invalidLastName = "";
		
		UserInsertDTO userInsertDto = new UserInsertDTO();
		userInsertDto.setUsername("username");
		userInsertDto.setFirstName("John");
		userInsertDto.setLastName(invalidLastName);

		usersServiceImpl.addUser(userInsertDto);
	}
	
	@Test(expected = NullPointerException.class)
	public void addUser_invaliddata_7() {
		
		// email null
		
		UserInsertDTO userInsertDto = new UserInsertDTO();
		userInsertDto.setUsername("username");
		userInsertDto.setFirstName("John");
		userInsertDto.setLastName("Doe");
		userInsertDto.setEmail(null);

		usersServiceImpl.addUser(userInsertDto);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void addUser_invaliddata_8() {
		
		// email invalid
		
		String invalidEmail = "invalid_email";
		
		UserInsertDTO userInsertDto= new UserInsertDTO();
		userInsertDto.setUsername("username");
		userInsertDto.setFirstName("John");
		userInsertDto.setLastName("Doe");
		userInsertDto.setEmail(invalidEmail);
		
		usersServiceImpl.addUser(userInsertDto);
	}
	
	@Test(expected = EntityExistsException.class)
	public void addUser_invaliddata_9() throws Exception {
		
		// username not available
						
		String username = "username";
		
		UserInsertDTO userInsertDto= new UserInsertDTO();
		userInsertDto.setUsername(username);
		userInsertDto.setFirstName("John");
		userInsertDto.setLastName("Doe");
		userInsertDto.setEmail("john.doe@mail.com");
		
		PowerMockito.doThrow(new EntityExistsException())
			.when(usersServiceImpl, "checkUsernameAvailability", username);
		
		usersServiceImpl.addUser(userInsertDto);
	}
	
	@Test(expected = EntityExistsException.class)
	public void addUser_invaliddata_10() throws Exception {
		
		// email not available
				
		String email = "john.doe@mail.com";
		
		UserInsertDTO userInsertDto= new UserInsertDTO();
		userInsertDto.setUsername("username");
		userInsertDto.setFirstName("John");
		userInsertDto.setLastName("Doe");
		userInsertDto.setEmail(email);
		
		PowerMockito.doNothing()
			.when(usersServiceImpl, "checkUsernameAvailability", anyString());
		PowerMockito.doThrow(new EntityExistsException())
			.when(usersServiceImpl, "checkEmailAvailability", email);
		
		usersServiceImpl.addUser(userInsertDto);
	}
	
	@Test(expected = EntityNotFoundException.class)
	public void addUser_invaliddata_11() throws Exception {
		
		// user status not found
				
		Integer userStatusId = 12;
		
		UserInsertDTO userInsertDto= new UserInsertDTO();
		userInsertDto.setUsername("username");
		userInsertDto.setFirstName("John");
		userInsertDto.setLastName("Doe");
		userInsertDto.setEmail("john.doe@mail.com");
		userInsertDto.setUserStatusId(userStatusId);
		
		PowerMockito.doNothing()
			.when(usersServiceImpl, "checkUsernameAvailability", anyString());
		PowerMockito.doNothing()
			.when(usersServiceImpl, "checkEmailAvailability", anyString());
		PowerMockito.doThrow(new EntityNotFoundException())
			.when(usersServiceImpl, "getUserStatus", userStatusId);
		
		usersServiceImpl.addUser(userInsertDto);
	}
	
	@Test(expected = EntityNotFoundException.class)
	public void addUser_invaliddata_12() throws Exception {
		
		// user group not found
		
		List<Integer> userGroupIds = new ArrayList<>();
		userGroupIds.add(1);
		userGroupIds.add(2);
				
		UserStatus us = new UserStatus();
		us.setId(12);
		us.setCode("ACTIVE");
		us.setName("Active");
		
		UserInsertDTO userInsertDto= new UserInsertDTO();
		userInsertDto.setUsername("username");
		userInsertDto.setFirstName("John");
		userInsertDto.setLastName("Doe");
		userInsertDto.setEmail("john.doe@mail.com");
		userInsertDto.setUserStatusId(12);
		userInsertDto.setUserGroupIds(userGroupIds);
		
		PowerMockito.doNothing()
			.when(usersServiceImpl, "checkUsernameAvailability", anyString());
		PowerMockito.doNothing()
			.when(usersServiceImpl, "checkEmailAvailability", anyString());
		
		PowerMockito.doReturn(us)
			.when(usersServiceImpl, "getUserStatus", anyInt());
		PowerMockito.doThrow(new EntityNotFoundException())
			.when(usersServiceImpl, "getUserGroups", userGroupIds);
		
		usersServiceImpl.addUser(userInsertDto);
	}
	
	@Test
	public void addUser_ok() throws Exception {
		
		List<Integer> userGroupIds = new ArrayList<>();
		userGroupIds.add(1);
		userGroupIds.add(2);
		
		List<UserGroup> userGroups = new ArrayList<>();
		userGroups.add(mock(UserGroup.class));
		userGroups.add(mock(UserGroup.class));
		
		UserInsertDTO userInsertDto = new UserInsertDTO();
		userInsertDto.setUsername("username");
		userInsertDto.setFirstName("John");
		userInsertDto.setLastName("Doe");
		userInsertDto.setEmail("john.doe@mail.com");
		userInsertDto.setUserStatusId(12);
		userInsertDto.setUserGroupIds(userGroupIds);
		
		PowerMockito.doNothing()
			.when(usersServiceImpl, "checkUsernameAvailability", anyString());
		PowerMockito.doNothing()
			.when(usersServiceImpl, "checkEmailAvailability", anyString());
		
		PowerMockito.doReturn(mock(UserStatus.class))
			.when(usersServiceImpl, "getUserStatus", anyInt());
		PowerMockito.doReturn(new HashSet<>(userGroups))
			.when(usersServiceImpl, "getUserGroups", userGroupIds);
		
		Mockito.when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(mock(User.class));
		
		usersServiceImpl.addUser(userInsertDto);
	}
	
	/*
	 * updateUser
	 */
	
	@Test(expected = NullPointerException.class)
	public void updateUser_invaliddata_1() {
		
		// first name null
		
		UserUpdateDTO userUpdateDto = new UserUpdateDTO();
		userUpdateDto.setFirstName(null);
		
		usersServiceImpl.updateUser(userUpdateDto);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void updateUser_invaliddata_2() {
		
		// first name invalid
		
		UserUpdateDTO userUpdateDto = new UserUpdateDTO();
		userUpdateDto.setFirstName("");
		
		usersServiceImpl.updateUser(userUpdateDto);
	}
	
	@Test(expected = NullPointerException.class)
	public void updateUser_invaliddata_3() {
		
		// last name null
		
		UserUpdateDTO userUpdateDto = new UserUpdateDTO();
		userUpdateDto.setFirstName("John");
		userUpdateDto.setLastName(null);

		usersServiceImpl.updateUser(userUpdateDto);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void updateUser_invaliddata_4() {
		
		// last name invalid
		
		UserUpdateDTO userUpdateDto = new UserUpdateDTO();
		userUpdateDto.setFirstName("John");
		userUpdateDto.setLastName("");

		usersServiceImpl.updateUser(userUpdateDto);
	}
	
	@Test(expected = NullPointerException.class)
	public void updateUser_invaliddata_5() {
		
		// email null
		
		UserUpdateDTO userUpdateDto = new UserUpdateDTO();
		userUpdateDto.setFirstName("John");
		userUpdateDto.setLastName("Doe");
		userUpdateDto.setEmail(null);

		usersServiceImpl.updateUser(userUpdateDto);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void updateUser_invaliddata_6() {
		
		// email invalid
		
		String sameEmailButInvalid = "same.email.mail.com";
		
		UserUpdateDTO userUpdateDto = new UserUpdateDTO();
		userUpdateDto.setFirstName("John");
		userUpdateDto.setLastName("Doe");
		userUpdateDto.setEmail(sameEmailButInvalid);
		
		usersServiceImpl.updateUser(userUpdateDto);
	}
	
	@Test(expected = EntityExistsException.class)
	public void updateUser_invaliddata_7() throws Exception {
		
		// email unavailable
		
		String currentUserMail = "current.user@mail.com";
		String newMail = "new.email@mail.com";
		
		Mockito.when(requestMiner.getCurrentUsername()).thenReturn("current_username");
		Mockito.when(userRepository.findByUsername("current_username")).thenReturn(current_user);
		Mockito.when(current_user.getEmail()).thenReturn(currentUserMail);
		
		Mockito.when(userRepository.findByEmail(newMail)).thenReturn(Mockito.mock(User.class));
		
		UserUpdateDTO userUpdateDto = new UserUpdateDTO();
		userUpdateDto.setFirstName("John");
		userUpdateDto.setLastName("Doe");
		userUpdateDto.setEmail(newMail);
		
		PowerMockito.doThrow(new EntityExistsException())
			.when(usersServiceImpl, "checkEmailAvailability", anyString());
		
		usersServiceImpl.updateUser(userUpdateDto);
	}
	
	@Test
	public void updateUser_ok_1() throws Exception {
		
		// email unavailable
		
		String currentUserMail = "current.user@mail.com";
		String newMail = "new.email@mail.com";
		
		Mockito.when(requestMiner.getCurrentUsername()).thenReturn("current_username");
		Mockito.when(userRepository.findByUsername("current_username")).thenReturn(current_user);
		Mockito.when(current_user.getEmail()).thenReturn(currentUserMail);
		
		Mockito.when(userRepository.findByEmail(newMail)).thenReturn(Mockito.mock(User.class));
		
		UserUpdateDTO userUpdateDto = new UserUpdateDTO();
		userUpdateDto.setFirstName("John");
		userUpdateDto.setLastName("Doe");
		userUpdateDto.setEmail(newMail);
		
		PowerMockito.doNothing()
			.when(usersServiceImpl, "checkEmailAvailability", anyString());
		
		usersServiceImpl.updateUser(userUpdateDto);
	}
	
	@Test
	public void updateUser_ok_2() throws Exception {
		
		// email unavailable
		
		String currentUserMail = "current.user@mail.com";
		
		Mockito.when(requestMiner.getCurrentUsername()).thenReturn("current_username");
		Mockito.when(userRepository.findByUsername("current_username")).thenReturn(current_user);
		Mockito.when(current_user.getEmail()).thenReturn(currentUserMail);
		
		Mockito.when(userRepository.findByEmail(currentUserMail)).thenReturn(Mockito.mock(User.class));
		
		UserUpdateDTO userUpdateDto = new UserUpdateDTO();
		userUpdateDto.setFirstName("John");
		userUpdateDto.setLastName("Doe");
		userUpdateDto.setEmail(currentUserMail);
		
		usersServiceImpl.updateUser(userUpdateDto);
	}
	
	/*
	 * updateUserComplete
	 */
	
	
	@Test(expected = NullPointerException.class)
	public void updateUserComplete_invalid_1() throws Exception {
		
		// Getting user results with null ID
		
		UserAdminUpdateDTO userIn = new UserAdminUpdateDTO();
		userIn.setId(null);
		userIn.setFirstName("John");
		userIn.setLastName("Doe");
		userIn.setEmail("john.doe@mail.com");
		userIn.setUsername("username");
		
		PowerMockito.doThrow(new NullPointerException())
			.when(usersServiceImpl, "getUser", userIn.getId());
		
		usersServiceImpl.updateUserComplete(userIn);
	}
	
	@Test(expected = EntityNotFoundException.class)
	public void updateUserComplete_invalid_2() throws Exception {
		
		// Getting user results for non existing user id
		
		UserAdminUpdateDTO userIn = new UserAdminUpdateDTO();
		userIn.setId(0);
		userIn.setFirstName("John");
		userIn.setLastName("Doe");
		userIn.setEmail("john.doe@mail.com");
		userIn.setUsername("username");
		
		PowerMockito.doThrow(new EntityNotFoundException())
			.when(usersServiceImpl, "getUser", userIn.getId());
		
		usersServiceImpl.updateUserComplete(userIn);
		
	}
	
	@Test(expected = EntityExistsException.class)
	public void updateUserComplete_invalid_3() throws Exception {
		
		// Username not available
		
		UserAdminUpdateDTO userIn = new UserAdminUpdateDTO();
		userIn.setId(1);
		userIn.setFirstName("John");
		userIn.setLastName("Doe");
		userIn.setEmail("john.doe@mail.com");
		userIn.setUsername("username");
		
		User user = mock(User.class);
		
		PowerMockito.doReturn(user)
			.when(usersServiceImpl, "getUser", userIn.getId());
		
		Mockito.when(user.getUsername()).thenReturn("oldusername");
		PowerMockito.doThrow(new EntityExistsException())
			.when(usersServiceImpl, "checkUsernameAvailability", userIn.getUsername());
		
		usersServiceImpl.updateUserComplete(userIn);
	}
	
	@Test(expected = EntityExistsException.class)
	public void updateUserComplete_invalid_4() throws Exception {
		
		// Email not available
		
		UserAdminUpdateDTO userIn = new UserAdminUpdateDTO();
		userIn.setId(1);
		userIn.setFirstName("John");
		userIn.setLastName("Doe");
		userIn.setEmail("john.doe@mail.com");
		userIn.setUsername("username");
		
		User user = mock(User.class);
		
		PowerMockito.doReturn(user)
			.when(usersServiceImpl, "getUser", userIn.getId());
		
		Mockito.when(user.getUsername()).thenReturn("oldusername");
		PowerMockito.doNothing()
			.when(usersServiceImpl, "checkUsernameAvailability", userIn.getUsername());
		
		Mockito.when(user.getEmail()).thenReturn("old.mail@mail.com");
		PowerMockito.doThrow(new EntityExistsException())
			.when(usersServiceImpl, "checkEmailAvailability", userIn.getEmail());
		
		usersServiceImpl.updateUserComplete(userIn);
	}
	
	@Test
	public void updateUserComplete_ok() throws Exception {
		
		//
		
		List<Integer> userGroupIds = new ArrayList<>();
		userGroupIds.add(1);
		userGroupIds.add(2);
		
		List<UserGroup> userGroups = new ArrayList<>();
		userGroups.add(mock(UserGroup.class));
		userGroups.add(mock(UserGroup.class));
		
		UserAdminUpdateDTO userIn = new UserAdminUpdateDTO();
		userIn.setId(1);
		userIn.setFirstName("John");
		userIn.setLastName("Doe");
		userIn.setEmail("john.doe@mail.com");
		userIn.setUsername("username");
		userIn.setUserStatusId(12);
		userIn.setUserGroupIds(userGroupIds);
		
		User user = mock(User.class);
		
		PowerMockito.doReturn(user)
			.when(usersServiceImpl, "getUser", userIn.getId());
		
		Mockito.when(user.getUsername()).thenReturn("oldusername");
		PowerMockito.doNothing()
			.when(usersServiceImpl, "checkUsernameAvailability", userIn.getUsername());
		
		Mockito.when(user.getEmail()).thenReturn("old.mail@mail.com");
		PowerMockito.doNothing()
			.when(usersServiceImpl, "checkEmailAvailability", userIn.getUsername());
		
		PowerMockito.doReturn(mock(UserStatus.class))
			.when(usersServiceImpl, "getUserStatus", userIn.getUserStatusId());
		PowerMockito.doReturn(new HashSet<>(userGroups))
			.when(usersServiceImpl, "getUserGroups", userIn.getUserGroupIds());
		
		usersServiceImpl.updateUserComplete(userIn);
	}

	/*
	 * getProfile
	 */
	
	@Test
	public void getProfile_ok() {
		
		UserGroup userGroup = new UserGroup();
		userGroup.setId(1);
		userGroup.setName("Admin");
		
		List<UserGroup> userGroups = new ArrayList<>();
		userGroups.add(userGroup);
		
		UserStatus userStatus = new UserStatus();
		userStatus.setId(1);
		userStatus.setCode("ACTIVE");
		userStatus.setName("Active");
		
		User user = new User();
		user.setId(1);
		user.setUsername("username");
		user.setPassword("somepassword");
		user.setFirstName("John");
		user.setLastName("Doe");
		user.setEmail("john.doe@mail.com");
		user.setUserStatus(userStatus);
		user.setUserGroups(new HashSet<>(userGroups));
		
		Mockito.when(requestMiner.getCurrentUsername()).thenReturn("current_username");
		Mockito.when(userRepository.findByUsername("current_username")).thenReturn(user);
		
		UserDTO returnDTO = usersServiceImpl.getProfile();
		
		assertEquals("ID of the user is not as expected", user.getId(), returnDTO.getId());
		assertEquals("Username of the user is not as expected", user.getUsername(), returnDTO.getUsername());
		assertNull("Password should be hidden - null", returnDTO.getGeneratedPassword());
		assertEquals("First name of the user is not as expected", user.getFirstName(), returnDTO.getFirstName());
		assertEquals("Last name of the user is not as expected", user.getLastName(), returnDTO.getLastName());
		assertEquals("Email of the user is not as expected", user.getEmail(), returnDTO.getEmail());
		assertEquals("User group name of the user is not as expected", 
				user.getUserGroups().iterator().next().getName(), returnDTO.getUserGroups().get(0).getName());
		assertEquals("User status of the user is not as expected", 
				user.getUserStatus().getName(), returnDTO.getUserStatus().getName());
	}
	
	/*
	 * deleteUser
	 */
	
	@Test(expected = EntityNotFoundException.class)
	public void deleteUser_invalid_1() {
		
		// User does not exist
		
		Mockito.when(userRepository.findById(0)).thenReturn(Optional.empty());
		
		usersServiceImpl.deleteUser(0);
	}
	
	@Test
	public void deleteUser_ok() {
		
		// 
		
		Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(new User()));
		
		usersServiceImpl.deleteUser(1);
	}
	
	/*
	 * deactivateUser
	 */
	
	@Test(expected = IllegalArgumentException.class)
	public void deactivateUser_invalid_1() {
		
		// User does not exist
		
		Mockito.when(userRepository.findById(0)).thenReturn(Optional.empty());
		
		usersServiceImpl.deactivateUser(0);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void deactivateUser_invalid_2() {
		
		// User is already deactivated
		
		UserStatus us = new UserStatus();
		us.setCode("NOT_ACTIVE");
		
		User user = new User();
		user.setUserStatus(us);
		
		Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(user));
		
		usersServiceImpl.deactivateUser(1);
	}
	
	@Test
	public void deactivateUser_ok() {
		
		// 
		
		String notActiveName = "Not active";
		
		UserStatus usNotActive = new UserStatus();
		usNotActive.setCode("NOT_ACTIVE");
		usNotActive.setName(notActiveName);
		
		UserStatus us = new UserStatus();
		us.setCode("ACTIVE");
		
		User user = new User();
		user.setUserStatus(us);
		
		Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(user));
		Mockito.when(userStatusRepository.findByCode("NOT_ACTIVE")).thenReturn(usNotActive);
		
		UserDTO userOut = usersServiceImpl.deactivateUser(1);
		
		assertEquals("Status code is not as expected", notActiveName, userOut.getUserStatus().getName());
	}
	
	/*
	 * activateUser
	 */
	
	@Test(expected = IllegalArgumentException.class)
	public void activateUser_invalid_1() {
		
		// User does not exist
		
		Mockito.when(userRepository.findById(0)).thenReturn(Optional.empty());
		
		usersServiceImpl.activateUser(0);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void activateUser_invalid_2() {
		
		// User is already activated
		
		UserStatus us = new UserStatus();
		us.setCode("ACTIVE");
		
		User user = new User();
		user.setUserStatus(us);
		
		Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(user));
		
		usersServiceImpl.activateUser(1);
	}
	
	@Test
	public void activateUser_ok() {
		
		// 
		
		String activeName = "Active";
		
		UserStatus usActive = new UserStatus();
		usActive.setCode("ACTIVE");
		usActive.setName(activeName);
		
		UserStatus us = new UserStatus();
		us.setCode("NOT_ACTIVE");
		
		User user = new User();
		user.setUserStatus(us);
		
		Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(user));
		Mockito.when(userStatusRepository.findByCode("ACTIVE")).thenReturn(usActive);
		
		UserDTO userOut = usersServiceImpl.activateUser(1);
		
		assertEquals("Status code is not as expected", activeName, userOut.getUserStatus().getName());
	}
	
	/*
	 * getUsers
	 */
	
	@Test
	public void getUsers_ok() {
		
		// 
		
		UserGroup ug = new UserGroup();
		ug.setId(1);
		ug.setName("Admin");
		
		List<UserGroup> ugList = new ArrayList<>();
		ugList.add(ug);
		
		User u1 = new User();
		u1.setId(1);
		u1.setUserGroups(new HashSet<>(ugList));
		User u2 = new User();
		u2.setId(2);
		u2.setUserGroups(new HashSet<>(ugList));
		
		List<User> userList = new ArrayList<>();
		userList.add(u1);
		userList.add(u2);
		
		Mockito.when(userRepository.findAll()).thenReturn(userList);
		
		ListDTO<UserDTO> usersOut = usersServiceImpl.getUsers();
		
		assertEquals("Number of users not as expected", 2, usersOut.getContent().size());
		assertEquals("User's user group not as expected", "Admin", usersOut.getContent().get(0).getUserGroups().get(0).getName());
	}
	
	@Test
	public void getUsers_empty() {
		
		// No users in DB
		
		List<User> userList = new ArrayList<>();
		
		Mockito.when(userRepository.findAll()).thenReturn(userList);
		
		ListDTO<UserDTO> usersOut = usersServiceImpl.getUsers();
		
		assertEquals("Number of users not as expected", 0, usersOut.getContent().size());
		assertEquals("Number of total users not as expected", 0, usersOut.getTotal());
		assertEquals("Number of retrieved users not as expected", 0, usersOut.getRetrieved());
	}

	/*
	 * checkEmailValidity
	 */
	
	@Test
	public void checkEmailValidity_1() throws Exception {
		
		// invalid mail
		
		String mail = "invalid.mail.address";
		
		EmailCheckDTO emailCheckOut = usersServiceImpl.checkEmailValidity(mail);
		
		assertEquals("Email address not as expected", mail, emailCheckOut.getEmail());
		assertFalse("Validity of email address not as expected", emailCheckOut.isValid());
		assertNull("Availability of email address should be null", emailCheckOut.isAvailable());		
	}
	
	@Test
	public void checkEmailValidity_2() throws Exception {
		
		// valid mail, not available
		
		String mail = "valid.mail@mail.com";
		
		PowerMockito.doReturn(false)
			.when(usersServiceImpl, "isEmailUnconstrained", mail);
		
		EmailCheckDTO emailCheckOut = usersServiceImpl.checkEmailValidity(mail);
		
		assertTrue("Validity of email address not as expected", emailCheckOut.isValid());
		assertFalse("Availability of email address not as expected", emailCheckOut.isAvailable());
	}
	
	@Test
	public void checkEmailValidity_3() throws Exception {
		
		// valid mail, is available
		
		String mail = "valid.available.mail@mail.com";
		
		PowerMockito.doReturn(true)
			.when(usersServiceImpl, "isEmailUnconstrained", mail);
		
		EmailCheckDTO emailCheckOut = usersServiceImpl.checkEmailValidity(mail);
		
		assertTrue("Validity of email address not as expected", emailCheckOut.isValid());
		assertTrue("Availability of email address not as expected", emailCheckOut.isAvailable());
		
	}

	/*
	 * checkUsernameValidity
	 */
	
	@Test
	public void checkUsernameValidity_1() throws Exception {
		
		// invalid username
		
		String username = "";
		
		UsernameCheckDTO usernameCheckDTO = usersServiceImpl.checkUsernameValidity(username);
		
		assertEquals("Username not as expected", username, usernameCheckDTO.getUsername());
		assertFalse("Validity of username not as expected", usernameCheckDTO.isValid());
		assertNull("Availability of username should be null", usernameCheckDTO.isAvailable());		
	}
	
	@Test
	public void checkUsernameValidity_2() throws Exception {
		
		// valid username, not available
		
		String username = "unavailableusername";
		
		PowerMockito.doReturn(false)
			.when(usersServiceImpl, "isUsernameUnconstrained", username);
		
		UsernameCheckDTO usernameCheckDTO = usersServiceImpl.checkUsernameValidity(username);
		
		assertTrue("Validity of username not as expected", usernameCheckDTO.isValid());
		assertFalse("Availability of username not as expected", usernameCheckDTO.isAvailable());
	}
	
	@Test
	public void checkUsernameValidity_3() throws Exception {
		
		// valid username, is available
		
		String username = "username";
		
		PowerMockito.doReturn(true)
			.when(usersServiceImpl, "isUsernameUnconstrained", username);
		
		UsernameCheckDTO usernameCheckDTO = usersServiceImpl.checkUsernameValidity(username);
		
		assertTrue("Validity of username not as expected", usernameCheckDTO.isValid());
		assertTrue("Availability of username not as expected", usernameCheckDTO.isAvailable());
	}
	
}
