package de.e2security.processors.e2esper.utilities;

public final class UnwantedProtocolProcessorHelper {
	
	public static String concatenator(String protocol, String sPorts) {
    	sPorts.replaceAll(" ", "");
    	String[] ports = sPorts.split(",");
    	StringBuilder builder = new StringBuilder("(");
	    	builder.append("network.iana_number=");
	    	switch (protocol.toLowerCase()) {
	    		case "tcp": builder.append(6);
	    			   break;
	    		case"udp": builder.append(17);
	    			   break;
	    	}
	    	builder.append(" and");
	    		builder.append(" (");
			    	int counter = 0;
			    	for(String port : ports) {
			    		builder.append("source.port=" + port + " or destination.port=" + port);
			    		//prevent adding or to the last port in array
			    		counter++;
			    		if (counter < ports.length) {
			    			builder.append(" or ");
			    		}
			    	}
		    builder.append(")");
		builder.append(")");
    	return builder.toString();
    }
	
    public static String concatenatePorts(String unwantedTcpPortsPropertyValue, String unwantedUdpPortsPropertyValue) {
    	StringBuilder resBuilder = new StringBuilder();
    	String tcpConc = concatenator("tcp", unwantedTcpPortsPropertyValue);
    	String udpConc = concatenator("udp", unwantedUdpPortsPropertyValue);
    	resBuilder.append("(");
    		resBuilder.append(tcpConc);
    	resBuilder.append(")");
    	resBuilder.append(" or ");
    	resBuilder.append("(");
    		resBuilder.append(udpConc);
    	resBuilder.append(")");
    	return resBuilder.toString();
    }
}
