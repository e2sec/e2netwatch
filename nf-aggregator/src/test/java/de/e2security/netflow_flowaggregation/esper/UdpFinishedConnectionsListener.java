package de.e2security.netflow_flowaggregation.esper;

import java.util.ArrayList;
import java.util.List;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

import de.e2security.netflow_flowaggregation.esper.utils.EplExpressionTestSupporter;
import de.e2security.netflow_flowaggregation.model.protocols.ProtocolRegister;

public class UdpFinishedConnectionsListener implements UpdateListener {
	
	private List<ProtocolRegister> finishedUpdList = new ArrayList<>();

	@Override
	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		ProtocolRegister udp = (ProtocolRegister) newEvents[0].getUnderlying();
		if (udp.getIn_host().equals(udp.getOut_host())) {
			if (EplExpressionTestSupporter.hasTcpEventsCrossReference(udp, 120)) {
				
			}
		}
	}

}
