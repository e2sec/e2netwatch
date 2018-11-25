package de.e2security.netflow_flowaggregation.model.protocols;

import java.io.Serializable;
import java.time.ZonedDateTime;

import de.e2security.netflow_flowaggregation.exceptions.ProtocolRegisterException;

public class ProtocolRegister implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String description;
	private ZonedDateTime in_receivedTimeStamp;
	private ZonedDateTime out_receivedTimeStamp;
	private String in_host;
	private String out_host;
	private String in_ipv4_src_addr;
	private String in_ipv4_dst_addr;
	private String out_ipv4_src_addr;
	private String out_ipv4_dst_addr;
	private Integer in_l4_src_port;
	private Integer in_l4_dst_port;
	private Integer out_14_src_port;
	private Integer out_14_dst_port;
	private Integer in_tcp_flags;
	private Integer out_tcp_flags;
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

	public ProtocolRegister(String description,
			ZonedDateTime in_receivedTimeStamp,
			ZonedDateTime out_receivedTimeStamp,
			String in_host,
			String out_host,
			String in_ipv4_src_addr,
			String in_ipv4_dst_addr,
			String out_ipv4_src_addr,
			String out_ipv4_dst_addr,
			Integer in_l4_src_port,
			Integer in_l4_dst_port,
			Integer out_14_src_port,
			Integer out_14_dst_port,
			Integer in_tcp_flags,
			Integer out_tcp_flags,
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
			ZonedDateTime out_last_switched) throws ProtocolRegisterException {
		this.description = description;
		this.in_receivedTimeStamp = in_receivedTimeStamp;
		this.out_receivedTimeStamp = out_receivedTimeStamp;
		this.in_host = in_host;
		this.out_host = out_host;
		this.in_ipv4_src_addr = in_ipv4_src_addr;
		this.in_ipv4_dst_addr = in_ipv4_dst_addr;
		this.out_ipv4_src_addr = out_ipv4_src_addr;
		this.out_ipv4_dst_addr = out_ipv4_dst_addr;
		this.in_l4_src_port = in_l4_src_port;
		this.in_l4_dst_port = in_l4_dst_port;
		this.out_14_src_port = out_14_src_port;
		this.out_14_dst_port = out_14_dst_port;
		this.in_tcp_flags = in_tcp_flags;
		this.out_tcp_flags = out_tcp_flags;
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

	public String getIn_host() {
		return in_host;
	}

	public String getOut_host() {
		return out_host;
	}

	public String getIn_ipv4_src_addr() {
		return in_ipv4_src_addr;
	}

	public String getIn_ipv4_dst_addr() {
		return in_ipv4_dst_addr;
	}
	
	public String getOut_ipv4_src_addr() {
		return out_ipv4_src_addr;
	}

	public String getOut_ipv4_dst_addr() {
		return out_ipv4_dst_addr;
	}

	public Integer getIn_l4_src_port() {
		return in_l4_src_port;
	}

	public Integer getIn_l4_dst_port() {
		return in_l4_dst_port;
	}
	
	public Integer getOut_14_src_port() {
		return out_14_src_port;
	}
	
	public Integer getOut_14_dst_port() {
		return out_14_dst_port;
	}

	public Integer getIn_tcp_flags() {
		return in_tcp_flags;
	}

	public Integer getOut_tcp_flags() {
		return out_tcp_flags;
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

	@Override
	public String toString() {
		return description + ":\n" 
				+ "in_ipv4_src_addr=" + in_ipv4_src_addr + " - " + in_ipv4_dst_addr + "=in_ipv4_dst_addr \n"
				+ "out_ipv4_src_addr=" + out_ipv4_src_addr + " - " + out_ipv4_dst_addr + "=out_ipv4_dst_addr \n"  
				+ "in_l4_src_port=" + in_l4_src_port + " - " + in_l4_dst_port + "=in_l4_dst_port \n" 
				+ "out_14_src_port=" + out_14_src_port + " - " + out_14_dst_port + "=out_14_dst_port \n"  
				+ "in_tcp_flags=" + in_tcp_flags + " - " + out_tcp_flags + "=out_tcp_flags \n" 
				+ "out_last_switched=" + out_last_switched + " - " + in_last_switched + "=in_last_switched \n";
	}

	
}
