package de.e2security.processors.e2esper.utilities;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.nifi.flowfile.FlowFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TransformerWithAttributes implements EventTransformer {
	
	final FlowFile ff;
	
	public TransformerWithAttributes(FlowFile ff) {
		this.ff = ff;
	}

	/**
	 * MAPPING FOR CREATING EPL SCHEMA AGAINS EVENT JSON FORMAT:
	 * JSON ARRAY  => ARRAYLIST (e.g. create map schema EVENT_NAME as (object_name ArrayList))
	 * JSON STRING => STRING    (e.g. as (object_name string))
	 * JSON NUMBER => LONG/INT  (e.g. as (object_name long/int)) 
	 */
	
	@Override
	public Map<String, Object> transform(final String eventAsJson) throws IOException {
		final Map<String,String> ffAttributesAsMap = ff.getAttributes(); 
		final Map<String,Object> eventAsMap = new HashMap<>();
		final ObjectMapper mapper = new ObjectMapper();
		eventAsMap.putAll(mapper.readValue(eventAsJson, new TypeReference<Map<String,Object>>(){}));
		eventAsMap.put(CommonSchema.EVENT.flowFileAttributes.toString(),ffAttributesAsMap);
		return eventAsMap;
	}

}
