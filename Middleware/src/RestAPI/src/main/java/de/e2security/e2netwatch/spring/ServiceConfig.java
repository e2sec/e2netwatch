package de.e2security.e2netwatch.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = { "de.e2security.e2netwatch.business" })
public class ServiceConfig {
	
	public ServiceConfig() {
        super();
	}
	
}
