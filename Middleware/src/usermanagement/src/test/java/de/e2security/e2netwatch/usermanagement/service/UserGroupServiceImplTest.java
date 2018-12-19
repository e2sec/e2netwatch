package de.e2security.e2netwatch.usermanagement.service;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import de.e2security.e2netwatch.usermanagement.dao.UserGroupRepository;
import de.e2security.e2netwatch.usermanagement.dto.UserGroupDTO;
import de.e2security.e2netwatch.usermanagement.model.UserGroup;

/**
 * Tests the UserGroupService implementation
 *
 * @author Hrvoje
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(UserGroupServiceImpl.class)
public class UserGroupServiceImplTest {
	
	private UserGroupService userGroupServiceImpl;
	
	@Mock private UserGroupRepository userGroupRepository;
		
	@Before
	public void setup() throws Exception {
		MockitoAnnotations.initMocks(this);
		userGroupServiceImpl = PowerMockito.spy(new UserGroupServiceImpl(userGroupRepository));
	}
	
	/*
	 * getUserGroups
	 */
	
	@Test
	public void getUserGroups_ok() {
		
		// 
		
		UserGroup ug1 = new UserGroup();
		ug1.setId(1);
		ug1.setName("Admins");
		ug1.setDescription("User group admins");
		
		UserGroup ug2 = new UserGroup();
		ug2.setId(2);
		ug2.setName("Users");
		ug2.setDescription("User group users");
		
		List<UserGroup> userGroupList = new ArrayList<>();
		userGroupList.add(ug1);
		userGroupList.add(ug2);
		
		Mockito.when(userGroupRepository.findAll()).thenReturn(userGroupList);
		
		List<UserGroupDTO> userGroupsOut = userGroupServiceImpl.getUserGroups();
		
		assertEquals("Number of user groups not as expected", 2, userGroupsOut.size());
		assertEquals("Second user group name not as expected", "Users", userGroupsOut.get(1).getName());
	}
	
	@Test
	public void getUserGroups_empty() {
		
		// when no user groups in DB
		
		List<UserGroup> userGroupList = new ArrayList<>();
		
		Mockito.when(userGroupRepository.findAll()).thenReturn(userGroupList);
		
		List<UserGroupDTO> userGroupsOut = userGroupServiceImpl.getUserGroups();
		
		assertEquals("Number of user groups not as expected", 0, userGroupsOut.size());
	}
	
}
