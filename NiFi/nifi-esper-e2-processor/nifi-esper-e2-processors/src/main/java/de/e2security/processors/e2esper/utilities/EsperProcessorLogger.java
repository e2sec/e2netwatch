package de.e2security.processors.e2esper.utilities;

public final class EsperProcessorLogger {
	
	public static class Failure extends ProcessorLogger {
		public Failure(String message, String object) {
			super("ESPER FAILED", message, object);
		}
	}
	
	public static String failure(String message, String object) {
		return new EsperProcessorLogger.Failure(message, object).compose();
	}
	
	public static class Success extends ProcessorLogger {
		public Success(String message, String object) {
			super("ESPER SUCCESS", message, object);
		}
	}
	
	public static String success(String message, String object) {
		return new EsperProcessorLogger.Success(message, object).compose();
	}

}
