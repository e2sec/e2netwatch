package de.e2security.netflow_flowaggregation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Properties;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.time.CurrentTimeEvent;

import de.e2security.netflow_flowaggregation.esper.ProtocolRegisterListener;
import de.e2security.netflow_flowaggregation.esper.ProtocolRegisterTrigger;
import de.e2security.netflow_flowaggregation.esper.epl.TcpEplExpressions;
import de.e2security.netflow_flowaggregation.esper.epl.UdpEplExpressions;
import de.e2security.netflow_flowaggregation.kafka.CustomKafkaProducer;
import de.e2security.netflow_flowaggregation.kafka.KafkaConsumerMaster;
import de.e2security.netflow_flowaggregation.model.protocols.NetflowEvent;
import de.e2security.netflow_flowaggregation.model.protocols.NetflowEventOrdered;
import de.e2security.netflow_flowaggregation.model.protocols.ProtocolRegister;
import de.e2security.netflow_flowaggregation.utils.EsperUtil;
import de.e2security.netflow_flowaggregation.utils.PropertiesUtil;
import de.e2security.netflow_flowaggregation.utils.ThreadUtil;
import de.e2security.netflow_flowaggregation.utils.UpstartUtil;

public class App {
	
	
	private static final Logger LOG = LoggerFactory.getLogger(App.class);
	
	private Properties configs = new Properties();
	private File configFile;
	
	@Option(name = "-c", usage = "defines additional configuration file")
	public void setFile(File f) {
		if (f.exists()) configFile = f;
		else { 
			System.err.println("cannot read config file " + f.getName());
			System.exit(1);
		}
	}
	
	public static void main(String[] args) { 
		new App().doMain(args);  // Commandline Parser cannot use static object, so we use this ugly workaround
	}
	
	public void doMain(String[] args) {
		//read Default Configuration and Parse Arguments
		configs = new PropertiesUtil(configs).read("application.properties");
		
		if (args.length > 0) {
			CmdLineParser parser = new CmdLineParser(this);
			try {
				parser.parseArgument(args);
				if (this.configFile != null) {
					try {
						InputStream is = new FileInputStream(configFile);
						configs.load(is);
					} catch (IOException e) {
						System.err.println("Cannot read config file '" + configFile.getName() + "'");
						System.exit(1);
					}
				}
			} catch (CmdLineException e) {
				System.err.println(e.getMessage());
				System.err.println("Available options:");
				parser.printUsage(System.err);
				System.exit(1);
			}
		}
		
		LOG.info("Starting version {} ({})", configs.getProperty("application.version"), configs.getProperty("application.builddate"));

		//check kafka server availability before establishing connection
		new UpstartUtil(configs).statusKafka();
		
		//start KafkaProducer
		final CustomKafkaProducer<Serializable, Serializable> producer = new CustomKafkaProducer<>(configs);

		//register EP events
		EPServiceProvider epService = EsperUtil.registerEvents(NetflowEvent.class, NetflowEventOrdered.class, ProtocolRegister.class, CurrentTimeEvent.class);
		
		//register TCP EPLs @see description in TcpEplExpressions
		epService.getEPAdministrator().createEPL(TcpEplExpressions.tcpSortByLastSwitched());
		epService.getEPAdministrator().createEPL(UdpEplExpressions.udpSortByLastSwitched());
		epService.getEPAdministrator().createEPL(TcpEplExpressions.eplFinishedFlows());
		epService.getEPAdministrator().createEPL(TcpEplExpressions.eplRejectedFlows(TcpEplExpressions.eplRejectedPatternSyn2Ack16()));
		epService.getEPAdministrator().createEPL(TcpEplExpressions.eplRejectedFlows(TcpEplExpressions.eplRejectedPatternRst4()));
		
		//register UDP EPLs @see description in UdpEplExpressions
		epService.getEPAdministrator().createEPL(UdpEplExpressions.eplFinishedUDPFlows());

		//monitor incoming tcp/udp flows for debugging
		EPStatement statementGetTCPFlowsMonitor = epService.getEPAdministrator().createEPL("select * from NetflowEvent(protocol=6)");
		EPStatement statementGetUDPFlowsMonitor = epService.getEPAdministrator().createEPL("select * from NetflowEvent(protocol=17)");
		statementGetTCPFlowsMonitor.addListener(new ProtocolRegisterListener());
		statementGetUDPFlowsMonitor.addListener(new ProtocolRegisterListener());

		//send processed output to kafka
		EPStatement statementTcpConnectionTrigger = epService.getEPAdministrator().createEPL("select * from ProtocolRegister");
		statementTcpConnectionTrigger.addListener(new ProtocolRegisterTrigger(producer));
		
		//start KafkaConsumer with prepared EPService after checking topics' availability within consumerMaster 
		KafkaConsumerMaster consumerMaster = new KafkaConsumerMaster(epService, configs).waitTopics().startWorkers();
		
		//give overview about started threads
		ThreadUtil.printThreads();
		//manage graceful threads shutdown
		ThreadUtil.manageShutdown(consumerMaster, producer, epService);

	}

}
