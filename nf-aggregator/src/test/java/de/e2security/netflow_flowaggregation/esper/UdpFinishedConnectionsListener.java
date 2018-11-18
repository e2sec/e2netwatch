package de.e2security.netflow_flowaggregation.esper;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

import de.e2security.netflow_flowaggregation.esper.utils.EplExpressionTestSupporter;
import de.e2security.netflow_flowaggregation.model.protocols.ProtocolRegister;

public class UdpFinishedConnectionsListener implements UpdateListener {
	
	private static final Logger LOG = LoggerFactory.getLogger(UdpFinishedConnectionsListener.class);

	private List<ProtocolRegister> finishedUdpList = new ArrayList<>();

	@Override
	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		ProtocolRegister udp = (ProtocolRegister) newEvents[0].getUnderlying();
		if (udp.getIn_host().equals(udp.getOut_host())) {
			if (EplExpressionTestSupporter.hasTcpEventsCrossReference(udp, 120)) {
				finishedUdpList.add(udp);
			}
		}
	}
	
	public List<ProtocolRegister> getFinishedUdpList() {
		return this.finishedUdpList;
	}

}
