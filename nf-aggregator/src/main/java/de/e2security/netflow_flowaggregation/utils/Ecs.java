package de.e2security.netflow_flowaggregation.utils;

/*
 * ecs.version 0.1.0
 */
public final class Ecs {
	public static class Schema {
		public enum Netflow {
			HOST("host.ip"), 													
			IPV4_SRC_ADDR("source.ip"), 												
			L4_SRC_PORT("source.port"), 										
			IPV4_DST_ADDR("destination.ip"), 										
			L4_DST_PORT("destination.port"),									
			PROTOCOL("network.iana_number"),								
			IN_FLOW_SEQ_NUM("netflow.inbound.sequence_number"), 	
			IN_FLOW_RECORDS("netflow.inbound.records"),						
			OUT_FLOW_SEQ_NUM("netflow.outbound.sequence_number"),	
			OUT_FLOW_RECORDS("netflow.outbound.records"),					
			IN_BYTES("network.inbound.bytes"),							
			OUT_BYTES("network.outbound.bytes"),						
			IN_PKTS("network.inbound.packets"),						
			OUT_PKTS("network.outbound.packets"),					
			IN_TCP_FLAGS("network.inbound.tcp_flags"),					
			OUT_TCP_FLAGS("network.outbound.tcp_flags"),				
			IN_FIRST_SWITCHED("netflow.inbound.first_switched"),		
			OUT_FIRST_SWITCHED("netflow.outbound.first_switched"), 	
			IN_LAST_SWITCHED("netflow.inbound.last_switched"),			
			OUT_LAST_SWITCHED("netflow.outbound.last_switched");
			
			private final String value;
			
			private Netflow(String value) {
				this.value = value;
			}
			
			public String asEcs() {
				return value;
			}
		}
	}
}
