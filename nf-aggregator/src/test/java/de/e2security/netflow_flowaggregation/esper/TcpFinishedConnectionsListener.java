package de.e2security.netflow_flowaggregation.esper;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

import de.e2security.netflow_flowaggregation.esper.utils.EplExpressionTestSupporter;
import de.e2security.netflow_flowaggregation.model.protocols.ProtocolRegister;

public class TcpFinishedConnectionsListener implements UpdateListener {

	private static final Logger LOG = LoggerFactory.getLogger(TcpFinishedConnectionsListener.class);

	private List<ProtocolRegister> finished = new ArrayList<>();
	private boolean stdout;
	
	public TcpFinishedConnectionsListener(boolean stdout) {
		this.stdout = stdout;
	}
	
	@Override
	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		ProtocolRegister conn = (ProtocolRegister) newEvents[0].getUnderlying();
		if (stdout) LOG.info(conn.toString());
		if (isConnectionFinished(conn)) {
			finished.add(conn);
		}
	}

	private boolean isConnectionFinished(ProtocolRegister conn) {
		if (EplExpressionTestSupporter.hasTcpEventsCrossReference(conn, 60)) 
			return ((conn.getIn_tcp_flags() & 1 ) == 1) && ( (conn.getOut_tcp_flags() & 1) == 1);
		else
			return false;
	}
	
	public List<ProtocolRegister> getFinishedConns() {
		return finished;
	}

}
