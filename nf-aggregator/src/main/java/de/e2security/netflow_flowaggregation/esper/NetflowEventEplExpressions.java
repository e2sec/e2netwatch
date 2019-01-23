package de.e2security.netflow_flowaggregation.esper;

public final class NetflowEventEplExpressions {
	/**
	 * Get events into correct order
	 * @see http://esper.espertech.com/release-5.5.0/esper-reference/html/epl-views.html#view-time-order
	 */
	
	/* 
	 * From Esper Reference 7.1 to (ext:)time_order:
	 * Note the statement above uses the rstream keyword in both the  insert into clause and the select clause to select ordered events only. 
	 * It uses the insert into clause to makes such ordered stream available for subsequent statements to use.
	 */
	public static String eplSortByLastSwitched() {
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
				+ " from NetflowEvent#time(60 sec) order by last_switched, first_switched";
	}
	
	public static abstract class NetflowEventEplSupporter {
		static String connectionXReferenceChecker() {
			return    " ipv4_src_addr = a.ipv4_dst_addr"
					+ " and l4_src_port   = a.l4_dst_port"
					+ " and ipv4_dst_addr = a.ipv4_src_addr"
					+ " and l4_dst_port   = a.l4_src_port";
		}
		
		static String rightOrderChecker() {
			return "first_switched_as_long >= a.first_switched_as_long";
		}
		
		// @formatter:off
		/*
		 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		 * ! -- CAUTION --
		 * ! inserted fields need to match constructor arguments
		 * ! in type and order
		 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		 */
		
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
