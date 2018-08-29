package de.e2security.e2netwatch.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = { "de.e2security.e2netwatch.rest" })
public class MvcConfig {
	
	public MvcConfig() {
        super();
	}

}
