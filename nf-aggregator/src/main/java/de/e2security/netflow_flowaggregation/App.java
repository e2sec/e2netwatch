package de.e2security.netflow_flowaggregation;

import java.io.Serializable;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.time.CurrentTimeEvent;

import de.e2security.netflow_flowaggregation.esper.TcpConnectionTrigger;
import de.e2security.netflow_flowaggregation.esper.TcpEplExpressions;
import de.e2security.netflow_flowaggregation.esper.TcpFlowMonitorListener;
import de.e2security.netflow_flowaggregation.esper.UdpConnectionTrigger;
import de.e2security.netflow_flowaggregation.esper.UdpEplExpressions;
import de.e2security.netflow_flowaggregation.esper.UdpFlowMonitorListener;
import de.e2security.netflow_flowaggregation.kafka.CustomKafkaProducer;
import de.e2security.netflow_flowaggregation.kafka.KafkaConsumerMaster;
import de.e2security.netflow_flowaggregation.netflow.NetflowEvent;
import de.e2security.netflow_flowaggregation.netflow.NetflowEventOrdered;
import de.e2security.netflow_flowaggregation.utils.EsperUtil;
import de.e2security.netflow_flowaggregation.utils.PropertiesUtil;
import de.e2security.netflow_flowaggregation.utils.ThreadUtil;

public class App {
	
	private static final Logger LOG = LoggerFactory.getLogger(App.class);
	
	public static void main(String[] args) { new App().doMain(args); } // Commandline Parser cannot use static object, so we use this ugly workaround
	public void doMain(String[] args) {
	
		LOG.info("VERSION 0.0.1"); //just marker to see the right update in the docker container

		//read Default Configuration and Parse Arguments
		Properties configs = new PropertiesUtil().readInt("application.properties")
											     .readExt(args, App.class)
											     .create();

		//start KafkaProducer
		final CustomKafkaProducer<Serializable, Serializable> producer = new CustomKafkaProducer<>(configs);

		//register EP events
		EPServiceProvider epService = EsperUtil.registerEvents(NetflowEvent.class, NetflowEventOrdered.class, TcpConnection.class, CurrentTimeEvent.class);

		//register TCP EPLs @see description in TcpEplExpressions
		String rejectedPattern1 = "[every a=NetflowEventOrdered(protocol=6 and (tcp_flags&2)=2 and (tcp_flags&16)=0) -> "
								+ " b=NetflowEventOrdered(protocol=6 and (tcp_flags&4)=4 and host=a.host ";
		String rejectedPattern2 = "[every a=NetflowEventOrdered(protocol=6 and (tcp_flags&4)=4) ->"
							 	+ " b=NetflowEventOrdered(protocol=6 and (tcp_flags&2)=2 and (tcp_flags&16)=0 and host=a.host ";
		epService.getEPAdministrator().createEPL(TcpEplExpressions.eplSortByLastSwitched());
		epService.getEPAdministrator().createEPL(TcpEplExpressions.eplFinishedFlows());
		epService.getEPAdministrator().createEPL(TcpEplExpressions.eplRejectedFlows(rejectedPattern1));
		epService.getEPAdministrator().createEPL(TcpEplExpressions.eplRejectedFlows(rejectedPattern2));
		
		//register UDP EPLs @see description in UdpEplExpressions
		epService.getEPAdministrator().createEPL(UdpEplExpressions.eplFinishedUDPFlows());

		//monitor incoming tcp flows for debugging
		String eplGetTCPFlowsMonitor = "select host, ipv4_src_addr, l4_src_port, ipv4_dst_addr, l4_dst_port, in_bytes, first_switched from NetflowEvent(protocol=6)";
		EPStatement statementGetTCPFlowsMonitor = epService.getEPAdministrator().createEPL(eplGetTCPFlowsMonitor);
		statementGetTCPFlowsMonitor.addListener(new TcpFlowMonitorListener());

		//monitor incoming udp flows for debugging
		String eplGetUDPFlowsMonitor = "select host, ipv4_src_addr, l4_src_port, ipv4_dst_addr, l4_dst_port, in_bytes, first_switched from NetflowEvent(protocol=17)";
		EPStatement statementGetUDPFlowsMonitor = epService.getEPAdministrator().createEPL(eplGetUDPFlowsMonitor);
		statementGetUDPFlowsMonitor.addListener(new UdpFlowMonitorListener());

		//send processed output to kafka
		String eplTcpConnectionTrigger = "select * from TcpConnection";
		EPStatement statementTcpConnectionTrigger = epService.getEPAdministrator().createEPL(eplTcpConnectionTrigger);
		statementTcpConnectionTrigger.addListener(new TcpConnectionTrigger(producer));
		
		String eplUdpConnectionTrigger = "select * from UdpConnection";
		EPStatement statementUdpConnectionTrigger = epService.getEPAdministrator().createEPL(eplUdpConnectionTrigger);
		statementUdpConnectionTrigger.addListener(new UdpConnectionTrigger(producer));
		
		//start KafkaConsumer with prepared EPService 
		KafkaConsumerMaster consumerMaster = new KafkaConsumerMaster(epService).startWorkers(configs);
		
		//give overview about started threads
		ThreadUtil.printThreads();
		//manage graceful threads shutdown
		ThreadUtil.manageShutdown(consumerMaster, producer, epService);

	}

}
