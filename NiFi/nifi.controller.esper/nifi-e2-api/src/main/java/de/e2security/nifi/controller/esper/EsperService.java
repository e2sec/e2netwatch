package de.e2security.nifi.controller.esper;

import org.apache.nifi.annotation.documentation.CapabilityDescription;
import org.apache.nifi.annotation.documentation.Tags;
import org.apache.nifi.controller.ControllerService;
import org.apache.nifi.processor.exception.ProcessException;

import com.espertech.esper.client.EPServiceProvider;

@Tags({"e2","esper"})
@CapabilityDescription("Esper Service API")
public interface EsperService extends ControllerService {

    public EPServiceProvider execute()  throws ProcessException;

}
