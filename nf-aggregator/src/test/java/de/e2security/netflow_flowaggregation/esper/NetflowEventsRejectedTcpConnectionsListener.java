package de.e2security.netflow_flowaggregation.esper;

import java.util.ArrayList;
import java.util.List;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

import de.e2security.netflow_flowaggregation.model.protocols.TcpConnection;

public class NetflowEventsRejectedTcpConnectionsListener implements UpdateListener {
	
	boolean stdout = false;
	String pattern = "";
	
	public NetflowEventsRejectedTcpConnectionsListener(boolean stdout, String pattern) {
		this.stdout = stdout;
		this.pattern = pattern;
	}
	
	private List<TcpConnection> connRejected = new ArrayList<>();

	@Override
	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		TcpConnection event = (TcpConnection) newEvents[0].getUnderlying();
		if (isConnectionRejected(event, pattern)) 
			connRejected.add(event);
	}
	
	private boolean isConnectionRejected(TcpConnection conn, String pattern) {
		boolean res = false;
		if (EsperTestUtil.hasTcpEventsCrossReference(conn))  {
			if (pattern.equals(TcpEplExpressions.eplRejectedPatternSyn2Ack16())) {
				res = (( (conn.getIn_tcp_flags() & 2) == 2) && ( (conn.getIn_tcp_flags() & 16) == 0))
						&& (conn.getOut_tcp_flags() & 4) == 4; 
			} else if (pattern.equals(TcpEplExpressions.eplRejectedPatternRst4())) {
				res = ((conn.getIn_tcp_flags() & 4) == 4) 
					    && ((conn.getOut_tcp_flags() & 2) == 2) && ((conn.getOut_tcp_flags() & 16) == 0);
			} else {
				throw new RuntimeException("pattern is not recognized");
			}
		} 
		return res;
	}
	
	public List<TcpConnection> getRejectedList() {
		return connRejected;
	}
	
	public int getNumberOfRejectedConnections() {
		return this.connRejected.size();
	}

}
