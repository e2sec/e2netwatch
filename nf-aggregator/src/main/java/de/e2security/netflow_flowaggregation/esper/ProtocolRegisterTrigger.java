package de.e2security.netflow_flowaggregation.esper;

import static de.e2security.netflow_flowaggregation.utils.Ecs.Schema.Netflow.HOST;
import static de.e2security.netflow_flowaggregation.utils.Ecs.Schema.Netflow.IN_BYTES;
import static de.e2security.netflow_flowaggregation.utils.Ecs.Schema.Netflow.IN_FIRST_SWITCHED;
import static de.e2security.netflow_flowaggregation.utils.Ecs.Schema.Netflow.IN_FLOW_RECORDS;
import static de.e2security.netflow_flowaggregation.utils.Ecs.Schema.Netflow.IN_FLOW_SEQ_NUM;
import static de.e2security.netflow_flowaggregation.utils.Ecs.Schema.Netflow.IN_LAST_SWITCHED;
import static de.e2security.netflow_flowaggregation.utils.Ecs.Schema.Netflow.IN_PKTS;
import static de.e2security.netflow_flowaggregation.utils.Ecs.Schema.Netflow.IPV4_DST_ADDR;
import static de.e2security.netflow_flowaggregation.utils.Ecs.Schema.Netflow.IPV4_SRC_ADDR;
import static de.e2security.netflow_flowaggregation.utils.Ecs.Schema.Netflow.L4_DST_PORT;
import static de.e2security.netflow_flowaggregation.utils.Ecs.Schema.Netflow.L4_SRC_PORT;
import static de.e2security.netflow_flowaggregation.utils.Ecs.Schema.Netflow.OUT_BYTES;
import static de.e2security.netflow_flowaggregation.utils.Ecs.Schema.Netflow.OUT_FIRST_SWITCHED;
import static de.e2security.netflow_flowaggregation.utils.Ecs.Schema.Netflow.OUT_FLOW_RECORDS;
import static de.e2security.netflow_flowaggregation.utils.Ecs.Schema.Netflow.OUT_FLOW_SEQ_NUM;
import static de.e2security.netflow_flowaggregation.utils.Ecs.Schema.Netflow.OUT_LAST_SWITCHED;
import static de.e2security.netflow_flowaggregation.utils.Ecs.Schema.Netflow.OUT_PKTS;
import static de.e2security.netflow_flowaggregation.utils.Ecs.Schema.Netflow.PROTOCOL;

import java.io.Serializable;
import java.time.ZonedDateTime;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

import de.e2security.netflow_flowaggregation.kafka.CustomKafkaProducer;
import de.e2security.netflow_flowaggregation.model.protocols.ProtocolRegister;

public class ProtocolRegisterTrigger implements UpdateListener {
	private static final Logger LOG = LoggerFactory.getLogger(ProtocolRegisterTrigger.class);
	private final CustomKafkaProducer<Serializable, Serializable> producer;

	public ProtocolRegisterTrigger(CustomKafkaProducer<Serializable, Serializable> producer) {
		this.producer = producer;
	}
	
	public static class Host {
		private String addr;
		private Integer port;
		Host(String addr, Integer port) {
			this.addr = addr;
			this.port = port;
		}
		public String getAddr() { return this.addr; }
		public Integer getPort() { return this.port; }
	}

	//NW-76 distinguish traffic direction by port numbers.
	//higher port = source host; lower port = dst hosts
	public Pair<Host,Host> defineTrafficDirectionByPortOnEqualFirstSwitched(ProtocolRegister prtcl) {
		String srcaddr = prtcl.getIn_ipv4_src_addr();
		Integer srcport = prtcl.getIn_l4_src_port();
		String dstaddr = prtcl.getOut_ipv4_src_addr();
		Integer dstport = prtcl.getOut_14_src_port();
		
		Host src = new Host(srcaddr, srcport);
		Host dst = new Host(dstaddr, dstport);
		
		if (prtcl.getIn_first_switched().compareTo(prtcl.getOut_first_switched()) == 0) {
			if (srcport < dstport) {
				dst = new Host(srcaddr,srcport);
				src = new Host(dstaddr,dstport);
			} 
		}

		return new ImmutablePair<>(src, dst);
	}

	//setting method in order to make it possible for test
	public JSONObject prepareJson(ProtocolRegister prtcl) {
		JSONObject jsonObject = new JSONObject();
		Pair<Host,Host> pair = defineTrafficDirectionByPortOnEqualFirstSwitched(prtcl);

		String srcaddr = pair.getLeft().getAddr();
		Integer srcport = pair.getLeft().getPort();
		String dstaddr = pair.getRight().getAddr();
		Integer dstport = pair.getRight().getPort();	

		String description = prtcl.getDescription();
		
		String host = prtcl.getIn_host();
		Integer protocol = prtcl.getProtocol();
		Integer in_flow_seq_num = prtcl.getIn_flow_seq_num();
		Integer in_flow_records = prtcl.getIn_flow_records();
		Integer out_flow_seq_num = prtcl.getOut_flow_seq_num();
		Integer out_flow_records = prtcl.getOut_flow_records();
		Integer in_bytes = prtcl.getIn_bytes();
		Integer out_bytes = prtcl.getOut_bytes();
		Integer in_pkts = prtcl.getIn_pkts();
		Integer out_pkts = prtcl.getOut_pkts();
		ZonedDateTime in_last_switched = prtcl.getIn_last_switched();
		ZonedDateTime out_last_switched = prtcl.getOut_last_switched();
		ZonedDateTime in_first_switched = prtcl.getIn_first_switched();
		ZonedDateTime out_first_switched = prtcl.getOut_first_switched();

		LOG.info(String.format("%s Connection %s:%d -> %s:%d (%d/%d Bytes)", 
				description, srcaddr, srcport, dstaddr, dstport, in_bytes, out_bytes));
		//KEY names are compatible to ECS SCHEMA. ecs.version 0.1.0
		jsonObject.put("description", description);
		jsonObject.put(HOST.asEcs(), host);
		jsonObject.put(IPV4_SRC_ADDR.asEcs(), srcaddr);
		jsonObject.put(L4_SRC_PORT.asEcs(), srcport);
		jsonObject.put(IPV4_DST_ADDR.asEcs(), dstaddr);
		jsonObject.put(L4_DST_PORT.asEcs(), dstport);
		jsonObject.put(PROTOCOL.asEcs(), protocol);
		jsonObject.put(IN_FLOW_SEQ_NUM.asEcs(), in_flow_seq_num);
		jsonObject.put(IN_FLOW_RECORDS.asEcs(), in_flow_records);
		jsonObject.put(OUT_FLOW_SEQ_NUM.asEcs(), out_flow_seq_num);
		jsonObject.put(OUT_FLOW_RECORDS.asEcs(), out_flow_records);
		jsonObject.put(IN_BYTES.asEcs(), in_bytes);
		jsonObject.put(OUT_BYTES.asEcs(), out_bytes);
		jsonObject.put(IN_PKTS.asEcs(), in_pkts);
		jsonObject.put(OUT_PKTS.asEcs(), out_pkts);
		jsonObject.put(IN_FIRST_SWITCHED.asEcs(), in_first_switched);
		jsonObject.put(OUT_FIRST_SWITCHED.asEcs(), out_first_switched);
		jsonObject.put(IN_LAST_SWITCHED.asEcs(), in_last_switched);
		jsonObject.put(OUT_LAST_SWITCHED.asEcs(), out_last_switched);

		return jsonObject;
	}

	@Override
	public void update(EventBean[] newData, EventBean[] oldEvents) {
		try {
			ProtocolRegister prtcl = (ProtocolRegister) newData[0].getUnderlying();
			JSONObject jsonObject = prepareJson(prtcl);
			producer.send(jsonObject.toString());		
		} catch (ClassCastException ex) {
			LOG.error("the underlying object is instance of {}", newData[0]);
		}
	}

}
