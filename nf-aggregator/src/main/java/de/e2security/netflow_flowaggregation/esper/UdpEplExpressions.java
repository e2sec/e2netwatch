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
}
