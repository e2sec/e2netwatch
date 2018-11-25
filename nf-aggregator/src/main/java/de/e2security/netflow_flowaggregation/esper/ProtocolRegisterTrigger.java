package de.e2security.netflow_flowaggregation.esper;

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
			jsonObject.put("description", description);
			jsonObject.put("host", host);
			jsonObject.put("ipv4_src_addr", srcaddr);
			jsonObject.put("l4_src_port", srcport);
			jsonObject.put("ipv4_dst_addr", dstaddr);
			jsonObject.put("l4_dst_port", dstport);
			jsonObject.put("protocol", protocol);
			jsonObject.put("in_flow_seq_num", in_flow_seq_num);
			jsonObject.put("in_flow_records", in_flow_records);
			jsonObject.put("out_flow_seq_num", out_flow_seq_num);
			jsonObject.put("out_flow_records", out_flow_records);
			jsonObject.put("in_bytes", in_bytes);
			jsonObject.put("out_bytes", out_bytes);
			jsonObject.put("in_pkts", in_pkts);
			jsonObject.put("out_pkts", out_pkts);
			jsonObject.put("in_first_switched", in_first_switched);
			jsonObject.put("out_first_switched", out_first_switched);
			jsonObject.put("in_last_switched", in_last_switched);
			jsonObject.put("out_last_switched", out_last_switched);
		} catch (ClassCastException ex) {
			LOG.error("the underlying object is instance of {}", newData[0]);
		}
		producer.send(jsonObject.toString());		
	}

}
