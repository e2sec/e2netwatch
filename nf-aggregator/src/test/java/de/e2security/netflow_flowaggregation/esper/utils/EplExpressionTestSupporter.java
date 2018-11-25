package de.e2security.netflow_flowaggregation.esper.utils;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import de.e2security.netflow_flowaggregation.model.protocols.ProtocolRegister;

public final class EplExpressionTestSupporter {
	
	public static String selectNetStreamOrdered() {
		return "select * from NetflowEventOrdered";
	}
	
	public static String selectTcpConnections() {
		return "select * from ProtocolRegister where protocol=6";
	}
	
	public static String selectUdpConnections() {
		return "select * from ProtocolRegister where protocol=17";
	}
	
	/*
	 * timeInterval:
	 * 	e.g. TCP = 60
	 * 		 UDP = 120
	 */
	public static boolean hasTcpEventsCrossReference(ProtocolRegister conn, long timeInterval) {
		DateTimeFormatter formater = DateTimeFormatter.ISO_INSTANT;	
		long switchedTimeDelta = 0;
		try {
			Date lastInSwitched = Date.from(Instant.from(formater.parse(conn.getIn_last_switched().toString())));
			Date lastOutSwitched = Date.from(Instant.from(formater.parse(conn.getOut_last_switched().toString())));
			switchedTimeDelta = lastInSwitched.getTime() - lastOutSwitched.getTime();
		} catch (Exception ex) {}
		return     (conn.getIn_ipv4_src_addr().equals(conn.getOut_ipv4_dst_addr()))
				&& (conn.getOut_ipv4_src_addr().equals(conn.getIn_ipv4_dst_addr()))
				&& ( (int) conn.getIn_l4_dst_port() == (int) conn.getOut_14_src_port() )
				&& ( (int) conn.getOut_14_dst_port() == (int) conn.getIn_l4_src_port())
				&& ( switchedTimeDelta <= timeInterval); //timer:within(Xsec);
	}
}
