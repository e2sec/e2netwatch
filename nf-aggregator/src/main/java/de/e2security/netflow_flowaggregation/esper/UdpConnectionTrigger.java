package de.e2security.netflow_flowaggregation.esper;

import java.io.Serializable;
import java.time.ZonedDateTime;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

import de.e2security.netflow_flowaggregation.kafka.CustomKafkaProducer;

public class UdpConnectionTrigger implements UpdateListener {

	private static final Logger LOG = LoggerFactory.getLogger(UdpConnectionTrigger.class);
	private final CustomKafkaProducer<Serializable, Serializable> producer;
	
	public UdpConnectionTrigger(CustomKafkaProducer<Serializable, Serializable> producer) {
		this.producer = producer;
	}
	
	@Override
	public void update(EventBean[] newData, EventBean[] oldEvents) {
		String description = (String) newData[0].get("description");
		String host = (String) newData[0].get("host");
		String srcaddr = (String) newData[0].get("ipv4_src_addr");
		Integer srcport = (Integer) newData[0].get("l4_src_port");
		String dstaddr = (String) newData[0].get("ipv4_dst_addr");
		Integer dstport = (Integer) newData[0].get("l4_dst_port");
		Integer protocol = (Integer) newData[0].get("protocol");
		Integer in_flow_seq_num = (Integer) newData[0].get("in_flow_seq_num");
		Integer in_flow_records = (Integer) newData[0].get("in_flow_records");
		Integer out_flow_seq_num = (Integer) newData[0].get("out_flow_seq_num");
		Integer out_flow_records = (Integer) newData[0].get("out_flow_records");
		Integer in_bytes = (Integer) newData[0].get("in_bytes");
		Integer out_bytes = (Integer) newData[0].get("out_bytes");
		Integer in_pkts = (Integer) newData[0].get("in_pkts");
		Integer out_pkts = (Integer) newData[0].get("out_pkts");
		ZonedDateTime in_first_switched = (ZonedDateTime) newData[0].get("in_first_switched");
		ZonedDateTime out_first_switched = (ZonedDateTime) newData[0].get("out_first_switched");
		ZonedDateTime in_last_switched = (ZonedDateTime) newData[0].get("in_last_switched");
		ZonedDateTime out_last_switched = (ZonedDateTime) newData[0].get("out_last_switched");
		LOG.info(String.format("%s Connection %s:%d -> %s:%d (%d/%d Bytes)", description, srcaddr, srcport, dstaddr,
				dstport,
				in_bytes, out_bytes));
		JSONObject jsonObject = new JSONObject();
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
		producer.send(jsonObject.toString());		
	}

}
