package de.e2security.e2netwatch.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.e2security.e2netwatch.elasticsearch.dto.ClusterHealth;
import de.e2security.e2netwatch.elasticsearch.service.ClusterService;
import de.e2security.e2netwatch.utils.constants.Mappings;

/**
 * Rest endpoint for Cluster inquiries
 *
 * @author Hrvoje
 *
 */
@RestController
@RequestMapping(value = Mappings.CLUSTER)
@PreAuthorize("hasRole('ADMIN')")
public class ClusterController {
	
	@Autowired
	private ClusterService manager;
	
	/**
	 * Get all user statuses
	 * 
	 * @return list of all user statuses
	 */
	@RequestMapping(value = "/info", method = RequestMethod.GET, headers = "Accept=application/json")
	public ClusterHealth getClusterInfo() {
		return manager.getInfo();
	}
	
}
