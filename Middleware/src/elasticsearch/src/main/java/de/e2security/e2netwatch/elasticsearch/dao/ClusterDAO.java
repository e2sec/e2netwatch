package de.e2security.e2netwatch.elasticsearch.dao;

import java.io.IOException;

import org.elasticsearch.action.main.MainResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.e2security.e2netwatch.elasticsearch.dto.ClusterInfo;

/**
 * Implementation of ClusterService
 * 
 * @author Hrvoje
 *
 */
@Component
public class ClusterDAO {
	
	RestHighLevelClient restClient;
	
	@Autowired
	public ClusterDAO(RestHighLevelClient restClient) {
		this.restClient = restClient;
	}
	
	public ClusterInfo info() throws IOException {
		MainResponse response = restClient.info();
		return new ClusterInfo(response.getClusterName().value(), response.getVersion().toString());
	}
	
	public boolean ping() throws IOException {
		return restClient.ping();
	}

}
