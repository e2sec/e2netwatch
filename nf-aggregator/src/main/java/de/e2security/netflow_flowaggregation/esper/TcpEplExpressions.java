package de.e2security.netflow_flowaggregation.esper;

public final class TcpEplExpressions implements EplExpression {
	
	// @formatter:off
	/*
	 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	 * ! -- CAUTION --
	 * ! inserted fields need to match constructor arguments
	 * ! in type and order
	 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	 */
	
	private static String tcpFields() {
		return ",a.receivedTimeStamp as in_receivedTimeStamp"
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
	}
	
	/**
	 * Get events into correct order as we cannot rely on the correct order of netflow data,
	 * @see http://esper.espertech.com/release-5.5.0/esper-reference/html/epl-views.html#view-time-order
	 */
	public static String eplSortByLastSwitched() {
		return "insert rstream into NetflowEventOrdered"
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
	}
	
	/**
	 * Finished TCP Flow: FIN flag (1) set on both flows 
	 * @return EPL Expression 
	 */
	public static String eplFinishedFlows() {
		return "insert into TcpConnection select"
				+ " 'Finished TCP' as description"
				+ tcpFields()
				+ " from pattern [every a=NetflowEventOrdered(protocol=6 and (tcp_flags&1)=1) ->"
				+ " b=NetflowEventOrdered(protocol=6 and (tcp_flags&1)=1 and host=a.host "
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
	 * @param pattern pattern to be used
	 * @return EPL Expression
	 */
	public static String eplRejectedFlows(String pattern) {
		return "insert into TcpConnection select"
				+ " 'Rejected TCP' as description"
				+ tcpFields()
				+ " from pattern " + pattern
				+ " and ipv4_src_addr = a.ipv4_dst_addr"
				+ " and l4_src_port   = a.l4_dst_port"
				+ " and ipv4_dst_addr = a.ipv4_src_addr"
				+ " and l4_dst_port   = a.l4_src_port)"
				+ " where timer:within(60 sec)]";
	}
}
