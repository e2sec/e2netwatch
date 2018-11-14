package de.e2security.netflow_flowaggregation.esper;

import java.time.ZonedDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

import de.e2security.netflow_flowaggregation.model.protocols.NetflowEvent;

public class ProtocolRegisterListener implements UpdateListener {

	private static final Logger LOG = LoggerFactory.getLogger(ProtocolRegisterListener.class);
	private String format = "%s %s:%d -> %s:%d (%d Bytes) %s";
	
	@Override
	public void update(EventBean[] newData, EventBean[] oldEvents) {
		//host, ipv4_src_addr, l4_src_port, ipv4_dst_addr, l4_dst_port, in_bytes, first_switched
		try {
			NetflowEvent protocol = (NetflowEvent) newData[0].getUnderlying();
			Integer in_bytes = protocol.getIn_bytes();
			String srcaddr = protocol.getIpv4_src_addr();
			Integer srcport = protocol.getL4_src_port();
			String dstaddr = protocol.getIpv4_dst_addr();
			Integer dstport = protocol.getL4_dst_port();
			ZonedDateTime first_switched = protocol.getFirst_switched();
			if ((int) protocol.getProtocol() == 6) 
				LOG.info(String.format(format, "TCP", srcaddr, srcport, dstaddr, dstport, in_bytes, first_switched.toString()));
			else if ((int) protocol.getProtocol() == 17)
				LOG.info(String.format(format, "UDP", srcaddr, srcport, dstaddr, dstport, in_bytes, first_switched.toString()));
		} catch (Exception ex) {
			LOG.error("the underlying object is an instance of {}", newData[0].getUnderlying());
		}
	}
	
}
