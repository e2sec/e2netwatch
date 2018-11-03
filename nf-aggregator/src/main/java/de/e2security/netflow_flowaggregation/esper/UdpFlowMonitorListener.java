package de.e2security.netflow_flowaggregation.esper;

import java.time.ZonedDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

public class UdpFlowMonitorListener implements UpdateListener {

	private static final Logger LOG = LoggerFactory.getLogger(UdpFlowMonitorListener.class);

	@Override
	public void update(EventBean[] newData, EventBean[] oldEvents) {
		Integer in_bytes = (Integer) newData[0].get("in_bytes");
		String srcaddr = (String) newData[0].get("ipv4_src_addr");
		Integer srcport = (Integer) newData[0].get("l4_src_port");
		String dstaddr = (String) newData[0].get("ipv4_dst_addr");
		Integer dstport = (Integer) newData[0].get("l4_dst_port");
		ZonedDateTime first_switched = (ZonedDateTime) newData[0].get("first_switched");
		LOG.info(String.format("UDP %s:%d -> %s:%d (%d Bytes) %s", srcaddr, srcport, dstaddr, dstport, in_bytes,
				first_switched.toString()));
	}

}
