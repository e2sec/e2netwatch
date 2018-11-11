package de.e2security.netflow_flowaggregation.esper;

import java.util.ArrayList;
import java.util.List;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

import de.e2security.netflow_flowaggregation.model.protocols.TcpConnection;

public class NetflowEventsRejectedTcpConnectionsListener implements UpdateListener {
	
	boolean stdout = false;
	
	public NetflowEventsRejectedTcpConnectionsListener(boolean stdout) {
		this.stdout = stdout;
	}
	
	private List<TcpConnection> connRejected = new ArrayList<>();

	@Override
	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		TcpConnection event = (TcpConnection) newEvents[0].getUnderlying();
		if (isConnectionRejected(event)) 
			connRejected.add(event);
	}
	
	private boolean isConnectionRejected(TcpConnection conn) {
		if (EsperTestUtil.hasTcpEventsCrossReference(conn)) 
			return  (( (conn.getIn_tcp_flags() & 2) == 2) && ( (conn.getIn_tcp_flags() & 16) == 0))
					&& (conn.getOut_tcp_flags() & 4) == 4; 
		else 
			return false;
	}
	
	public List<TcpConnection> getRejectedList() {
		return connRejected;
	}
	
	public int getNumberOfRejectedConnections() {
		return this.connRejected.size();
	}

}
