package de.e2security.netflow_flowaggregation.esper;

import de.e2security.netflow_flowaggregation.esper.NetflowEventEplExpressions.NetflowEventEplSupporter;

public final class TcpEplExpressions extends NetflowEventEplSupporter {


	/**
	 * Finished TCP Flow: FIN flag (1) set on both flows 
	 * @param protocolFields can be either Tcp or Udp fields
	 * @return EPL Expression 
	 */
	public static String eplFinishedFlows() {
		return "insert into ProtocolRegister select"
				+ " 'Finished TCP' as description"
				+ fields()
				+ " from pattern [every a=NetflowEventOrdered(protocol=6 and (tcp_flags&1)=1) ->"
				+ " b=NetflowEventOrdered(protocol=6 and (tcp_flags&1)=1 and host=a.host and "
				+ timeIntervalLastSwitchedChecker() + " and "
				+ connectionXReferenceChecker() + ")" 
				+ " where timer:within(60 sec)]";
	}

	/**
	 * Rejected TCP connection
	 * first flow: SYN (2) set, but ACK (16) not set
	 * second flow: RST (4) set
	 * As we cannot rely on the correct order of netflow data,
	 * so we define two EPL statements 
	 * @param protocolFields can be either Tcp or Udp fields
	 * @return EPL Expression
	 */
	public static String eplRejectedFlows(String pattern) {
		return "insert into ProtocolRegister select"
				+ " 'Rejected TCP' as description"
				+ fields()
				+ " from pattern " + pattern + " and "
				+ timeIntervalLastSwitchedChecker() + " and "
				+ connectionXReferenceChecker() + ")"
				+ " where timer:within(60 sec)]";
	}

	public static String eplRejectedPatternSyn2Ack16() {
		return "[every a=NetflowEventOrdered(protocol=6 and (tcp_flags&2)=2 and (tcp_flags&16)=0) -> "
				+ " b=NetflowEventOrdered(protocol=6 and (tcp_flags&4)=4 and host=a.host ";
	}

	public static String eplRejectedPatternRst4() {
		return "[every a=NetflowEventOrdered(protocol=6 and (tcp_flags&4)=4) ->"
				+ " b=NetflowEventOrdered(protocol=6 and (tcp_flags&2)=2 and (tcp_flags&16)=0 and host=a.host ";
	}
}
