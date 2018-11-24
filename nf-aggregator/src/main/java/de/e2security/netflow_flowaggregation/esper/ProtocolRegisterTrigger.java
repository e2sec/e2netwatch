package de.e2security.netflow_flowaggregation.esper;

import static de.e2security.netflow_flowaggregation.utils.Ecs.Schema.Netflow.*;

import java.io.Serializable;
import java.time.ZonedDateTime;

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

	@Override
	public void update(EventBean[] newData, EventBean[] oldEvents) {
		JSONObject jsonObject = new JSONObject();
		try {
			ProtocolRegister proto = (ProtocolRegister) newData[0].getUnderlying();
			String description = proto.getDescription();
			String host = proto.getIn_host();
			String srcaddr = proto.getIn_ipv4_dst_addr();
			Integer srcport = proto.getIn_l4_src_port();
			String dstaddr = proto.getIn_ipv4_dst_addr();
			Integer dstport = proto.getIn_l4_dst_port();
			Integer protocol = proto.getProtocol();
			Integer in_flow_seq_num = proto.getIn_flow_seq_num();
			Integer in_flow_records = proto.getIn_flow_records();
			Integer out_flow_seq_num = proto.getOut_flow_seq_num();
			Integer out_flow_records = proto.getOut_flow_records();
			Integer in_bytes = proto.getIn_bytes();
			Integer out_bytes = proto.getOut_bytes();
			Integer in_pkts = proto.getIn_pkts();
			Integer out_pkts = proto.getOut_pkts();
			ZonedDateTime in_first_switched = proto.getIn_first_switched();
			ZonedDateTime out_first_switched = proto.getOut_first_switched();
			ZonedDateTime in_last_switched = proto.getIn_last_switched();
			ZonedDateTime out_last_switched = proto.getOut_last_switched();
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
		} catch (ClassCastException ex) {
			LOG.error("the underlying object is instance of {}", newData[0]);
		}
		producer.send(jsonObject.toString());		
	}

}
