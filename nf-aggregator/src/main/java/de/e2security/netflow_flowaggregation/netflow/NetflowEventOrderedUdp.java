package de.e2security.netflow_flowaggregation.netflow;

import java.time.ZonedDateTime;

public class NetflowEventOrderedUdp extends NetflowEventOrdered {
	private static final long serialVersionUID = 1L;

	public NetflowEventOrderedUdp(ZonedDateTime receivedTimeStamp, String host, String ipv4_src_addr,
			String ipv4_dst_addr, Integer l4_src_port, Integer l4_dst_port, Integer tcp_flags, Integer protocol,
			Integer version, Integer flow_seq_num, Integer flow_records, Integer in_bytes, Integer in_pkts,
			ZonedDateTime first_switched, ZonedDateTime last_switched) throws NetflowEventException {
		super(receivedTimeStamp, host, ipv4_src_addr, ipv4_dst_addr, l4_src_port, l4_dst_port, tcp_flags, protocol, version,
				flow_seq_num, flow_records, in_bytes, in_pkts, first_switched, last_switched);
	}




}