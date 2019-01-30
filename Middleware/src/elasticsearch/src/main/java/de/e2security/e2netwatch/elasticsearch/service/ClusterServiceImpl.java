package de.e2security.e2netwatch.elasticsearch.service;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.e2security.e2netwatch.elasticsearch.dao.ClusterDAO;
import de.e2security.e2netwatch.elasticsearch.dto.ClusterHealth;
import de.e2security.e2netwatch.elasticsearch.dto.ClusterInfo;

/**
 * Implementation of ClusterService
 * 
 * @author Hrvoje
 *
 */
@Service
public class ClusterServiceImpl implements ClusterService {
	
	private final Logger logger = LogManager.getLogger(ClusterServiceImpl.class);
	
	ClusterDAO clusterDAO;
	
	@Autowired
	public ClusterServiceImpl(ClusterDAO clusterDAO) {
		this.clusterDAO = clusterDAO;
	}

	/*
	 * (non-Javadoc)
	 * @see de.e2security.e2netwatch.elasticsearch.service.ClusterService#getInfo()
	 */
	@Override
	public ClusterHealth getInfo() {
		try {
			ClusterInfo info = clusterDAO.info();
			return new ClusterHealth(
					info.getName(), 
					clusterDAO.ping(), 
					info.getVersion());
		} catch (IOException e) {
			logger.warn(String.format("Getting Elasticsearch cluster info: %s", e.getMessage()));
			return new ClusterHealth(null, false, null);
		}
	}

}
