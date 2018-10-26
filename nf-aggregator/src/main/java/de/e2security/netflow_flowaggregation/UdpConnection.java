package de.e2security.netflow_flowaggregation;

import java.io.Serializable;
import java.time.ZonedDateTime;

public class UdpConnection implements Serializable {
	private static final long serialVersionUID = 1L;
	// private static final Logger LOG = LoggerFactory.getLogger(App.class);

	private String description;
	private ZonedDateTime in_receivedTimeStamp;
	private ZonedDateTime out_receivedTimeStamp;
	private String host;
	private String ipv4_src_addr;
	private String ipv4_dst_addr;
	private Integer l4_src_port;
	private Integer l4_dst_port;
	private Integer protocol;
	private Integer in_flow_seq_num;
	private Integer in_flow_records;
	private Integer out_flow_seq_num;
	private Integer out_flow_records;
	private Integer in_bytes;
	private Integer in_pkts;
	private Integer out_bytes;
	private Integer out_pkts;
	private ZonedDateTime in_first_switched;
	private ZonedDateTime in_last_switched;
	private ZonedDateTime out_first_switched;
	private ZonedDateTime out_last_switched;

	public UdpConnection(String description,
			ZonedDateTime in_receivedTimeStamp,
			ZonedDateTime out_receivedTimeStamp,
			String host,
			String ipv4_src_addr,
			String ipv4_dst_addr,
			Integer l4_src_port,
			Integer l4_dst_port,
			Integer protocol,
			Integer in_flow_seq_num,
			Integer out_flow_seq_num,
			Integer in_flow_records,
			Integer out_flow_records,
			Integer in_bytes,
			Integer out_bytes,
			Integer in_pkts,
			Integer out_pkts,
			ZonedDateTime in_first_switched,
			ZonedDateTime out_first_switched,
			ZonedDateTime in_last_switched,
			ZonedDateTime out_last_switched) throws TcpConnectionException {
		this.description = description;
		this.in_receivedTimeStamp = in_receivedTimeStamp;
		this.out_receivedTimeStamp = out_receivedTimeStamp;
		this.host = host;
		this.ipv4_src_addr = ipv4_src_addr;
		this.ipv4_dst_addr = ipv4_dst_addr;
		this.l4_src_port = l4_src_port;
		this.l4_dst_port = l4_dst_port;
		this.protocol = protocol;
		this.in_flow_seq_num = in_flow_seq_num;
		this.in_flow_records = in_flow_records;
		this.out_flow_seq_num = out_flow_seq_num;
		this.out_flow_records = out_flow_records;
		this.in_bytes = in_bytes;
		this.in_pkts = in_pkts;
		this.out_bytes = out_bytes;
		this.out_pkts = out_pkts;
		this.in_first_switched = in_first_switched;
		this.in_last_switched = in_last_switched;
		this.out_first_switched = out_first_switched;
		this.out_last_switched = out_last_switched;
	}

	public String getDescription() {
		return description;
	}

	public ZonedDateTime getIn_ReceivedTimeStamp() {
		return in_receivedTimeStamp;
	}

	public ZonedDateTime getOut_ReceivedTimeStamp() {
		return out_receivedTimeStamp;
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

	public Integer getProtocol() {
		return protocol;
	}

	public Integer getIn_flow_seq_num() {
		return in_flow_seq_num;
	}

	public Integer getOut_flow_seq_num() {
		return out_flow_seq_num;
	}

	public Integer getIn_flow_records() {
		return in_flow_records;
	}

	public Integer getOut_flow_records() {
		return out_flow_records;
	}

	public Integer getIn_bytes() {
		return in_bytes;
	}

	public Integer getIn_pkts() {
		return in_pkts;
	}

	public Integer getOut_bytes() {
		return out_bytes;
	}

	public Integer getOut_pkts() {
		return out_pkts;
	}

	public ZonedDateTime getIn_first_switched() {
		return in_first_switched;
	}

	public ZonedDateTime getIn_last_switched() {
		return in_last_switched;
	}

	public ZonedDateTime getOut_first_switched() {
		return out_first_switched;
	}

	public ZonedDateTime getOut_last_switched() {
		return out_last_switched;
	}
}
