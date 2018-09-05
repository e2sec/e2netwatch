package de.e2security.e2netwatch.run;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ErrorMvcAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

import de.e2security.e2netwatch.spring.AqlJpaConfig;
import de.e2security.e2netwatch.spring.MvcConfig;
import de.e2security.e2netwatch.spring.PropertiesConfig;
import de.e2security.e2netwatch.spring.ServiceConfig;
import de.e2security.e2netwatch.spring.ServletConfig;
import de.e2security.e2netwatch.spring.UserJpaConfig;
import de.e2security.e2netwatch.spring.WebSecurity;
import de.e2security.e2netwatch.spring.setup.MyApplicationContextInitializer;

@SpringBootApplication(exclude = {
		ErrorMvcAutoConfiguration.class
})
public class App extends SpringBootServletInitializer {
	
	private final static Object[] CONFIGS = {
			UserJpaConfig.class
			, AqlJpaConfig.class
			, ServiceConfig.class
			, MvcConfig.class
			, PropertiesConfig.class
			, ServletConfig.class
			, WebSecurity.class
			, App.class
    };
	
	@Override
    protected SpringApplicationBuilder configure(final SpringApplicationBuilder application) {
        return application.sources(CONFIGS).initializers(new MyApplicationContextInitializer());
    }

    public static void main(final String... args) {
        final SpringApplication springApplication = new SpringApplication(CONFIGS);
        springApplication.addInitializers(new MyApplicationContextInitializer());
        springApplication.run(args);
    }

}
