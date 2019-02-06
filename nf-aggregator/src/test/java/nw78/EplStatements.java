package nw78;

public class EplStatements {
		
		private static String unwantedConnectionsList = 
				 "network.iana_number=6 and (source.port=23 or destination.port=21)"; 
		
		public static String detectUnwantedProtocols = 
				"insert irstream into AlarmDetected"
				+ " select source.port"
				+ " from ProtocolRegister(" + unwantedConnectionsList + ")";

		public static String selectAlarms = 
				"select source.port from AlarmDetected";
}
