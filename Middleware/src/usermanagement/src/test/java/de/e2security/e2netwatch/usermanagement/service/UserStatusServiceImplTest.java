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

import de.e2security.e2netwatch.usermanagement.dao.UserStatusRepository;
import de.e2security.e2netwatch.usermanagement.dto.UserStatusDTO;
import de.e2security.e2netwatch.usermanagement.model.UserStatus;

/**
 * Tests the UserStatusService implementation
 *
 * @author Hrvoje
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(UsersServiceImpl.class)
public class UserStatusServiceImplTest {
	
	private UserStatusService userStatusServiceImpl;
	
	@Mock private UserStatusRepository userStatusRepository;
		
	@Before
	public void setup() throws Exception {
		MockitoAnnotations.initMocks(this);
		userStatusServiceImpl = PowerMockito.spy(new UserStatusServiceImpl(userStatusRepository));
	}
	
	/*
	 * getUsers
	 */
	
	@Test
	public void getUserStatuses_ok() {
		
		// 
		
		UserStatus us1 = new UserStatus();
		us1.setId(1);
		us1.setCode("ACTIVE");
		us1.setName("Active");
		us1.setDescription("Active user status");
		
		UserStatus us2 = new UserStatus();
		us2.setId(2);
		us2.setCode("NOT_ACTIVE");
		us2.setName("Not active");
		us2.setDescription("Not active user status");
		
		List<UserStatus> userStatusList = new ArrayList<>();
		userStatusList.add(us1);
		userStatusList.add(us2);
		
		Mockito.when(userStatusRepository.findAll()).thenReturn(userStatusList);
		
		List<UserStatusDTO> userStatusesOut = userStatusServiceImpl.getUserStatuses();
		
		assertEquals("Number of user statuses not as expected", 2, userStatusesOut.size());
		assertEquals("Second user status name not as expected", "Not active", userStatusesOut.get(1).getName());
	}
	
	@Test
	public void getUserStatuses_empty() {
		
		//
		
		List<UserStatus> userStatusList = new ArrayList<>();
		
		Mockito.when(userStatusRepository.findAll()).thenReturn(userStatusList);
		
		List<UserStatusDTO> userStatusesOut = userStatusServiceImpl.getUserStatuses();
		
		assertEquals("Number of user statuses not as expected", 0, userStatusesOut.size());
	}
	
}
