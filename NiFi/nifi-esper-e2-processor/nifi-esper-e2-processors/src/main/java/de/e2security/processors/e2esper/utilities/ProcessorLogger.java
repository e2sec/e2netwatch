package de.e2security.processors.e2esper.utilities;

import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

	abstract class AbstractDebugger implements Map.Entry<String,Pair<String,String>> {
	
		private final String header;
	
		public AbstractDebugger(String header) {
			this.header = new StringBuilder("[").append(header).append("]").toString();
		}
	
		public String compose() {
			return getKey() + getValue();
		}
	
		public abstract String setObject();
		public abstract String setMessage();
	
		@Override
		public String getKey() {
			return this.header;
		}
	
		@Override
		public Pair<String, String> getValue() {
			return new ImmutablePair<>(setMessage(), setObject());
		}
	
		@Override
		public Pair<String, String> setValue(Pair<String, String> value) {
			throw new UnsupportedOperationException();
		}
	
	}
	
	public abstract class ProcessorLogger extends AbstractDebugger {
		
		private final String object;
		private final String message;
	
		public ProcessorLogger(String header, String message, String object) {
			super(header);
			this.message = message;
			this.object = object;
		}

		@Override
		public String setObject() {
			return this.object;
		}

		@Override
		public String setMessage() {
			return this.message;
		}
		
}

