package de.e2security.e2netwatch.elasticsearch.service;

import de.e2security.e2netwatch.elasticsearch.dto.ClusterHealth;

/**
 * Service to get information about Elasticsearch cluster
 * 
 * @author Hrvoje
 *
 */
public interface ClusterService {
	
	/**
	 * Get info about cluster
	 * 
	 * @return cluster health info data
	 */
	ClusterHealth getInfo();

}
