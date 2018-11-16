package de.e2security.netflow_flowaggregation.esper;

public final class UdpEplExpressions {
	
	private static String udpFields() {
		return ",a.receivedTimeStamp as in_receivedTimeStamp"
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
	}
	
	/**
	 * Finished UDP connection
	 * no further packets in both directions after 2 minutes
	 * @formatter:on
	 */
	public static String eplFinishedUDPFlows() {
		return "insert into UdpConnection select"
				+ " 'Finished UDP' as description"
				+ udpFields()
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
	}
}
