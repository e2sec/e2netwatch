package de.e2security.netflow_flowaggregation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.json.JSONObject;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.espertech.esper.client.ConfigurationOperations;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
//import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.time.CurrentTimeEvent;

public class App {
	private File configFile;

	@Option(name = "-c", usage = "defines additional configuration file")
	public void setFile(File f) {
		if (f.exists()) {
			configFile = f;
		} else {
			System.err.println("Cannot read config file '" + f.getName() + "'");
			System.exit(1);
		}
	}

	private static final Logger LOG = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) {
		// Commandline Parser cannot use static object, so we use this ugly workaround
		new App().doMain(args);
	}

	public void doMain(String[] args) {
		/*
		 * Read default configuration
		 */
		Properties configs = new Properties();
		try {
			InputStream is = App.class.getClassLoader().getResourceAsStream("application.properties");
			configs.load(is);
			is.close();
		} catch (IOException e) {
			System.err.println("Cannot load properties!");
			System.exit(1);
		}

		/*
		 * Parse Arguments
		 */
		if (args.length > 0) {
			CmdLineParser parser = new CmdLineParser(this);

			try {
				parser.parseArgument(args);

				if (configFile != null) {
					try {
						/*
						 * Read additional configuration
						 */
						InputStream is = new FileInputStream(configFile);
						configs.load(is);
					} catch (IOException e) {
						System.err.println("Cannot read config file '" + configFile.getName() + "'");
						System.exit(1);
					}
				}
			} catch (CmdLineException e) {
				System.err.println(e.getMessage());
				System.err.println("Available sptions:");
				parser.printUsage(System.err);
				System.exit(1);
			}
		}

		/*
		 * Build Kafka Config
		 */
		Properties configKafkaIn = getKafkaConfigIn(configs);
		Properties configKafkaOut = getKafkaConfigOut(configs);

		List<String> topicsIn = Arrays.asList(configs.get("consumer.topic").toString());
		String topicOut = configs.get("producer.topic").toString();

		/*
		 * Start KafkaProducer
		 */
		final CustomKafkaProducer<Serializable, Serializable> producer = new CustomKafkaProducer<>(configKafkaOut,
				topicOut);

		/*
		 * Get EPL provider and configuration
		 */
		EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider();
		// EPRuntime epRuntime = epService.getEPRuntime();
		ConfigurationOperations epConfiguration = epService.getEPAdministrator().getConfiguration();

		/*
		 * Register events
		 */
		epConfiguration.addEventType(NetflowEvent.class);
		epConfiguration.addEventType(NetflowEventOrdered.class);
		epConfiguration.addEventType(TcpConnection.class);
		epConfiguration.addEventType(CurrentTimeEvent.class);

		// @formatter:off
		/*
		 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		 * ! -- CAUTION --
		 * ! inserted fields need to match constructor arguments
		 * ! in type and order
		 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		 */
		String UdpFields = ",a.receivedTimeStamp as in_receivedTimeStamp"
				+ ",b.receivedTimeStamp as out_receivedTimeStamp"
				+ ",a.host as host"
				+ ",a.ipv4_src_addr as ipv4_src_addr"
				+ ",a.ipv4_dst_addr as ipv4_dst_addr"
				+ ",a.l4_src_port as l4_src_port"
				+ ",a.l4_dst_port as l4_dst_port"
				+ ",a.protocol as protocol"
				+ ",a.flow_seq_num as in_flow_seq_num"	
				+ ",b.flow_seq_num as out_flow_seq_num"
				+ ",a.flow_records as in_flow_records"
				+ ",b.flow_records as out_flow_records"
				+ ",a.in_bytes as in_bytes"
				+ ",b.in_bytes as out_bytes"
				+ ",a.in_pkts as in_pkts"
				+ ",b.in_pkts as out_pkts"
				+ ",a.first_switched as in_first_switched"
				+ ",b.first_switched as out_first_switched"
				+ ",a.last_switched as in_last_switched"
				+ ",b.last_switched as out_last_switched";
		String TcpFields = ",a.receivedTimeStamp as in_receivedTimeStamp"
				+ ",b.receivedTimeStamp as out_receivedTimeStamp"
				+ ",a.host as host"
				+ ",a.ipv4_src_addr as ipv4_src_addr"
				+ ",a.ipv4_dst_addr as ipv4_dst_addr"
				+ ",a.l4_src_port as l4_src_port"
				+ ",a.l4_dst_port as l4_dst_port"
				+ ",a.tcp_flags as in_tcp_flags"
				+ ",b.tcp_flags as out_tcp_flags"
				+ ",a.protocol as protocol"
				+ ",a.flow_seq_num as in_flow_seq_num"
				+ ",b.flow_seq_num as out_flow_seq_num"
				+ ",a.flow_records as in_flow_records"
				+ ",b.flow_records as out_flow_records"
				+ ",a.in_bytes as in_bytes"
				+ ",b.in_bytes as out_bytes"
				+ ",a.in_pkts as in_pkts"
				+ ",b.in_pkts as out_pkts"
				+ ",a.first_switched as in_first_switched"
				+ ",b.first_switched as out_first_switched"
				+ ",a.last_switched as in_last_switched"
				+ ",b.last_switched as out_last_switched";
		
		/*
		 * Get events into correct order
		 */
		String eplSortByLastSwitched = "insert rstream into NetflowEventOrdered"
				+ " select receivedTimeStamp"
				+ ",host"
				+ ",ipv4_src_addr"
				+ ",ipv4_dst_addr"
				+ ",l4_src_port"
				+ ",l4_dst_port"
				+ ",tcp_flags"
				+ ",protocol"
				+ ",version"
				+ ",flow_seq_num"
				+ ",flow_records"
				+ ",in_bytes"
				+ ",in_pkts"
				+ ",first_switched"
				+ ",last_switched"
				+ " from NetflowEvent.ext:time_order(last_switched.toMilliSec(), 60 sec)";
		epService.getEPAdministrator().createEPL(eplSortByLastSwitched);
		
		/*
		 * Finished TCP Flow
		 * FIN flag (1) set on both flows
		 */
		String eplFinishedTCPFlows = "insert into TcpConnection select"
				+ " 'Finished TCP' as description"
				+ TcpFields
				+ " from pattern [every a=NetflowEventOrdered(protocol=6 and (tcp_flags&1)=1) ->"
				+ " b=NetflowEventOrdered(protocol=6 and (tcp_flags%2)=1 and host=a.host "
				+ " and ipv4_src_addr = a.ipv4_dst_addr"
				+ " and l4_src_port   = a.l4_dst_port"
				+ " and ipv4_dst_addr = a.ipv4_src_addr"
				+ " and l4_dst_port   = a.l4_src_port)"
				+ " where timer:within(60 sec)]";
		epService.getEPAdministrator().createEPL(eplFinishedTCPFlows);

		/*
		 * Rejected TCP connection
		 * first flow: SYN (2) set, but ACK (16) not set
		 * second flow: RST (4) set
		 * As we cannot rely on the correct order of netflow data,
		 * so we define two EPL statements 
		 */
		String eplRejectedTCPFlows1 = "insert into TcpConnection select"
				+ " 'Rejected TCP' as description"
				+ TcpFields
				+ " from pattern [every a=NetflowEventOrdered(protocol=6 and (tcp_flags&2)=2 and (tcp_flags&16)=0) ->"
				+ " b=NetflowEventOrdered(protocol=6 and (tcp_flags&4)=4 and host=a.host "
				+ " and ipv4_src_addr = a.ipv4_dst_addr"
				+ " and l4_src_port   = a.l4_dst_port"
				+ " and ipv4_dst_addr = a.ipv4_src_addr"
				+ " and l4_dst_port   = a.l4_src_port)"
				+ " where timer:within(60 sec)]";
		String eplRejectedTCPFlows2 = "insert into TcpConnection select"
				+ " 'Rejected TCP' as description"
				+ TcpFields
				+ " from pattern [every a=NetflowEventOrdered(protocol=6 and (tcp_flags&4)=4) ->"
				+ " b=NetflowEventOrdered(protocol=6 and (tcp_flags&2)=2 and (tcp_flags&16)=0 and host=a.host "
				+ " and ipv4_src_addr = a.ipv4_dst_addr"
				+ " and l4_src_port   = a.l4_dst_port"
				+ " and ipv4_dst_addr = a.ipv4_src_addr"
				+ " and l4_dst_port   = a.l4_src_port)"
				+ " where timer:within(60 sec)]";
		epService.getEPAdministrator().createEPL(eplRejectedTCPFlows1);
		epService.getEPAdministrator().createEPL(eplRejectedTCPFlows2);

		/*
		 * Finished UDP connection
		 * no further packets in both directions after 2 minutes
		 */
		String eplFinishedUDPFlows = "insert into UdpConnection select"
				+ " 'Finished UDP' as description"
				+ UdpFields
				+ " from pattern [every a=NetflowEventOrdered(protocol=17) ->"
				+ " b=NetflowEventOrdered(protocol=17 and host=a.host "
				+ " and ipv4_src_addr = a.ipv4_dst_addr"
				+ " and l4_src_port   = a.l4_dst_port"
				+ " and ipv4_dst_addr = a.ipv4_src_addr"
				+ " and l4_dst_port   = a.l4_src_port)"
				+ " -> "
				+ "(timer:interval(120 sec)"
				+ " and not d=NetflowEventOrdered(protocol=17"
				+ " and host=a.host"
				+ " and ipv4_src_addr = a.ipv4_dst_addr"
				+ " and l4_src_port   = a.l4_dst_port"
				+ " and ipv4_dst_addr = a.ipv4_src_addr"
				+ " and l4_dst_port   = a.l4_src_port)"
				+ " and not "
				+ " e=NetflowEventOrdered(protocol=17 and host=a.host "
				+ " and ipv4_src_addr = a.ipv4_dst_addr"
				+ " and l4_src_port   = a.l4_dst_port"
				+ " and ipv4_dst_addr = a.ipv4_src_addr"
				+ " and l4_dst_port   = a.l4_src_port)"
				+ ")]";
		epService.getEPAdministrator().createEPL(eplFinishedUDPFlows);
		// @formatter:on

		/*
		 * Monitor incoming tcp flows for debugging
		 */
		String eplGetTCPFlowsMonitor = "select host, ipv4_src_addr, l4_src_port, ipv4_dst_addr, l4_dst_port, in_bytes, first_switched from NetflowEvent(protocol=6)";
		EPStatement statementGetTCPFlowsMonitor = epService.getEPAdministrator().createEPL(eplGetTCPFlowsMonitor);
		statementGetTCPFlowsMonitor.addListener((newData, oldData) -> {
			Integer in_bytes = (Integer) newData[0].get("in_bytes");
			String srcaddr = (String) newData[0].get("ipv4_src_addr");
			Integer srcport = (Integer) newData[0].get("l4_src_port");
			String dstaddr = (String) newData[0].get("ipv4_dst_addr");
			Integer dstport = (Integer) newData[0].get("l4_dst_port");
			ZonedDateTime first_switched = (ZonedDateTime) newData[0].get("first_switched");
			LOG.info(String.format("TCP %s:%d -> %s:%d (%d Bytes) %s", srcaddr, srcport, dstaddr, dstport, in_bytes,
					first_switched.toString()));
		});

		/*
		 * Monitor incoming udp flows for debugging
		 */
		String eplGetUDPFlowsMonitor = "select host, ipv4_src_addr, l4_src_port, ipv4_dst_addr, l4_dst_port, in_bytes, first_switched from NetflowEvent(protocol=17)";
		EPStatement statementGetUDPFlowsMonitor = epService.getEPAdministrator().createEPL(eplGetUDPFlowsMonitor);
		statementGetUDPFlowsMonitor.addListener((newData, oldData) -> {
			Integer in_bytes = (Integer) newData[0].get("in_bytes");
			String srcaddr = (String) newData[0].get("ipv4_src_addr");
			Integer srcport = (Integer) newData[0].get("l4_src_port");
			String dstaddr = (String) newData[0].get("ipv4_dst_addr");
			Integer dstport = (Integer) newData[0].get("l4_dst_port");
			ZonedDateTime first_switched = (ZonedDateTime) newData[0].get("first_switched");
			LOG.info(String.format("UDP %s:%d -> %s:%d (%d Bytes) %s", srcaddr, srcport, dstaddr, dstport, in_bytes,
					first_switched.toString()));
		});

		String eplTcpConnectionTrigger = "select * from TcpConnection";
		EPStatement statementTcpConnectionTrigger = epService.getEPAdministrator().createEPL(eplTcpConnectionTrigger);
		statementTcpConnectionTrigger.addListener((newData, oldData) -> {
			String description = (String) newData[0].get("description");
			String host = (String) newData[0].get("host");
			String srcaddr = (String) newData[0].get("ipv4_src_addr");
			Integer srcport = (Integer) newData[0].get("l4_src_port");
			String dstaddr = (String) newData[0].get("ipv4_dst_addr");
			Integer dstport = (Integer) newData[0].get("l4_dst_port");
			Integer protocol = (Integer) newData[0].get("protocol");
			Integer in_flow_seq_num = (Integer) newData[0].get("in_flow_seq_num");
			Integer in_flow_records = (Integer) newData[0].get("in_flow_records");
			Integer out_flow_seq_num = (Integer) newData[0].get("out_flow_seq_num");
			Integer out_flow_records = (Integer) newData[0].get("out_flow_records");
			Integer in_bytes = (Integer) newData[0].get("in_bytes");
			Integer out_bytes = (Integer) newData[0].get("out_bytes");
			Integer in_pkts = (Integer) newData[0].get("in_pkts");
			Integer out_pkts = (Integer) newData[0].get("out_pkts");
			Integer in_tcp_flags = (Integer) newData[0].get("in_tcp_flags");
			Integer out_tcp_flags = (Integer) newData[0].get("out_tcp_flags");
			ZonedDateTime in_first_switched = (ZonedDateTime) newData[0].get("in_first_switched");
			ZonedDateTime out_first_switched = (ZonedDateTime) newData[0].get("out_first_switched");
			ZonedDateTime in_last_switched = (ZonedDateTime) newData[0].get("in_last_switched");
			ZonedDateTime out_last_switched = (ZonedDateTime) newData[0].get("out_last_switched");
			LOG.info(String.format("%s Connection %s:%d -> %s:%d (%d/%d Bytes)", description, srcaddr, srcport, dstaddr,
					dstport,
					in_bytes, out_bytes));
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("description", description);
			jsonObject.put("host", host);
			jsonObject.put("ipv4_src_addr", srcaddr);
			jsonObject.put("l4_src_port", srcport);
			jsonObject.put("ipv4_dst_addr", dstaddr);
			jsonObject.put("l4_dst_port", dstport);
			jsonObject.put("protocol", protocol);
			jsonObject.put("in_flow_seq_num", in_flow_seq_num);
			jsonObject.put("in_flow_records", in_flow_records);
			jsonObject.put("out_flow_seq_num", out_flow_seq_num);
			jsonObject.put("out_flow_records", out_flow_records);
			jsonObject.put("in_bytes", in_bytes);
			jsonObject.put("out_bytes", out_bytes);
			jsonObject.put("in_pkts", in_pkts);
			jsonObject.put("out_pkts", out_pkts);
			jsonObject.put("in_tcp_flags", in_tcp_flags);
			jsonObject.put("out_tcp_flags", out_tcp_flags);
			jsonObject.put("in_first_switched", in_first_switched);
			jsonObject.put("out_first_switched", out_first_switched);
			jsonObject.put("in_last_switched", in_last_switched);
			jsonObject.put("out_last_switched", out_last_switched);
			producer.send(jsonObject.toString());
		});

		String eplUdpConnectionTrigger = "select * from UdpConnection";
		EPStatement statementUdpConnectionTrigger = epService.getEPAdministrator().createEPL(eplUdpConnectionTrigger);
		statementUdpConnectionTrigger.addListener((newData, oldData) -> {
			String description = (String) newData[0].get("description");
			String host = (String) newData[0].get("host");
			String srcaddr = (String) newData[0].get("ipv4_src_addr");
			Integer srcport = (Integer) newData[0].get("l4_src_port");
			String dstaddr = (String) newData[0].get("ipv4_dst_addr");
			Integer dstport = (Integer) newData[0].get("l4_dst_port");
			Integer protocol = (Integer) newData[0].get("protocol");
			Integer in_flow_seq_num = (Integer) newData[0].get("in_flow_seq_num");
			Integer in_flow_records = (Integer) newData[0].get("in_flow_records");
			Integer out_flow_seq_num = (Integer) newData[0].get("out_flow_seq_num");
			Integer out_flow_records = (Integer) newData[0].get("out_flow_records");
			Integer in_bytes = (Integer) newData[0].get("in_bytes");
			Integer out_bytes = (Integer) newData[0].get("out_bytes");
			Integer in_pkts = (Integer) newData[0].get("in_pkts");
			Integer out_pkts = (Integer) newData[0].get("out_pkts");
			ZonedDateTime in_first_switched = (ZonedDateTime) newData[0].get("in_first_switched");
			ZonedDateTime out_first_switched = (ZonedDateTime) newData[0].get("out_first_switched");
			ZonedDateTime in_last_switched = (ZonedDateTime) newData[0].get("in_last_switched");
			ZonedDateTime out_last_switched = (ZonedDateTime) newData[0].get("out_last_switched");
			LOG.info(String.format("%s Connection %s:%d -> %s:%d (%d/%d Bytes)", description, srcaddr, srcport, dstaddr,
					dstport,
					in_bytes, out_bytes));
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("description", description);
			jsonObject.put("host", host);
			jsonObject.put("ipv4_src_addr", srcaddr);
			jsonObject.put("l4_src_port", srcport);
			jsonObject.put("ipv4_dst_addr", dstaddr);
			jsonObject.put("l4_dst_port", dstport);
			jsonObject.put("protocol", protocol);
			jsonObject.put("in_flow_seq_num", in_flow_seq_num);
			jsonObject.put("in_flow_records", in_flow_records);
			jsonObject.put("out_flow_seq_num", out_flow_seq_num);
			jsonObject.put("out_flow_records", out_flow_records);
			jsonObject.put("in_bytes", in_bytes);
			jsonObject.put("out_bytes", out_bytes);
			jsonObject.put("in_pkts", in_pkts);
			jsonObject.put("out_pkts", out_pkts);
			jsonObject.put("in_first_switched", in_first_switched);
			jsonObject.put("out_first_switched", out_first_switched);
			jsonObject.put("in_last_switched", in_last_switched);
			jsonObject.put("out_last_switched", out_last_switched);
			producer.send(jsonObject.toString());
		});

		/*
		 * Prepare KafkaConsumer
		 */
		Callable<Integer> consumer = new CustomKafkaConsumer<>(configKafkaIn, topicsIn, epService);

		/*
		 * Start KafkaConsumer
		 */
		ExecutorService exec = Executors.newFixedThreadPool(1);
		Future<Integer> future = exec.submit(consumer);

		/*
		 * define shutdown procedure
		 */
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					LOG.info("Exiting...");

					// Stop Consumer
					future.cancel(true);

					// Stop Producer
					producer.flush();
					producer.close();

					// Stop ESPER
					epService.destroy();

					// Close thread pool
					exec.shutdown();
				} catch (ConcurrentModificationException ignore) {
					/*
					 * KafkaConsumer is not thread safe. As we have only one consumer thread we may
					 * ignore the exception.
					 */
				}
			}
		}));

		/*
		 * wait for Consumer => endless loop
		 */
		try {
			@SuppressWarnings("unused")
			int result = future.get();
		} catch (Exception ignore) {
			// ending the endless loop
		}
	}

	private static Properties getKafkaConfigIn(Properties config) {
		Properties configIn = new Properties();
		configIn.put("bootstrap.servers", config.get("bootstrap.servers"));
		configIn.put("enable.auto.commit", config.get("enable.auto.commit"));
		configIn.put("auto.commit.interval.ms", config.get("auto.commit.interval.ms"));
		configIn.put("session.timeout.ms", config.get("session.timeout.ms"));
		configIn.put("client.id", config.get("consumer.client.id"));
		configIn.put("group.id", config.get("consumer.group.id"));
		configIn.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		configIn.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		return configIn;
	}

	private static Properties getKafkaConfigOut(Properties config) {
		Properties configOut = new Properties();
		configOut.put("bootstrap.servers", config.get("bootstrap.servers"));
		configOut.put("client.id", config.get("producer.client.id"));
		configOut.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		configOut.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		return configOut;
	}

	@SuppressWarnings("unused")
	private static void printThreads() {
		System.out.println("");
		System.out.println("Current threads:");

		Set<Thread> threads = Thread.getAllStackTraces().keySet();

		for (Thread t : threads) {
			String name = t.getName();
			Thread.State state = t.getState();
			int priority = t.getPriority();
			String type = t.isDaemon() ? "Daemon" : "Normal";
			System.out.printf("%-20s \t %s \t %d \t %s\n", name, state, priority, type);
		}
	}

}
