package de.e2security.netflow_flowaggregation.utils;

import java.net.Socket;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.e2security.netflow_flowaggregation.App;

public class UpstartUtil {
	
	private static final Logger LOG = LoggerFactory.getLogger(App.class);

	private final String host;
	private final int port;
	private final int kafkaUptime;
	
	public UpstartUtil(Properties props) {
		String bootstrapServers = props.getProperty("bootstrap.servers");
		this.host = bootstrapServers.split(":")[0];
		this.port = Integer.valueOf(bootstrapServers.split(":")[1]);
		this.kafkaUptime = Integer.valueOf(props.getProperty("kafka.uptime"));
	}
	
	//check for host and port being there:
	private boolean isHostKafkaAvailable(){
		boolean ret = false;
		try {
			Socket soc = new Socket(this.host, this.port);
			ret = true;
		} catch (Exception e) { }
		return ret;
	}
	//check for topic availability
	
	//output status of each check (waiting, success, failed)
	public void status() {
		int waiting = kafkaUptime;
		while (!isHostKafkaAvailable()) {
			if (waiting == 0) {
				LOG.error("Kafka on {}:{} hasn't been started. Process terminated", this.host, this.port, waiting);
				System.exit(1);
			}
			try {
				LOG.info("waiting for {}:{}\t {} sec remains", this.host, this.port, waiting);
				Thread.sleep(1000);
				waiting--;
			} catch (InterruptedException e) { }
		}
		LOG.info("Kafka host was found. Trying establish connection...");
	}
}
