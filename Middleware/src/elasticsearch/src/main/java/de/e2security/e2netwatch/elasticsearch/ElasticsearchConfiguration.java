package de.e2security.e2netwatch.elasticsearch;

import org.apache.http.HttpHost;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource({ "classpath:elasticsearch/persistence-${envTarget:dev}.properties"})
public class ElasticsearchConfiguration extends AbstractFactoryBean<RestHighLevelClient> {
	
	private final Logger logger = LogManager.getLogger(ElasticsearchConfiguration.class);
	
	@Value( "${es.hostname}" )
	private String es_hostname;
	@Value( "${es.port}" )
	private int es_port;
	@Value( "${es.scheme}" )
	private String es_scheme;
	
    private RestHighLevelClient restHighLevelClient;

    @Override
    public void destroy() {
        try {
            if (restHighLevelClient != null) {
                restHighLevelClient.close();
            }
        } catch (final Exception e) {
            logger.error("Error closing ElasticSearch client: ", e);
        }
    }

    @Override
    public Class<RestHighLevelClient> getObjectType() {
        return RestHighLevelClient.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    @Override
    public RestHighLevelClient createInstance() {
        return buildClient();
    }

    private RestHighLevelClient buildClient() {
    	logger.info("Building Elasticsearch client: " + es_scheme + "://" + es_hostname + ":" + es_port);
        try {
            restHighLevelClient = new RestHighLevelClient(
                    RestClient.builder(
                            new HttpHost(es_hostname, es_port, es_scheme)));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return restHighLevelClient;
    }

}
