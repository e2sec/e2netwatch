package de.e2security.netflow_flowaggregation.esper;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
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
		DateTimeFormatter formater = DateTimeFormatter.ISO_INSTANT;	
		long switchedTimeDelta = 0;
		try {
			Date lastInSwitched = Date.from(Instant.from(formater.parse(conn.getIn_last_switched().toString())));
			Date lastOutSwitched = Date.from(Instant.from(formater.parse(conn.getOut_last_switched().toString())));
			switchedTimeDelta = lastInSwitched.getTime() - lastOutSwitched.getTime();
		} catch (Exception ex) {}
		return   ( (conn.getIn_tcp_flags() & 1 ) == 1) && ( (conn.getOut_tcp_flags() & 1) == 1)
				&& (conn.getIn_ipv4_src_addr().equals(conn.getOut_ipv4_dst_addr()))
				&& (conn.getOut_ipv4_src_addr().equals(conn.getIn_ipv4_dst_addr()))
				&& ( (int) conn.getIn_l4_dst_port() == (int) conn.getOut_14_src_port() )
				&& ( (int) conn.getOut_14_dst_port() == (int) conn.getIn_l4_src_port())
				&& ( switchedTimeDelta <= 60); //timer:within(60sec) 
	}
	
	public List<TcpConnection> getFinishedConns() {
		return finished;
	}

}
