package hr.eito.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = { "hr.eito.kynkite.business" })
public class ServiceConfig {
	
	public ServiceConfig() {
        super();
	}
	
}
