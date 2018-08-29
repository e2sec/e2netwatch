package hr.eito.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = { "hr.eito.kynkite.rest" })
public class MvcConfig {
	
	public MvcConfig() {
        super();
	}

}
