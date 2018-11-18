package de.e2security.netflow_flowaggregation.esper;

import de.e2security.netflow_flowaggregation.esper.NetflowEventEplExpressions.NetflowEventEplSupporter;

public final class UdpEplExpressions extends NetflowEventEplSupporter {
	
	/**
	 * Finished UDP connection
	 * no further packets in both directions after 2 minutes
	 * @formatter:on
	 */
	public static String eplFinishedUDPFlows() {
		return "insert into ProtocolRegister select"
				+ " 'Finished UDP' as description"
				+ fields()
				+ " from pattern [every a=NetflowEventOrdered(protocol=17) ->"
				+ " b=NetflowEventOrdered(protocol=17 and host=a.host "
				+ connectionXReferenceChecker() + ")"
				+ " -> "
				+ "(timer:interval(120 sec)"
				+ " and not d=NetflowEventOrdered(protocol=17"
				+ " and host=a.host"
				+ connectionXReferenceChecker() + ")"
				+ " and not "
				+ " e=NetflowEventOrdered(protocol=17 and host=a.host "
				+ connectionXReferenceChecker() + ")"
				+ ")]";
	}
	
	
	public static String eplFinishedUDPFlows2() {
		return "insert into ProtocolRegister select"
				+ " 'Finished UDP' as description"
				+ fields()
				+ " from pattern [" 
				+ 			"every a=NetflowEventOrdered(protocol=17) ->"
				+ 			" b=NetflowEventOrdered(protocol=17 and host=a.host"
				+ 			" and ipv4_src_addr = a.ipv4_dst_addr"
				+ 			" and l4_src_port   = a.l4_dst_port"
				+ 			" and ipv4_dst_addr = a.ipv4_src_addr"
				+ 			" and l4_dst_port   = a.l4_src_port" + ")"
				+			"->"
				+ 			" timer:interval(120 sec)"
				+ 			" and not "
				+ 			" c=NetflowEventOrdered(protocol=17 and host=a.host"
				+			" and ipv4_src_addr = b.ipv4_dst_addr"
				+ 			" and l4_src_port   = b.l4_dst_port"
				+	 		" and ipv4_dst_addr = b.ipv4_src_addr"
				+	 		" and l4_dst_port   = b.l4_src_port" + ")"
				+			" and not "
				+ 			" d=NetflowEventOrdered(protocol=17 and host=a.host "
				+			" and ipv4_src_addr = a.ipv4_dst_addr"
				+ 			" and l4_src_port   = a.l4_dst_port"
				+ 			" and ipv4_dst_addr = a.ipv4_src_addr"
				+ 			" and l4_dst_port   = a.l4_src_port" + ")"
				+ "]";
	}
}
