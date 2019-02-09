package de.e2security.netflow_flowaggregation.esper.epl;

public final class CommonEplExpressions {
	
	public static String tcpSortByLastSwitched() {
		/**
		 * Using time observer instead of time_order in order to sort by both parameters (first_switched in case of last_switched equality)
		 * In case of that the window takes ALL events coming independently of its timestamp and sort them within 60 seconds for tcp or 120 seconds for udp
		 * As time do not evaluate event's timestamp, the additional check to be implemented within patterns such as 
		 * 	and last_switched.toMillisec() - a.last_switched.toMillisec() <= 60000 for tcp
		 *  and last_switched.toMillisec() - a.last_switched.toMillisec() <= 120000 for udp
		 * even though such long delay is hardly to await in reality 
		 */
		
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
	
	public static String udpSortByLastSwitched() {
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
				+ " from NetflowEvent#time(120 sec) where protocol=17 order by last_switched, first_switched";
	}
	
	@Deprecated
	public static abstract class NetflowEventEplSupporter {
		
		@Deprecated
		static String connectionXReferenceChecker() {
			return    " ipv4_src_addr = a.ipv4_dst_addr"
					+ " and l4_src_port   = a.l4_dst_port"
					+ " and ipv4_dst_addr = a.ipv4_src_addr"
					+ " and l4_dst_port   = a.l4_src_port";
		}
		
		@Deprecated
		static String rightOrderChecker() {
			return "first_switched_as_long >= a.first_switched_as_long";
		}
		
		@Deprecated
		static String timeIntervalLastSwitchedChecker() {
			return "last_switched.toMillisec() - a.last_switched.toMillisec() <= 60000 ";
		}
		
		
		@Deprecated
		static String fields() {
			return ",a.receivedTimeStamp as in_receivedTimeStamp"
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
					+ ",b.last_switched as out_last_switched";
		}

	}
	
}
