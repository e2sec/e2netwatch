package de.e2security.processors.e2esper.utilities;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.nifi.flowfile.FlowFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TransformerWithMetrics implements EventTransformer {
	
	final FlowFile ff;
	
	public TransformerWithMetrics(FlowFile ff) {
		this.ff = ff;
	}

	@Override
	public Map<String, Object> transform(final String eventAsJson) throws IOException {
		final Map<String,String> ffAttributesAsMap = ff.getAttributes();
		final Map<String,Object> eventAsMap = new HashMap<>();
		final ObjectMapper mapper = new ObjectMapper();
		eventAsMap.putAll(mapper.readValue(eventAsJson, new TypeReference<Map<String,Object>>(){}));
		//duplicates checker. TODO: evtl. utilize GuavaMap to handle duplicates -> more complex logic is required. 
		long numberOfDuplicates = ffAttributesAsMap.entrySet().stream().filter(entryset -> eventAsMap.containsKey(entryset.getKey())).count();
		if (numberOfDuplicates > 0) 
			throw new RuntimeException("some args of event schema overlaps with the flow file attributes...");
		eventAsMap.putAll(ffAttributesAsMap);
		return eventAsMap;
	}

}
