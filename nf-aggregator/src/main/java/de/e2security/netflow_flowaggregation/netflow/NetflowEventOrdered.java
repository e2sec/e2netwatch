package de.e2security.netflow_flowaggregation.netflow;

import java.io.Serializable;
import java.time.ZonedDateTime;

import de.e2security.netflow_flowaggregation.exceptions.NetflowEventException;

public class NetflowEventOrdered implements Serializable {
	private static final long serialVersionUID = 1L;	

	private ZonedDateTime receivedTimeStamp;
	private String host;
	private String ipv4_src_addr;
	private String ipv4_dst_addr;
	private Integer l4_src_port;
	private Integer l4_dst_port;
	private Integer tcp_flags;
	private Integer protocol;
	private Integer version;
	private Integer flow_seq_num;
	private Integer flow_records;
	private Integer in_bytes;
	private Integer in_pkts;
	private ZonedDateTime first_switched;
	private ZonedDateTime last_switched;

	public NetflowEventOrdered(
			ZonedDateTime receivedTimeStamp,
			String host,
			String ipv4_src_addr,
			String ipv4_dst_addr,
			Integer l4_src_port,
			Integer l4_dst_port,
			Integer tcp_flags,
			Integer protocol,
			Integer version,
			Integer flow_seq_num,
			Integer flow_records,
			Integer in_bytes,
			Integer in_pkts,
			ZonedDateTime first_switched,
			ZonedDateTime last_switched) throws NetflowEventException {
		this.receivedTimeStamp = receivedTimeStamp;
		this.host = host;
		this.ipv4_src_addr = ipv4_src_addr;
		this.ipv4_dst_addr = ipv4_dst_addr;
		this.l4_src_port = l4_src_port;
		this.l4_dst_port = l4_dst_port;
		this.tcp_flags = tcp_flags;
		this.protocol = protocol;
		this.version = version;
		this.flow_seq_num = flow_seq_num;
		this.flow_records = flow_records;
		this.in_bytes = in_bytes;
		this.in_pkts = in_pkts;
		this.first_switched = first_switched;
		this.last_switched = last_switched;
	}

	public ZonedDateTime getReceivedTimeStamp() {
		return receivedTimeStamp;
	}

	public String getHost() {
		return host;
	}

	public String getIpv4_src_addr() {
		return ipv4_src_addr;
	}

	public String getIpv4_dst_addr() {
		return ipv4_dst_addr;
	}

	public Integer getL4_src_port() {
		return l4_src_port;
	}

	public Integer getL4_dst_port() {
		return l4_dst_port;
	}

	public Integer getTcp_flags() {
		return tcp_flags;
	}

	public Integer getProtocol() {
		return protocol;
	}

	public Integer getVersion() {
		return version;
	}

	public Integer getFlow_seq_num() {
		return flow_seq_num;
	}

	public Integer getFlow_records() {
		return flow_records;
	}

	public Integer getIn_bytes() {
		return in_bytes;
	}

	public Integer getIn_pkts() {
		return in_pkts;
	}

	public ZonedDateTime getFirst_switched() {
		return first_switched;
	}

	public ZonedDateTime getLast_switched() {
		return last_switched;
	}
}
