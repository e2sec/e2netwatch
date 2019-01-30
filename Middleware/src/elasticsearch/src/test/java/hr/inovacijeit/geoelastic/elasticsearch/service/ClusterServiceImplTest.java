package hr.inovacijeit.geoelastic.elasticsearch.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import de.e2security.e2netwatch.elasticsearch.dao.ClusterDAO;
import de.e2security.e2netwatch.elasticsearch.dto.ClusterHealth;
import de.e2security.e2netwatch.elasticsearch.dto.ClusterInfo;
import de.e2security.e2netwatch.elasticsearch.service.ClusterService;
import de.e2security.e2netwatch.elasticsearch.service.ClusterServiceImpl;

/**
 * Tests the ClusterService.
 *
 * @author Hrvoje
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(ClusterServiceImpl.class)
public class ClusterServiceImplTest {
	
	private ClusterService clusterService;
	
	@Mock private ClusterDAO clusterDao;
	
	@Before
	public void setup() throws Exception {
		MockitoAnnotations.initMocks(this);
		clusterService = PowerMockito.spy(new ClusterServiceImpl(clusterDao));
	}
	
	/*
	 * getInfo
	 */
	
	@Test
	public void getInfo_1() throws IOException {
		
		// when cluster available
		
		ClusterInfo ci = new ClusterInfo("clusterName", "1.version");
		
		when(clusterDao.info()).thenReturn(ci);
		when(clusterDao.ping()).thenReturn(true);
		
		ClusterHealth health = clusterService.getInfo();
		
		assertEquals("Cluster name not as expected", ci.getName(), health.getName());
		assertTrue("Cluster availability should be true", health.isAvailable());
	}
	
	@Test
	public void getInfo_2() throws IOException {
		
		// when connection refused to ES cluster
		
		when(clusterDao.info()).thenThrow(IOException.class);
		when(clusterDao.ping()).thenReturn(true);
		
		ClusterHealth health = clusterService.getInfo();
		
		assertFalse("Cluster availability should be false", health.isAvailable());
	}
	
}
