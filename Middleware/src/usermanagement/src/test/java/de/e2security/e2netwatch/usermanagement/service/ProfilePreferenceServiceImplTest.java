package de.e2security.e2netwatch.usermanagement.service;

import static org.junit.Assert.assertEquals;

import org.dozer.DozerBeanMapper;
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

import de.e2security.e2netwatch.usermanagement.dao.ProfilePreferenceRepository;
import de.e2security.e2netwatch.usermanagement.dao.UserRepository;
import de.e2security.e2netwatch.usermanagement.dto.ProfilePreferenceDTO;
import de.e2security.e2netwatch.usermanagement.dto.ProfilePreferenceUpdateDTO;
import de.e2security.e2netwatch.usermanagement.model.ProfilePreference;
import de.e2security.e2netwatch.usermanagement.model.User;

/**
 * Tests the ProfilePreferenceService implementation
 *
 * @author Hrvoje
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(ProfilePreferenceServiceImpl.class)
public class ProfilePreferenceServiceImplTest {
	
	private ProfilePreferenceService profilePreferenceServiceImpl;
	
	@Mock private UserRepository userRepository;
	@Mock private ProfilePreferenceRepository profilePreferenceRepository;
	@Mock private RequestMiner requestMiner;
		
	@Before
	public void setup() throws Exception {
		MockitoAnnotations.initMocks(this);
		profilePreferenceServiceImpl = PowerMockito.spy(new ProfilePreferenceServiceImpl(requestMiner, userRepository, profilePreferenceRepository));
	}
	
	/*
	 * getGlobal
	 */
	
	@Test
	public void getGlobal_ok() {
		
		// 
		
		ProfilePreference pp = new ProfilePreference();
		pp.setId(1);
		pp.setTimezone("Europe/Berlin");
		
		Mockito.when(profilePreferenceRepository.findGlobal()).thenReturn(pp);
		
		ProfilePreferenceDTO ppDto = profilePreferenceServiceImpl.getGlobal();
		
		assertEquals("Id of the profile preference not as expected", pp.getId(), ppDto.getId());
	}
	
	/*
	 * getForCurrent
	 */
	
	@Test
	public void getForCurrent_1() {
		
		// User has profile preference
		
		String username = "username";
		
		ProfilePreference pp = new ProfilePreference();
		pp.setId(1);
		pp.setTimezone("Europe/Berlin");
		
		User user = new User();
		user.setUsername(username);
		user.setProfilePreference(pp);
		
		Mockito.when(requestMiner.getCurrentUsername()).thenReturn(username);
		Mockito.when(userRepository.findByUsername(username)).thenReturn(user);
		
		ProfilePreferenceDTO ppDto = profilePreferenceServiceImpl.getForCurrent();
		
		assertEquals("Id of the profile preference not as expected", pp.getId(), ppDto.getId());
	}
	
	@Test
	public void getForCurrent_2() throws Exception {
		
		// User has no profile preference
		
		String username = "username";
		
		ProfilePreference globalpp = new ProfilePreference();
		globalpp.setId(2);
		globalpp.setTimezone("Global timezone");
		
		DozerBeanMapper mapper = new DozerBeanMapper();
		ProfilePreferenceDTO globalProfilePreferenceDTO = mapper.map(globalpp, ProfilePreferenceDTO.class);	
		
		User user = new User();
		user.setUsername(username);
		user.setProfilePreference(null);
		
		Mockito.when(requestMiner.getCurrentUsername()).thenReturn(username);
		Mockito.when(userRepository.findByUsername(username)).thenReturn(user);
		PowerMockito.doReturn(globalProfilePreferenceDTO).when(profilePreferenceServiceImpl, "getGlobal");
		
		ProfilePreferenceDTO ppDto = profilePreferenceServiceImpl.getForCurrent();
		
		assertEquals("Id of the profile preference not as expected", globalProfilePreferenceDTO.getId(), ppDto.getId());
	}
	
	/*
	 * updateProfilePreferences
	 */
	
	@Test
	public void updateProfilePreferences_1() {
		
		// User has profile preference
		
		ProfilePreferenceUpdateDTO ppIn = new ProfilePreferenceUpdateDTO();
		ppIn.setId(null);
		ppIn.setTimezone("my/updated/timezone");
				
		ProfilePreference pp = new ProfilePreference();
		pp.setId(2);
		pp.setTimezone("my/timezone");
		
		User user = new User();
		user.setUsername("username");
		user.setProfilePreference(pp);
		
		Mockito.when(requestMiner.getCurrentUsername()).thenReturn("username");
		Mockito.when(userRepository.findByUsername("username")).thenReturn(user);
		
		ProfilePreferenceDTO ppDto = profilePreferenceServiceImpl.updateProfilePreferences(ppIn);
		
		assertEquals("Timezone not as expected", ppIn.getTimezone(), ppDto.getTimezone());
	}
	
	@Test
	public void updateProfilePreferences_2() {
		
		// User has no profile preference
		
		ProfilePreferenceUpdateDTO ppIn = new ProfilePreferenceUpdateDTO();
		ppIn.setId(null);
		ppIn.setTimezone("my/new/timezone");
				
		ProfilePreference pp = new ProfilePreference();
		pp.setId(1);
		pp.setTimezone("global/timezone");
		
		ProfilePreference ppOut = new ProfilePreference();
		ppOut.setId(2);
		ppOut.setTimezone("my/new/timezone");
		
		User user = new User();
		user.setUsername("username");
		user.setProfilePreference(null);
		
		Mockito.when(requestMiner.getCurrentUsername()).thenReturn("username");
		Mockito.when(userRepository.findByUsername("username")).thenReturn(user);
		Mockito.when(profilePreferenceRepository.save(ArgumentMatchers.any(ProfilePreference.class))).thenReturn(ppOut);
		
		ProfilePreferenceDTO ppDto = profilePreferenceServiceImpl.updateProfilePreferences(ppIn);
		
		assertEquals("Timezone not as expected", ppIn.getTimezone(), ppDto.getTimezone());
	}
	
}
