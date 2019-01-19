package de.e2security.processors.e2esper.utilities;

import java.io.IOException;
import java.util.Map;

public interface EventTransformer {
	
	Map<String,Object> transform(String eventAsJson) throws IOException;
	
}
