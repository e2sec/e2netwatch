package de.e2security.netflow_flowaggregation.esper.epl;

public final class UdpEplExpressions {
	
	/**
	 * Finished UDP connection
	 * no further packets in both directions after 2 minutes
	 * @formatter:on
	 */
	//TODO: tests with real netflow data
	public static String eplFinishedUDPFlows() {
		return "insert into ProtocolRegister select"
				+ " 'Finished UDP' as description"
				+ ",a.receivedTimeStamp as in_receivedTimeStamp"
				+ ",b.receivedTimeStamp as out_receivedTimeStamp"
				+ ",a.host as in_host"
				+ ",b.host as out_host"
				+ ",a.ipv4_src_addr as in_ipv4_src_addr"
				+ ",a.ipv4_dst_addr as in_ipv4_dst_addr"
				+ ",b.ipv4_src_addr as out_ipv4_src_addr"
				+ ",b.ipv4_dst_addr as out_ipv4_dst_addr"
				+ ",a.l4_src_port as in_l4_src_port"
				+ ",a.l4_dst_port as in_l4_dst_port"
				+ ",b.l4_src_port as out_l4_src_port"
				+ ",b.l4_dst_port as out_l4_dst_port"
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
				+ ",b.last_switched as out_last_switched"
				+ " from pattern [" 
				+ 			"every a=NetflowEventOrdered(protocol=17) ->"
				+ 			" b=NetflowEventOrdered(protocol=17 and host=a.host"
				+ 			" and ipv4_src_addr = a.ipv4_dst_addr"
				+ 			" and l4_src_port   = a.l4_dst_port"
				+ 			" and ipv4_dst_addr = a.ipv4_src_addr"
				+ 			" and l4_dst_port   = a.l4_src_port" + ")"
				+			"->"
				+ 			" timer:interval(120 sec)"
				+ 			" and not "
				+ 			" c=NetflowEventOrdered(protocol=17 and host=a.host"
				+			" and ipv4_src_addr = a.ipv4_src_addr"
				+ 			" and l4_src_port   = a.l4_src_port"
				+	 		" and ipv4_dst_addr = a.ipv4_dst_addr"
				+	 		" and l4_dst_port   = a.l4_dst_port" + ")"
				+			" and not "
				+ 			" d=NetflowEventOrdered(protocol=17 and host=a.host "
				+			" and ipv4_src_addr = b.ipv4_src_addr"
				+ 			" and l4_src_port   = b.l4_src_port"
				+ 			" and ipv4_dst_addr = b.ipv4_dst_addr"
				+ 			" and l4_dst_port   = b.l4_dst_port" + ")"
				+ "]";
	}
}
