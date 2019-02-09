package de.e2security.netflow_flowaggregation.esper.epl;

public final class TcpEplExpressions {

	// @formatter:off
		/*
		 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		 * ! -- CAUTION --
		 * ! inserted fields need to match constructor arguments
		 * ! in type and order
		 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		 */

	/**
	 * Finished TCP Flow: FIN flag (1) set on both flows 
	 * @param protocolFields can be either Tcp or Udp fields
	 * @return EPL Expression 
	 */
	
	public static String eplFinishedFlows() {
		return "insert into ProtocolRegister select"
				//fields to be retrieved into ProtocolRegister
				+ " 'Finished TCP' as description"
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
				+ " from pattern [every a=NetflowEventOrdered(protocol=6 and (tcp_flags&1)=1) ->"
				+ " b=NetflowEventOrdered ("
				+ " protocol=6 and (tcp_flags&1)=1 and host=a.host"
				// time window check
				+ " and last_switched.toMillisec() - a.last_switched.toMillisec() <= 60000"
				//right order check
				+ " and a.first_switched.before(first_switched)"
				//cross reference check
				+ " and ipv4_src_addr = a.ipv4_dst_addr"
				+ " and l4_src_port   = a.l4_dst_port"
				+ " and ipv4_dst_addr = a.ipv4_src_addr"
				+ " and l4_dst_port   = a.l4_src_port)" 
				+ " where timer:within(60 sec)]";
	}

	/**
	 * Rejected TCP connection
	 * first flow: SYN (2) set, but ACK (16) not set
	 * second flow: RST (4) set
	 * As we cannot rely on the correct order of netflow data,
	 * so we define two EPL statements 
	 * @param protocolFields can be either Tcp or Udp fields
	 * @return EPL Expression
	 */
	public static String eplRejectedFlows(String pattern) {
		return "insert into ProtocolRegister select"
				+ " 'Rejected TCP' as description"
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
				+ " from pattern " + pattern
				// check due to order by within eplSortByLastSwitched() instead of ext.time_order
				+ " and last_switched.toMillisec() - a.last_switched.toMillisec() <= 60000"
				//right order check
				+ " and a.first_switched.before(first_switched)"
				// cross reference checker
				+ " and ipv4_src_addr = a.ipv4_dst_addr"
				+ " and l4_src_port   = a.l4_dst_port"
				+ " and ipv4_dst_addr = a.ipv4_src_addr"
				+ " and l4_dst_port   = a.l4_src_port)"
				+ " where timer:within(60 sec)]";
	}

	public static String eplRejectedPatternSyn2Ack16() {
		return "[every a=NetflowEventOrdered(protocol=6 and (tcp_flags&2)=2 and (tcp_flags&16)=0) -> "
				+ " b=NetflowEventOrdered(protocol=6 and (tcp_flags&4)=4 and host=a.host ";
	}

	public static String eplRejectedPatternRst4() {
		return "[every a=NetflowEventOrdered(protocol=6 and (tcp_flags&4)=4) ->"
				+ " b=NetflowEventOrdered(protocol=6 and (tcp_flags&2)=2 and (tcp_flags&16)=0 and host=a.host ";
	}

	/**
	 * Using time observer instead of time_order in order to sort by both parameters (first_switched in case of last_switched equality)
	 * In case of that the window takes ALL events coming independently of its timestamp and sort them within 60 seconds for tcp or 120 seconds for udp
	 * As time do not evaluate event's timestamp, the additional check to be implemented within patterns such as 
	 * 	and last_switched.toMillisec() - a.last_switched.toMillisec() <= 60000 for tcp
	 *  and last_switched.toMillisec() - a.last_switched.toMillisec() <= 120000 for udp
	 * even though such long delay is hardly to await in reality 
	 */
	
	public static String tcpSortByLastSwitched() {
		
		return "insert rstream into NetflowEventOrdered"
				+ " select rstream receivedTimeStamp"
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
				+ " from NetflowEvent#time(60 sec) where protocol=6 order by last_switched, first_switched"; 
	}
}
