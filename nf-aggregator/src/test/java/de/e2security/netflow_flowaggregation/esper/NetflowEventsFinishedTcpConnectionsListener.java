package de.e2security.netflow_flowaggregation.esper;

import java.util.ArrayList;
import java.util.List;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

import de.e2security.netflow_flowaggregation.model.protocols.TcpConnection;

public class NetflowEventsFinishedTcpConnectionsListener implements UpdateListener {

	private List<TcpConnection> finished = new ArrayList<>();
	private boolean stdout;
	
	public NetflowEventsFinishedTcpConnectionsListener(boolean stdout) {
		this.stdout = stdout;
	}
	
	@Override
	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		TcpConnection conn = (TcpConnection) newEvents[0].getUnderlying();
		if (stdout) System.out.println(conn);
		if (isConnectionFinished(conn)) {
			finished.add(conn);
		}
	}

	private boolean isConnectionFinished(TcpConnection conn) {
		if (EsperTestUtil.hasTcpEventsCrossReference(conn)) 
			return ((conn.getIn_tcp_flags() & 1 ) == 1) && ( (conn.getOut_tcp_flags() & 1) == 1);
		else
			return false;
	}
	
	public List<TcpConnection> getFinishedConns() {
		return finished;
	}

}
