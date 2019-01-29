package de.e2security.netflow_flowaggregation.esper;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

import de.e2security.netflow_flowaggregation.esper.epl.TcpEplExpressions;
import de.e2security.netflow_flowaggregation.esper.utils.EplExpressionTestSupporter;
import de.e2security.netflow_flowaggregation.model.protocols.ProtocolRegister;

public class TcpRejectedConnectionsListener implements UpdateListener {

	private static final Logger LOG = LoggerFactory.getLogger(TcpRejectedConnectionsListener.class);

	boolean stdout = false;
	String pattern = "";
	
	public TcpRejectedConnectionsListener(boolean stdout, String pattern) {
		this.stdout = stdout;
		this.pattern = pattern;
	}
	
	private List<ProtocolRegister> connRejected = new ArrayList<>();

	@Override
	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		ProtocolRegister event = (ProtocolRegister) newEvents[0].getUnderlying();
		if (stdout) LOG.info(event.toString());
		if (isConnectionRejected(event, pattern)) 
			connRejected.add(event);
	}
	
	private boolean isConnectionRejected(ProtocolRegister conn, String pattern) {
		boolean res = false;
		if (EplExpressionTestSupporter.hasTcpEventsCrossReference(conn, 60))  {
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
	
	public List<ProtocolRegister> getRejectedList() {
		return connRejected;
	}
	
	public int getNumberOfRejectedConnections() {
		return this.connRejected.size();
	}

}
